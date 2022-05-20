package com.p2eanalytics.takehome.helper;

import lombok.Data;

import java.math.BigInteger;

@Data
public class InflationRateHelper {
    Long timestamp;
    Double netSupply;
    Double mintAmount;
    Double mintPercent;
    Double burnAmount;
    Double burnPercent;
    Double inflationRate;
}
