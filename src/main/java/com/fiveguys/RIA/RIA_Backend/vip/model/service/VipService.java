package com.fiveguys.RIA.RIA_Backend.vip.model.service;

import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;

public interface VipService {
  VipListPageResponseDto getVipList(int page, int size, Vip.VipGrade grade, String keyword);

  VipStatsResponseDto getStats();

}