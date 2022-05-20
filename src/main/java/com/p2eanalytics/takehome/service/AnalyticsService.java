package com.p2eanalytics.takehome.service;

import com.p2eanalytics.takehome.helper.HandleMintBurnHelper;
import com.p2eanalytics.takehome.helper.InflationRateHelper;
import com.p2eanalytics.takehome.model.BurnRate;
import com.p2eanalytics.takehome.model.MintRate;
import com.p2eanalytics.takehome.repository.BurnRateRepository;
import com.p2eanalytics.takehome.repository.MintRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.sql.Date;

@Service
public class AnalyticsService {
    @Autowired
    BurnRateRepository burnRep;
    @Autowired
    MintRateRepository mintRep;

    public List<InflationRateHelper> getInflationRates(){
        List<InflationRateHelper> inflationRates = new ArrayList<>();

        HashMap<Long, HandleMintBurnHelper> dateRatesMap = getMappedRatesByDate();

        dateRatesMap.forEach((date, rate) -> {
            InflationRateHelper inflation = new InflationRateHelper();
            inflation.setTimestamp(date);
            MintRate mint = rate.getMint();
            BurnRate burn = rate.getBurn();
            inflation.setMintAmount(mint.getMintAmount());
            inflation.setBurnAmount(burn.getBurnAmount());
            Double netSupply = burn.getBlockTo().compareTo(mint.getBlockTo()) > 1 ?
                                        burn.getNetSupply() :
                                        mint.getNetSupply();
            inflation.setMintPercent(mint.getMintAmount()/netSupply);
            inflation.setBurnPercent(burn.getBurnAmount()/netSupply);
            inflation.setNetSupply(netSupply);
            inflation.setInflationRate(inflation.getMintPercent() - inflation.getBurnPercent());
            inflationRates.add(inflation);

        });
        Comparator<InflationRateHelper> sortByTimestamp =  new Comparator<InflationRateHelper>() {
            @Override
            public int compare(InflationRateHelper rate1, InflationRateHelper rate2) {

                return rate1.getTimestamp() > rate2.getTimestamp() ? 1 : -1;
            }
        };
        Collections.sort(inflationRates,sortByTimestamp);

        return inflationRates;
    }

    private HashMap<Long, HandleMintBurnHelper> getMappedRatesByDate(){
        HashMap<Long, HandleMintBurnHelper> dateRatesMap = new HashMap<>();

        for(MintRate mint : mintRep.findAll(Sort.by(Sort.Direction.ASC, "timestamp"))){
            HandleMintBurnHelper helper = new HandleMintBurnHelper();
            helper.setMint(mint);
            dateRatesMap.put(mint.getTimestamp(), helper);
        }

        for(BurnRate burn : burnRep.findAll(Sort.by(Sort.Direction.ASC, "timestamp"))){
            HandleMintBurnHelper helper = dateRatesMap.get(burn.getTimestamp());
            if(helper == null){
                helper = new HandleMintBurnHelper();
            }

            helper.setBurn(burn);
            dateRatesMap.put(burn.getTimestamp(), helper);
        }

        return dateRatesMap;
    }
}
