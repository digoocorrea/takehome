package com.p2eanalytics.takehome.repository;

import com.p2eanalytics.takehome.model.BurnRate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BurnRateRepository extends MongoRepository<BurnRate, Date> {
    public List<BurnRate> findAll();
}
