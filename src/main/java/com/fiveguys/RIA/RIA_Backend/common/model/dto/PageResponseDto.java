package com.fiveguys.RIA.RIA_Backend.common.model.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PageResponseDto<T> {
  private int page;
  private int size;
  private long totalCount;
  private List<T> data;
}