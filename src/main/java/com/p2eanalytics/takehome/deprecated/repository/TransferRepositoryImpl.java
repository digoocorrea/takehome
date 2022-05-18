package com.p2eanalytics.takehome.deprecated.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.p2eanalytics.takehome.helper.RateInfoHelper;
import com.p2eanalytics.takehome.deprecated.model.Transfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import java.math.BigInteger;
import java.sql.Date;
import java.util.*;

public class TransferRepositoryImpl implements TransferRepositoryCustom {
    @Autowired
    MongoOperations mongoOperations;


    @Override
    public RateInfoHelper getRateInfoByDateAndEvent(java.sql.Date from, Date to, Transfer.ContractTokenEvent eventType) {
        List<AggregationOperation> ops = new ArrayList<>();

        ops.add(Aggregation.match(new Criteria("eventType").is(eventType)
                        .andOperator(new Criteria("timestamp").gte(from).lt(to))));

        ops.add(Aggregation.group("eventType")
                .sum("amount").as("amount")
                .min("blockNumber").as("blockFrom")
                .max("blockNumber").as("blockTo")
        );
//        ops.add(Aggregation.project("amount","blockFrom", "blockTo"));

        Aggregation agg = Aggregation.newAggregation(ops);

        AggregationResults<Transfer> result = mongoOperations.aggregate(agg,"transfer", Transfer.class);

        ObjectMapper mapObject = new ObjectMapper();

        List<Map<String, Object>> set =  (List<Map<String, Object>>) result.getRawResults().get("results") ;

        RateInfoHelper rate = new RateInfoHelper();

        for(Map<String, Object> obj : set) {
            rate.setTimestamp(from);
            rate.setAmount((Double) obj.get("amount"));
            rate.setBlockFrom(BigInteger.valueOf(Long.valueOf((String) obj.get("blockFrom"))));
            rate.setBlockTo(BigInteger.valueOf(Long.valueOf((String) obj.get("blockTo"))));
        }
        return rate;
    }
}
