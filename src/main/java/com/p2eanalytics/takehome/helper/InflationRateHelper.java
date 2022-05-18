package com.p2eanalytics.takehome.helper;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class InflationRateHelper {
    Date timestamp;
    Double netSupply;
    Double mintAmount;
    Double mintPercent;
    Double burnAmount;
    Double burnPercent;
    Double inflationRate;
}
