package com.fiveguys.RIA.RIA_Backend.vip.model.component;

import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip;
import com.fiveguys.RIA.RIA_Backend.vip.model.entity.Vip.VipGrade;
import com.fiveguys.RIA.RIA_Backend.vip.model.repository.VipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class VipLoader {

  private final VipRepository vipRepository;

  public Page<Vip> loadVipPage(VipGrade grade, String keyword, int page, int size) {

    int index = (page <= 0) ? 0 : page - 1;

    Pageable pageable = PageRequest.of(
        index,
        size,
        Sort.by("createdAt").descending()
    );

    String normalizedKeyword =
        (keyword == null || keyword.isBlank()) ? null : keyword;

    // 등급 + 키워드(이름, 연락처) 조건을 한 번에 처리
    return vipRepository.searchVip(grade, normalizedKeyword, pageable);
  }


  @Transactional(readOnly = true)
  public long countAll() {
    return vipRepository.count();
  }

  @Transactional(readOnly = true)
  public long count(VipGrade grade) {
    return vipRepository.countByGrade(grade);
  }
}