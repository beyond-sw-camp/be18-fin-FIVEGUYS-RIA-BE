package com.fiveguys.RIA.RIA_Backend.admin.model.dto.respones;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class PageResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    public static <T> PageResponse<T> of(Page<T> page) {
        return PageResponse.<T>builder()
                           .content(page.getContent())
                           .page(page.getNumber())
                           .size(page.getSize())
                           .totalElements(page.getTotalElements())
                           .totalPages(page.getTotalPages())
                           .first(page.isFirst())
                           .last(page.isLast())
                           .build();
    }
}
