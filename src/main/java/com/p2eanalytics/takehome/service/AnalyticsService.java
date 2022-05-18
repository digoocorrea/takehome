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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class AnalyticsService {
    @Autowired
    BurnRateRepository burnRep;
    @Autowired
    MintRateRepository mintRep;

    public List<InflationRateHelper> getInflationRates(){
        List<InflationRateHelper> inflationRates = new ArrayList<>();

        HashMap<Date, HandleMintBurnHelper> dateRatesMap = getMappedRatesByDate();

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

        return inflationRates;
    }

    private HashMap<Date, HandleMintBurnHelper> getMappedRatesByDate(){
        HashMap<Date, HandleMintBurnHelper> dateRatesMap = new HashMap<>();

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

        dateRatesMap.forEach((date, rate) ->{
            if (rate.getMint() == null){
                MintRate mint = new MintRate();
                mint.setMintAmount(0D);
                mint.setBlockFrom(BigInteger.valueOf(0));
                mint.setBlockTo(BigInteger.valueOf(0));
                mint.setMintPercent(0D);
                rate.setMint(mint);
            }
            if (rate.getBurn() == null){
                BurnRate burn = new BurnRate();
                burn.setBurnAmount(0D);
                burn.setBlockFrom(BigInteger.valueOf(0));
                burn.setBlockTo(BigInteger.valueOf(0));
                burn.setBurnPercent(0D);
                rate.setBurn(burn);
            }
        });

        return dateRatesMap;
    }
}
