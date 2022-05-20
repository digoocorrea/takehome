package com.p2eanalytics.takehome.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigInteger;
import java.sql.Date;

@Data
@Document("burnRate")
public class BurnRate {
    @Id
    Long timestamp;
    BigInteger blockFrom;
    BigInteger blockTo;
    Double netSupply;
    Double burnAmount;
    Double burnVariation;
    Double burnPercent;
}
