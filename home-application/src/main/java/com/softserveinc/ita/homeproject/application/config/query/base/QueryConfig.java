package com.softserveinc.ita.homeproject.application.config.query.base;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;

public interface QueryConfig<T extends BaseEntity> {

    Class<T> getEntityClass();

    List<QueryParamEnum> getWhiteListEnums();
}
