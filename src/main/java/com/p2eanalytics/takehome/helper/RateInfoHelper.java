package com.p2eanalytics.takehome.helper;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;
@Data

public class RateInfoHelper {
    private double amount;
    private BigInteger blockFrom;
    private BigInteger blockTo;
    private Date timestamp;

}
