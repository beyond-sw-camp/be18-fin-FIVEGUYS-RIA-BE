package com.fiveguys.RIA.RIA_Backend.vip.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.vip.model.component.VipLoader;
import com.fiveguys.RIA.RIA_Backend.vip.model.component.VipMapper;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip.VipGrade;
import com.fiveguys.RIA.RIA_Backend.vip.model.service.VipService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VipServiceImpl implements VipService {

  private final VipLoader  vipLoader;
  private final VipMapper  vipMapper;

  @Override
  public VipListPageResponseDto getVipList(int page, int size, VipGrade grade) {

    Page<Vip> result = vipLoader.loadVipPage(grade, page, size);

    return vipMapper.toListPageDto(result, page, size);
  }

  @Override
  public VipStatsResponseDto getStats() {

    long total = vipLoader.countAll();

    long psrBlack = vipLoader.count(VipGrade.PSR_BLACK);
    long psrWhite = vipLoader.count(VipGrade.PSR_WHITE);

    long parkJadeBlack = vipLoader.count(VipGrade.PARK_JADE_BLACK);
    long parkJadeWhite = vipLoader.count(VipGrade.PARK_JADE_WHITE);
    long parkJadeBlue = vipLoader.count(VipGrade.PARK_JADE_BLUE);

    long jadePlus = vipLoader.count(VipGrade.JADE_PLUS);
    long jade = vipLoader.count(VipGrade.JADE);

    return vipMapper.toStatsDto(
        total,
        psrBlack,
        psrWhite,
        parkJadeBlack,
        parkJadeWhite,
        parkJadeBlue,
        jadePlus,
        jade
    );
  }
}
