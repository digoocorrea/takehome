package com.p2eanalytics.takehome.deprecated.repository;

import com.p2eanalytics.takehome.deprecated.model.SyncBlockStatus;
import com.p2eanalytics.takehome.deprecated.model.SyncBlockStatus.SyncBlockStatusType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyncBlockStatusRepository  extends MongoRepository<SyncBlockStatus, SyncBlockStatusType> {

}
