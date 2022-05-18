package com.p2eanalytics.takehome.service;

import com.p2eanalytics.takehome.deprecated.service.BlockchainSyncService;
import com.p2eanalytics.takehome.helper.RateInfoHelper;
import com.p2eanalytics.takehome.model.BurnRate;
import com.p2eanalytics.takehome.model.MintRate;
import com.p2eanalytics.takehome.model.TransferEvent;
import com.p2eanalytics.takehome.moralis.MoralisClientService;
import com.p2eanalytics.takehome.moralis.MoralisEventResponse;
import com.p2eanalytics.takehome.moralis.MoralisTransferEventModel;
import com.p2eanalytics.takehome.repository.BurnRateRepository;
import com.p2eanalytics.takehome.repository.MintRateRepository;
import com.p2eanalytics.takehome.repository.TransferEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.*;
@Slf4j
@Service
public class LoadBaseService {
    public String tokenAddress = "0xf693248F96Fe03422FEa95aC0aFbBBc4a8FdD172";
    public String mintAddr = "0x0000000000000000000000000000000000000000"; //0x0000000000000000000000000000000000000000000000000000000000000000
    public String feeAddr =  "0x4e57a39cac2499abeafd3698f7164ecbfde008ee";
    public List<String> excludedBurn = Arrays.asList("0xb5a0ffe202e9223dce018c4fdc9e6b952fac4a2c", "0x4e57a39cac2499abeafd3698f7164ecbfde008ee");
    @Autowired
    MoralisClientService moralis;
    @Autowired
    BlockchainSyncService blockchainService;
    @Autowired
    TransferEventRepository eventRep;

    @Autowired
    MintRateRepository mintRep;
    @Autowired
    BurnRateRepository burnRep;

    public void loadBase(Date dateFrom, Integer page){
        TransferEvent lastEvent = eventRep.findTopByOrderByBlockTimestampAsc();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR, 0);
        Date dateTo = lastEvent != null ? lastEvent.getBlockTimestamp() : cal.getTime();

        dateFrom = cal.getTime();

        Integer maxPage = 0;
        log.info("Starting processing at: " + new Date().toString());
        do {
            log.info("Getting page: " + page);
            MoralisEventResponse events = moralis.fetchTransferEventPageByDate(tokenAddress, dateFrom, dateTo, page);
            if(events.getResult() == null){
                log.error("Error on getting page: " + page);
                log.info("Retrying...");
                continue;
            }
            List<TransferEvent> transfers = new ArrayList<>();
            for(MoralisTransferEventModel event : events.getResult()){
                TransferEvent transfer = new TransferEvent(event);
                if(transfer.getEventType() != null){
                    transfers.add(transfer);
                }
            }
            eventRep.saveAll(transfers);
            maxPage = events.getTotal().intValue()%500 > 0 ?
                    (events.getTotal().intValue()/500) + 1 :
                     events.getTotal().intValue() / 500;
            page++;
        }
        while (page <= maxPage);

        log.info("Processing finished at: " + new Date().toString());

    }

    public void loadMintRates(){

        Double lastMintPercent = 0D;
        for(RateInfoHelper info : eventRep.getRateInfoByEventType(TransferEvent.ContractTokenEvent.MINT)){
            MintRate rate = new MintRate();
            rate.setTimestamp(new java.sql.Date(info.getTimestamp().getTime()));
            rate.setBlockTo(info.getBlockTo());
            rate.setBlockFrom(info.getBlockFrom());
            rate.setMintAmount(info.getAmount());
            String netSupply = blockchainService.getNetSupplyForBlock(info.getBlockTo()).toString();
            rate.setNetSupply(Convert.fromWei(netSupply, Convert.Unit.ETHER).doubleValue());
            rate.setMintPercent(rate.getMintAmount()/rate.getNetSupply());
            rate.setMintVariation(rate.getMintPercent() - lastMintPercent);
            lastMintPercent = rate.getMintPercent();
            mintRep.save(rate);
        }

    }

    public void loadBurnRates(){

        Double lastBurnPercent = 0D;
        for(RateInfoHelper info : eventRep.getRateInfoByEventType(TransferEvent.ContractTokenEvent.BURN)){
            BurnRate rate = new BurnRate();
            rate.setTimestamp(new java.sql.Date(info.getTimestamp().getTime()));
            rate.setBlockTo(info.getBlockTo());
            rate.setBlockFrom(info.getBlockFrom());
            rate.setBurnAmount(info.getAmount());
            String netSupply = blockchainService.getNetSupplyForBlock(info.getBlockTo()).toString();
            rate.setNetSupply(Convert.fromWei(netSupply, Convert.Unit.ETHER).doubleValue());
            rate.setBurnPercent(rate.getBurnAmount()/rate.getNetSupply());
            rate.setBurnVariation(rate.getBurnPercent() - lastBurnPercent);
            lastBurnPercent = rate.getBurnPercent();
            burnRep.save(rate);
        }

    }


}
