package com.fiveguys.RIA.RIA_Backend.dashboard.model.component;

import java.util.List;
import lombok.Getter;

@Getter
public class TargetMonthContext {

  private final int year;
  private final int month;
  private final int ym;
  private final boolean hasPrev;
  private final boolean hasNext;
  private final boolean empty;

  private TargetMonthContext(
      int year,
      int month,
      int ym,
      boolean hasPrev,
      boolean hasNext,
      boolean empty
  ) {
    this.year = year;
    this.month = month;
    this.ym = ym;
    this.hasPrev = hasPrev;
    this.hasNext = hasNext;
    this.empty = empty;
  }

  public static TargetMonthContext empty() {
    return new TargetMonthContext(0, 0, 0, false, false, true);
  }

  public static TargetMonthContext of(
      int year,
      int month,
      int ym,
      boolean hasPrev,
      boolean hasNext
  ) {
    return new TargetMonthContext(year, month, ym, hasPrev, hasNext, false);
  }
}
