package com.p2eanalytics.takehome.moralis;

import lombok.Data;

import java.util.Date;
@Data
public class MoralisTransferEventModel {
    private String transaction_hash;
    private String address;
    private Date block_timestamp;
    private Long block_number;
    private String block_hash;
    private MoralisTransferDataModel data;

    protected MoralisTransferEventModel(MoralisTransferEventModel model){
        this.address = model.address;
        this.block_timestamp = model.block_timestamp;
        this.block_number = model.block_number;
        this.block_hash = model.block_hash;
        this.data = model.data;
    }
}

