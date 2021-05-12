package com.softserveinc.ita.homeproject.api.tests.query;

public abstract class BaseQuery {
    private Integer pageNumber;

    private Integer pageSize;

    private String sort;

    private String filter;

    private Long id;

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    protected static abstract class BaseBuilder<T extends BaseQuery, B extends BaseBuilder> {
        protected T queryClass;

        protected B queryBuilder;

        protected BaseBuilder() {
            queryClass = getActual();
            queryBuilder = getActualBuilder();
        }

        protected abstract T getActual();

        protected abstract B getActualBuilder();

        public B pageNumber(Integer pageNumber) {
            queryClass.setPageNumber(pageNumber);
            return queryBuilder;
        }

        public B pageSize(Integer setPageSize) {
            queryClass.setPageSize(setPageSize);
            return queryBuilder;
        }

        public B sort(String sort) {
            queryClass.setSort(sort);
            return queryBuilder;
        }

        public B filter(String filter) {
            queryClass.setFilter(filter);
            return queryBuilder;
        }

        public B id(Long id) {
            queryClass.setId(id);
            return queryBuilder;
        }

        public T build() {
            return queryClass;
        }
    }
}
