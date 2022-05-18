package com.p2eanalytics.takehome.deprecated.repository;

import com.p2eanalytics.takehome.helper.RateInfoHelper;
import com.p2eanalytics.takehome.deprecated.model.Transfer;

import java.sql.Date;

public interface TransferRepositoryCustom {

    public RateInfoHelper getRateInfoByDateAndEvent(java.sql.Date from, Date to, Transfer.ContractTokenEvent eventType);
}
