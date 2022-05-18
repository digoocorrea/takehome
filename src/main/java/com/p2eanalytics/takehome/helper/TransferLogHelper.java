package com.p2eanalytics.takehome.helper;

import com.p2eanalytics.takehome.deprecated.model.Transfer;
import lombok.Data;
import org.web3j.protocol.core.methods.response.EthLog.LogObject;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Data
public class TransferLogHelper {
    private Transfer.ContractTokenEvent event;
    private boolean removed;
    private BigInteger logIndex;
    private BigInteger transactionIndex;
    private String transactionHash;
    private String blockHash;
    private BigInteger blockNumber;
    private String address;
    private String data;
    private String type;
    private List<String> topics;
    private String from;
    private String to;
    private BigDecimal amount;

    private Date timestamp;

    public TransferLogHelper(LogObject log) {
        this.removed = log.isRemoved();
        this.logIndex = log.getLogIndex();
        this.transactionIndex = log.getTransactionIndex();
        this.transactionHash = log.getTransactionHash();
        this.blockHash = log.getBlockHash();
        this.blockNumber = log.getBlockNumber();
        this.address = log.getAddress();
        this.data = log.getData();
        this.type = log.getType();
        this.topics = log.getTopics();
        this.from = log.getTopics().get(1).replaceFirst("000000000000000000000000", "");
        this.to = log.getTopics().get(2).replaceFirst("000000000000000000000000", "");
        this.amount = Convert.fromWei(Numeric.decodeQuantity(log.getData()).toString(), Convert.Unit.ETHER);

    }

}
