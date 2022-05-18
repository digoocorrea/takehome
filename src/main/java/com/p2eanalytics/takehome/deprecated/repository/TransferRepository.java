package com.p2eanalytics.takehome.deprecated.repository;

import com.p2eanalytics.takehome.deprecated.model.Transfer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransferRepository  extends MongoRepository<Transfer, String>, TransferRepositoryCustom {

    @Query("{ '$and': [ { 'timestamp' :{ '$gte' : ?0 } }, { 'timestamp' :{ '$lte' : ?1 } }  ] }")
    List<Transfer> findByDateQuery(Date from, Date to);
}
