package com.p2eanalytics.takehome.moralis;

import lombok.Data;

import java.util.List;

@Data
public class MoralisEventResponse {
    private Long total;
    private Integer page;
    private Integer page_size;
    private List<MoralisTransferEventModel> result;
}
