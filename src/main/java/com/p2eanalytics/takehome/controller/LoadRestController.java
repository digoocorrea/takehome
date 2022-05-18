package com.p2eanalytics.takehome.controller;

import com.p2eanalytics.takehome.service.LoadBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;


@RestController
@RequestMapping("/load")
public class LoadRestController {
    @Autowired
    LoadBaseService loadService;

    @GetMapping("transfers/{dateFrom}/{page}")
    public void loadTransferBase(@PathVariable("date") Date dateFrom, @PathVariable("page") Integer page ){
        loadService.loadBase(new java.util.Date(dateFrom.getTime()), page);
    }

    @GetMapping("mint")
    public void loadMint(){
        loadService.loadMintRates();
    }

    @GetMapping("burn")
    public void loadBurn(){
        loadService.loadBurnRates();
    }
}
