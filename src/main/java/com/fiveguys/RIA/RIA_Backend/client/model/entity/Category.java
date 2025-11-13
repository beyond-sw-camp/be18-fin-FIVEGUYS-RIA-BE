package com.fiveguys.RIA.RIA_Backend.client.model.entity;

public enum Category {
  FASHION("패션"),
  BEAUTY("뷰티"),
  FOOD("식음료"),
  LIFESTYLE("리빙/라이프스타일"),
  ELECTRONICS("가전/디지털"),
  ACCESSORY("잡화/액세서리"),
  SPORTS("스포츠/아웃도어"),
  SERVICE("서비스/기타");

  private final String description;

  Category(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
