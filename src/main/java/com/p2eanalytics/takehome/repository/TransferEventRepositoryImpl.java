package com.p2eanalytics.takehome.repository;

import com.p2eanalytics.takehome.helper.RateInfoHelper;
import com.p2eanalytics.takehome.model.TransferEvent.ContractTokenEvent;
import com.p2eanalytics.takehome.model.TransferEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TransferEventRepositoryImpl implements TransferEventRepositoryCustom {
    @Autowired
    MongoOperations mongoOperations;

    @Override
    public List<RateInfoHelper> getRateInfoByEventType(ContractTokenEvent eventType) {
        List<AggregationOperation> ops = new ArrayList<>();

        ops.add(Aggregation.match(new Criteria("eventType").is(eventType)));

        ops.add(Aggregation.group("eventType")
                .sum("amount").as("amount")
                .min("blockNumber").as("blockFrom")
                .max("blockNumber").as("blockTo")
                .max("date").as("date")
        );

        Aggregation agg = Aggregation.newAggregation(ops);

        AggregationResults<TransferEvent> result = mongoOperations.aggregate(agg,"transferEvent", TransferEvent.class);

        List<Map<String, Object>> set =  (List<Map<String, Object>>) result.getRawResults().get("results") ;

        RateInfoHelper rate = new RateInfoHelper();
        List<RateInfoHelper> rates = new ArrayList<>();
        for(Map<String, Object> obj : set) {
            rate.setTimestamp((Date) obj.get("date"));
            rate.setAmount((double) obj.get("amount"));
            rate.setBlockFrom(BigInteger.valueOf(Long.valueOf((String) obj.get("blockFrom"))));
            rate.setBlockTo(BigInteger.valueOf(Long.valueOf((String) obj.get("blockTo"))));
            rates.add(rate);
        }
        return rates;
    }
}
