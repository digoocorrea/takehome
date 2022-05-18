package com.p2eanalytics.takehome.moralis;

import lombok.Data;

@Data
public class MoralisTransferDataModel {
    private String from;
    private String to;
    private String value;
}
