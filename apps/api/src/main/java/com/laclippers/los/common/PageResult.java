package com.laclippers.los.common;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResult<T> {
    private List<T> list;
    private long total;
    private int page;
    private int size;
    private int totalPages;

    public static <T> PageResult<T> of(Page<T> p) {
        PageResult<T> r = new PageResult<>();
        r.setList(p.getContent());
        r.setTotal(p.getTotalElements());
        r.setPage(p.getNumber() + 1);
        r.setSize(p.getSize());
        r.setTotalPages(p.getTotalPages());
        return r;
    }
}