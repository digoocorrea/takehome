package com.p2eanalytics.takehome.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;


@Data
@Document("mintRate")
public class MintRate {
    @Id
    Date timestamp;
    BigInteger blockFrom;
    BigInteger blockTo;
    Double netSupply;
    Double mintAmount;
    Double mintVariation;
    Double mintPercent;

}
