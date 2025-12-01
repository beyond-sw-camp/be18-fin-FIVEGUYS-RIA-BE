package com.fiveguys.RIA.RIA_Backend.campaign.revenue.model.service;

import java.time.LocalDate;

public interface SalesAggregationService {


/*
  targetDate 하루치 POS 기준으로
  SALES_DAILY / STORE_SALES_STATS 집계
  */
  void aggregateDaily(LocalDate targetDate);
}