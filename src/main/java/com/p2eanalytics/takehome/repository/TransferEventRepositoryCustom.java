package com.p2eanalytics.takehome.repository;

import com.p2eanalytics.takehome.helper.RateInfoHelper;
import com.p2eanalytics.takehome.model.TransferEvent.ContractTokenEvent;

import java.util.List;

public interface TransferEventRepositoryCustom {

    public List<RateInfoHelper> getRateInfoByEventType(ContractTokenEvent eventType);
}
