package com.p2eanalytics.takehome.controller;

import com.p2eanalytics.takehome.helper.InflationRateHelper;
import com.p2eanalytics.takehome.model.MintRate;
import com.p2eanalytics.takehome.deprecated.service.BlockchainSyncService;
import com.p2eanalytics.takehome.service.AnalyticsService;
import com.p2eanalytics.takehome.service.LoadBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsRestController {

    @Autowired
    AnalyticsService analyticsService;
    @GetMapping("inflation")
    public List<InflationRateHelper> getInflationRates(){
        return analyticsService.getInflationRates();
    }

}
