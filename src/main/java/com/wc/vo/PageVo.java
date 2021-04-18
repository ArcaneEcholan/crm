package com.wc.vo;


import java.util.List;

public class PageVo<T> {
   private int totalCount;
   private int totalPages;
   private List<T> list;

    public PageVo() {
    }
    public PageVo(int totalCount, int totalPages, List<T> list) {
        this.totalCount = totalCount;
        this.totalPages = totalPages;
        this.list = list;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
