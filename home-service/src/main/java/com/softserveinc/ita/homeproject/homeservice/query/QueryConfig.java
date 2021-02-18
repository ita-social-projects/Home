package com.softserveinc.ita.homeproject.homeservice.query;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;

public interface QueryConfig<T extends BaseEntity> {

    Class<T> getEntityClass();
    List<QueryParamEnum> getWhiteListEnums();
}
