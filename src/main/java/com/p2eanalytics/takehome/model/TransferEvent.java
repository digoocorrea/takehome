package com.p2eanalytics.takehome.model;

import com.p2eanalytics.takehome.moralis.MoralisTransferEventModel;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
@Document("transferEvent")
public class TransferEvent {
    public enum ContractTokenEvent { MINT, BURN }
    @Id
    String trxHash;
    private String contractAddress;
    private Date blockTimestamp;
    private BigDecimal blockNumber;
    private String blockHash;
    private String from;
    private String to;
    private double amount;
    @Indexed
    private Date date;
    @Indexed
    private ContractTokenEvent eventType;

    public TransferEvent(){}
    public TransferEvent(MoralisTransferEventModel moralis){
        this.trxHash = moralis.getTransaction_hash();
        this.blockTimestamp = moralis.getBlock_timestamp();
        this.blockNumber = BigDecimal.valueOf(moralis.getBlock_number());
        this.blockHash = moralis.getBlock_hash();
        this.from = moralis.getData().getFrom();
        this.to = moralis.getData().getTo();
        this.amount = Convert.fromWei(moralis.getData().getValue(), Convert.Unit.ETHER).doubleValue();
        this.eventType = getTransferType(moralis);
        this.date = getDateForDay(moralis.getBlock_timestamp());
    }

    private ContractTokenEvent getTransferType(MoralisTransferEventModel transfer){
        final String mintAddr = "0x0000000000000000000000000000000000000000";
        final String feeAddr =  "0x4e57a39cac2499abeafd3698f7164ecbfde008ee";
        final List<String> excludedBurn = Arrays.asList("0xb5a0ffe202e9223dce018c4fdc9e6b952fac4a2c", "0x4e57a39cac2499abeafd3698f7164ecbfde008ee");

        if(transfer.getData().getFrom().equals(mintAddr)){
            return ContractTokenEvent.MINT;
        }

        if (excludedBurn.contains(transfer.getData().getFrom())) {
            return null;
        }

        if(transfer.getData().getTo().equals(mintAddr) || transfer.getData().getTo().equals(feeAddr)){
            return ContractTokenEvent.BURN;
        }

        return null;

    }

    @SneakyThrows
    private Date getDateForDay(Date date) {
        SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = dateParser.format(date);
        dateParser.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateParser.parse(dateStr);
    }
}
