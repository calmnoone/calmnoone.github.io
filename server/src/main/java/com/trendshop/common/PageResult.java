package com.trendshop.common;

import java.util.List;

public class PageResult<T> {
    private List<T> list;
    private Pagination pagination;

    public PageResult() {
    }

    public PageResult(List<T> list, Pagination pagination) {
        this.list = list;
        this.pagination = pagination;
    }

    public List<T> getList() { return list; }
    public void setList(List<T> list) { this.list = list; }

    public Pagination getPagination() { return pagination; }
    public void setPagination(Pagination pagination) { this.pagination = pagination; }

    public static class Pagination {
        private long page;
        private long pageSize;
        private long total;
        private long totalPages;

        public Pagination() {
        }

        public Pagination(long page, long pageSize, long total, long totalPages) {
            this.page = page;
            this.pageSize = pageSize;
            this.total = total;
            this.totalPages = totalPages;
        }

        public long getPage() { return page; }
        public void setPage(long page) { this.page = page; }

        public long getPageSize() { return pageSize; }
        public void setPageSize(long pageSize) { this.pageSize = pageSize; }

        public long getTotal() { return total; }
        public void setTotal(long total) { this.total = total; }

        public long getTotalPages() { return totalPages; }
        public void setTotalPages(long totalPages) { this.totalPages = totalPages; }
    }

    public static <T> PageResult<T> of(List<T> list, long page, long pageSize, long total) {
        return new PageResult<>(list, new Pagination(page, pageSize, total,
                total % pageSize == 0 ? total / pageSize : total / pageSize + 1));
    }
}
