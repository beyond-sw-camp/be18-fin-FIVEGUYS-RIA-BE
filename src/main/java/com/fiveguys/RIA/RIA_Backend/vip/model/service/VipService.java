package com.fiveguys.RIA.RIA_Backend.vip.model.service;

import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipBrandTop5ResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipGradeStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipSalesTrendResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStoreSalesPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;

public interface VipService {
  VipGradeStatsResponseDto getStats();

  VipListPageResponseDto getVipList(int page, int size, Vip.VipGrade grade, String keyword);

  VipStatsResponseDto getVipSalesStats(Integer year, Integer month);

  VipBrandTop5ResponseDto getVipBrandTop5(Integer year, Integer month);

  VipSalesTrendResponseDto getVipSalesTrend();

  VipStoreSalesPageResponseDto getVipStoreSalesPage(
      Integer year,
      Integer month,
      int page,
      int size
  );
}
