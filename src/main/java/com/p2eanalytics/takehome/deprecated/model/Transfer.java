package com.p2eanalytics.takehome.deprecated.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.util.Date;

@Data
@Document("transfer")
public class Transfer {
    public enum ContractTokenEvent { MINT, BURN }

    @Id
    String id; //trxHash
    ContractTokenEvent eventType;
    BigInteger blockNumber;
    String from;
    String to;
    Double amount;
    @Indexed
    Date timestamp;
}
