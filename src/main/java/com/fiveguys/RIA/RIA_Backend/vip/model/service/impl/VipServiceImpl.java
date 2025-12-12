package com.fiveguys.RIA.RIA_Backend.vip.model.service.impl;

import com.fiveguys.RIA.RIA_Backend.pos.model.repository.PosRepository;
import com.fiveguys.RIA.RIA_Backend.vip.model.component.VipBrandTop5Loader;
import com.fiveguys.RIA.RIA_Backend.vip.model.component.VipLoader;
import com.fiveguys.RIA.RIA_Backend.vip.model.component.VipMapper;
import com.fiveguys.RIA.RIA_Backend.vip.model.component.VipSalesStatsLoader;
import com.fiveguys.RIA.RIA_Backend.vip.model.component.VipSalesTrendLoader;
import com.fiveguys.RIA.RIA_Backend.vip.model.component.VipStoreSalesPageLoader;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipBrandTop5ResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipListPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipListResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipGradeStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipSalesTrendResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStatsResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.dto.response.VipStoreSalesPageResponseDto;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip.VipGrade;
import com.fiveguys.RIA.RIA_Backend.vip.model.repository.VipRepository;
import com.fiveguys.RIA.RIA_Backend.vip.model.service.VipService;
import java.time.YearMonth;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VipServiceImpl implements VipService {

  private final VipLoader  vipLoader;
  private final VipMapper  vipMapper;
  private final PosRepository posRepository;
  private final VipRepository vipRepository;
  private final VipSalesStatsLoader  vipSalesStatsLoader;
  private final VipBrandTop5Loader vipBrandTop5Loader;
  private final VipSalesTrendLoader  vipSalesTrendLoader;
  private final VipStoreSalesPageLoader vipStoreSalesPageLoader;

  @Override
  public VipListPageResponseDto getVipList(int page, int size, VipGrade grade, String keyword) {

    Pageable pageable = PageRequest.of(page - 1, size);

    String keywordParam =
            (keyword == null || keyword.isBlank()) ? null : keyword;

    Page<Vip> result = vipRepository.searchVip(grade, keywordParam, pageable);

    List<VipListResponseDto> content = result.getContent().stream()
                                             .map(vip -> {
                                               Long totalSales = posRepository.getTotalSalesByCustomerId(vip.getCustomerId());
                                               return vipMapper.toListDto(vip, totalSales);
                                             })
                                             .toList();

    return vipMapper.toListPageDto(result, page, size, content);
  }

  @Override
  public VipGradeStatsResponseDto getStats() {
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

  @Override
  public VipStatsResponseDto getVipSalesStats(Integer year, Integer month) {
    return vipSalesStatsLoader.load(year, month);
  }

  @Override
  public VipBrandTop5ResponseDto getVipBrandTop5(Integer year, Integer month) {
    return vipBrandTop5Loader.load(year, month);
  }

  @Override
  public VipSalesTrendResponseDto getVipSalesTrend() {
    return vipSalesTrendLoader.loadLastSixMonths();
  }

  @Override
  public VipStoreSalesPageResponseDto getVipStoreSalesPage(
      Integer year,
      Integer month,
      int page,
      int size
  ) {
    return vipStoreSalesPageLoader.load(year, month, page, size);
  }
}
