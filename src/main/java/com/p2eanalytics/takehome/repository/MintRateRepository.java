package com.p2eanalytics.takehome.repository;

import com.p2eanalytics.takehome.model.MintRate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MintRateRepository extends MongoRepository<MintRate, Date> {

    public List<MintRate> findAll();

}
