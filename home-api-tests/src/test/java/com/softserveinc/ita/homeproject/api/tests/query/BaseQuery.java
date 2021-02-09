package com.softserveinc.ita.homeproject.api.tests.query;

public abstract class BaseQuery {
    private Integer pageNumber;
    private Integer pageSize;
    private String sort;
    private String filter;
    private String id;

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public String getSort() {
        return sort;
    }

    public String getFilter() {
        return filter;
    }

    public String getId() {
        return id;
    }

    protected static abstract class BaseBuilder<T extends BaseQuery, B extends BaseBuilder> {
        protected T queryClass;
        protected B queryBuilder;

        protected abstract T getActual();

        protected abstract B getActualBuilder();

        protected BaseBuilder(int pageNumber, Integer pageSize) {
            queryClass = getActual();
            queryBuilder = getActualBuilder();
            queryClass.setPageNumber(pageNumber);
            queryClass.setPageSize(pageSize);
        }

        public B sort(String sort) {
            queryClass.setSort(sort);
            return queryBuilder;
        }

        public B filter(String filter) {
            queryClass.setFilter(filter);
            return queryBuilder;
        }

        public B id(String id) {
            queryClass.setId(id);
            return queryBuilder;
        }

        public T build() {
            return queryClass;
        }
    }
}
