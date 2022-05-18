package com.p2eanalytics.takehome.deprecated.service;

import com.p2eanalytics.takehome.contract.Erc20Wrapper;
import com.p2eanalytics.takehome.helper.RateInfoHelper;
import com.p2eanalytics.takehome.model.MintRate;
import com.p2eanalytics.takehome.deprecated.model.SyncBlockStatus;
import com.p2eanalytics.takehome.deprecated.model.Transfer;
import com.p2eanalytics.takehome.deprecated.model.Transfer.ContractTokenEvent;
import com.p2eanalytics.takehome.repository.MintRateRepository;
import com.p2eanalytics.takehome.deprecated.repository.SyncBlockStatusRepository;
import com.p2eanalytics.takehome.deprecated.repository.TransferRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.Contract;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;
import java.net.ConnectException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class BlockchainSyncService {

    public Web3j web3;
    public Erc20Wrapper token;

    public BigInteger startingBlock = BigInteger.valueOf(13500000);
    public EthBlock currentBlock = null;
    public String mintAddr = "0x0000000000000000000000000000000000000000";
    public String feeAddr =  "0x4e57a39cac2499abeafd3698f7164ecbfde008ee";
    public List<String> excludedBurn = Arrays.asList(
                    "0xb5a0ffe202e9223dce018c4fdc9e6b952fac4a2c",
                    "0x4e57a39cac2499abeafd3698f7164ecbfde008ee");
    public final String tusAddress = "0xf693248F96Fe03422FEa95aC0aFbBBc4a8FdD172";
    public final BigInteger blockOffset = BigInteger.valueOf(500);
    public WebSocketService socket = new WebSocketService(
            "wss://speedy-nodes-nyc.moralis.io/b407b2184c0d3e235e62173c/avalanche/mainnet/ws",
            true);

    @Autowired
    SyncBlockStatusRepository statusRep;
    @Autowired
    TransferRepository transferRep;

    BlockchainSyncService(){
        initializeWeb3j();
    }

    @SneakyThrows
    public void initializeWeb3j(){

        try {
            socket.connect();
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        }

        web3 = Web3j.build(socket);

        try {
            Web3ClientVersion version = web3.web3ClientVersion().send();
            log.info("Version: "+ version.getWeb3ClientVersion());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BigInteger blockNumber = web3.ethBlockNumber().send().getBlockNumber();
        currentBlock = web3.ethGetBlockByNumber(DefaultBlockParameter.valueOf(blockNumber), false).send();
        log.info("Current Block:" + blockNumber);
        token = getToken();
        log.info("Token Name: " + token.name().sendAsync().get() + " Contract: " + token.getContractAddress());
    }

    public void syncTransfers(BigInteger fromBlock){
        syncMintTransfers();
        syncBurnTransfers();
    }
    public void syncMintTransfers(){
        Optional<SyncBlockStatus> statusOpt = statusRep.findById(SyncBlockStatus.SyncBlockStatusType.MINT);
        SyncBlockStatus status = new SyncBlockStatus();

        if(!statusOpt.isPresent()){
            status.setId(SyncBlockStatus.SyncBlockStatusType.MINT);
            status.setSyncTimestamp(new Date());
            status.setBlockNumber(startingBlock.longValue());
            status.setBlockTimestamp(getBlockTimestamp(startingBlock));
            statusRep.save(status);
        } else {
            status = statusOpt.get();
        }

        BigInteger syncBlock = BigInteger.valueOf(status.getBlockNumber() + 1);

        do{
            status.setSyncTimestamp(new Date());
            status.setBlockNumber(syncBlock.longValue());
            status.setBlockTimestamp(getBlockTimestamp(syncBlock));

            syncBlock = syncTransfersHistory(syncBlock, ContractTokenEvent.MINT);

            statusRep.save(status);
        }
        while(syncBlock.compareTo(currentBlock.getBlock().getNumber()) < 0);


    }

    public void syncBurnTransfers(){
        Optional<SyncBlockStatus> statusOpt = statusRep.findById(SyncBlockStatus.SyncBlockStatusType.BURN);
        SyncBlockStatus status = new SyncBlockStatus();

        if(!statusOpt.isPresent()){
            status.setId(SyncBlockStatus.SyncBlockStatusType.BURN);
            status.setSyncTimestamp(new Date());
            status.setBlockNumber(startingBlock.longValue());
            status.setBlockTimestamp(getBlockTimestamp(startingBlock));
            statusRep.save(status);
        } else {
            status = statusOpt.get();
        }

        BigInteger syncBlock = BigInteger.valueOf(status.getBlockNumber() + 1);

        do{
            status = new SyncBlockStatus();
            status.setId(SyncBlockStatus.SyncBlockStatusType.BURN);
            status.setSyncTimestamp(new Date());
            status.setBlockNumber(syncBlock.longValue());
            status.setBlockTimestamp(getBlockTimestamp(syncBlock));

            syncBlock = syncTransfersHistory(syncBlock, ContractTokenEvent.BURN);

            statusRep.save(status);
        }
        while(syncBlock.compareTo(currentBlock.getBlock().getNumber()) < 0);


    }
    public BigInteger syncTransfersHistory(BigInteger fromBlock, ContractTokenEvent eventType){
        log.info("Starting batch on block: "+fromBlock + " for " + eventType);

        AtomicReference<BigInteger> syncingBlock = new AtomicReference<>(fromBlock);

        token.transferEventFlowable(
                getFilterForContract(tusAddress, fromBlock, eventType)
                                     ).forEach(transfer->{

            ContractTokenEvent transferType = getTransferType(transfer);
            if(transferType != null){
                Transfer model = new Transfer();
                model.setId(transfer.log.getTransactionHash());
                model.setFrom(transfer._from);
                model.setTo(transfer._to);
                model.setAmount(Convert.fromWei(transfer._value.toString(), Convert.Unit.ETHER).doubleValue());
                model.setBlockNumber(transfer.log.getBlockNumber());
                model.setTimestamp(getBlockTimestamp(transfer.log.getBlockNumber()));
                model.setEventType(transferType);
                transferRep.save(model);

                syncingBlock.set(transfer.log.getBlockNumber());
            }

        });
        log.info("Finished batch on block: "+syncingBlock.get() + " for " + eventType);
        return syncingBlock.get();
    }

    @Autowired
    MintRateRepository mintRep;
    public MintRate updateMintRates(){
        SyncBlockStatus status = statusRep.findById(SyncBlockStatus.SyncBlockStatusType.MINT).get();
        Calendar syncDate = Calendar.getInstance();
        BigInteger syncingBlock = status.getRateBlockNumber() == null ?
                                    startingBlock :
                                    BigInteger.valueOf(status.getRateBlockNumber() + 1);
        syncDate.setTime(getBlockTimestamp(syncingBlock));
        syncDate.set(Calendar.HOUR_OF_DAY, 0);
        syncDate.set(Calendar.MINUTE, 0);
        syncDate.set(Calendar.SECOND, 0);
        syncDate.set(Calendar.MILLISECOND, 0);
        MintRate rate = new MintRate();
        do{
            rate = handleMintRate(syncDate);
            status.setRateBlockNumber(rate.getBlockTo().longValue());
            syncingBlock.add(BigInteger.valueOf(1));
            statusRep.save(status);
        }
        while (BigInteger.valueOf(status.getBlockNumber()).compareTo(syncingBlock) >= 0);


        return rate;
    }

    private MintRate handleMintRate(Calendar syncDate){
        java.sql.Date from = new java.sql.Date(syncDate.getTime().getTime());
        syncDate.add(Calendar.DATE, 1);
        java.sql.Date to = new java.sql.Date(syncDate.getTime().getTime());

        RateInfoHelper helper = transferRep.getRateInfoByDateAndEvent(from, to, ContractTokenEvent.MINT);
        MintRate rate = new MintRate();
        rate.setMintAmount(helper.getAmount());
        rate.setBlockTo(helper.getBlockTo());
        rate.setBlockFrom(helper.getBlockFrom());
        rate.setTimestamp(new java.sql.Date(from.getTime()));
        BigInteger netSupply = getNetSupplyForBlock(rate.getBlockTo());
        rate.setNetSupply(Convert.fromWei(netSupply.toString(), Convert.Unit.ETHER).doubleValue());

        rate.setMintPercent(rate.getMintAmount() / rate.getNetSupply());
        rate.setMintVariation(0D);
        mintRep.save(rate);
        return rate;
    }


    private ContractTokenEvent getTransferType(Erc20Wrapper.TransferEventResponse transfer){

        if(transfer._from.equals(mintAddr)){
            return ContractTokenEvent.MINT;
        }

        if (excludedBurn.contains(transfer._from)) {
            return null;
        }

        if(transfer._to.equals(mintAddr) || transfer._to.equals(feeAddr)){
            return ContractTokenEvent.BURN;
        }

        return null;

    }

    private Date getBlockTimestamp(BigInteger blockNumber){
        EthBlock block = null;
        Date timestamp = new Date(0);
        try {
            block = web3.ethGetBlockByNumber(
                    DefaultBlockParameter.valueOf(blockNumber), false).send();
            timestamp = new Date(block.getBlock().getTimestamp().longValue() * 1000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return timestamp;
    }

    public void fetchTransfersOnline(){
        // Get the events online as block is finished
        token.transferEventFlowable(getFilterForContract(tusAddress, null, ContractTokenEvent.MINT))
                .subscribe(event -> {
                    System.out.println(event._from);
                }, throwable -> {
                    throwable.printStackTrace();
                });

		web3.ethLogFlowable(getFilterForContract(tusAddress, null, ContractTokenEvent.MINT))
				.subscribe(event -> {
					System.out.println(event.getTransactionHash());
				}, throwable -> {
					throwable.printStackTrace();
				});
    }

    public BigInteger getCurrentBlock(){
        EthBlockNumber currentBlock = null;

        try {
            currentBlock = web3.ethBlockNumber().sendAsync().get(10, TimeUnit.SECONDS);
            // Get the events from current block minus 5000 blocks
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
        return currentBlock.getBlockNumber();
    }
    public BigInteger getNetSupplyForBlock(BigInteger blockNumber){

        BigInteger supply = BigInteger.valueOf(0);

        try {
            token.setDefaultBlockParameter(DefaultBlockParameter.valueOf(blockNumber));

            supply = token.totalSupply().sendAsync().get();
            log.info("TotalSupply for block "+ blockNumber +" is: " + supply);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

        return supply;
    }

    public Erc20Wrapper getToken(){
        ClientTransactionManager transactionManager = new ClientTransactionManager(web3, tusAddress);
        Erc20Wrapper token = Erc20Wrapper.load(tusAddress, web3, transactionManager,
                Contract.GAS_PRICE, Contract.GAS_LIMIT);
        return token;
    }

    private EthFilter getFilterForContract(String contractAddress, BigInteger fromBlock, ContractTokenEvent event){
        if(fromBlock == null){
            fromBlock = getCurrentBlock().subtract(blockOffset);
        }

        BigInteger toBlock = fromBlock.add(blockOffset);

        DefaultBlockParameter startingBlock = DefaultBlockParameter.valueOf(fromBlock);
        DefaultBlockParameter endBlock = DefaultBlockParameter.valueOf(toBlock);

        EthFilter filter = new EthFilter(startingBlock, endBlock, contractAddress)
                .addSingleTopic(getTransferEventSignature());

        switch (event){
            case BURN:
                filter.addOptionalTopics(null, null, mintAddr);
                break;
            case MINT:
                filter.addOptionalTopics(null, mintAddr, null);
                break;
            default:
                break;
        }

        return filter;
    }

    private static String getTransferEventSignature(){
        final Event event =
                new Event(
                        "Transfer",
                        Arrays.asList(
                                new TypeReference<Address>() {},
                                new TypeReference<Address>() {},
                                new TypeReference<Uint256>() {}
                        ));


        return EventEncoder.encode(event);
    }
}
