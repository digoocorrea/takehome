package com.p2eanalytics.takehome.repository;

import com.p2eanalytics.takehome.model.TransferEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransferEventRepository extends MongoRepository<TransferEvent, String>, TransferEventRepositoryCustom {
    TransferEvent findTopByOrderByBlockTimestampAsc();
}
