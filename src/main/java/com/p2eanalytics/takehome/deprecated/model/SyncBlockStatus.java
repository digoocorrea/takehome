package com.p2eanalytics.takehome.deprecated.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Data
@Document("syncBlockStatus")
public class SyncBlockStatus {
    public enum SyncBlockStatusType { MINT, BURN }

    @Id
    SyncBlockStatusType id;
    Long blockNumber;
    Long rateBlockNumber;
    Date blockTimestamp;
    Date syncTimestamp;
}
