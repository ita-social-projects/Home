package com.softserveinc.ita.homeproject.api.tests.utils;

public enum FilterPredicateEnum implements FilterPredicate {

    BETWEEN("=bt="),
    IGNORE_CASE_LIKE("=ilike=");

    private final String parametr;

    FilterPredicateEnum(String parametr) {
        this.parametr = parametr;
    }

    @Override
    public String getPredicate() {
        return this.parametr;
    }
}
