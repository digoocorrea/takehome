package com.p2eanalytics.takehome.deprecated.controller;

import com.p2eanalytics.takehome.deprecated.service.BlockchainSyncService;
import com.p2eanalytics.takehome.model.MintRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sync")
public class SyncRestController {
    @Autowired
    BlockchainSyncService syncService;

    @GetMapping("sync")
    public void sync(){
        syncService.syncTransfers(null);
    }

    @GetMapping("updateMintRate")
    public MintRate updateMintRates(){
        return syncService.updateMintRates();
    }
}
