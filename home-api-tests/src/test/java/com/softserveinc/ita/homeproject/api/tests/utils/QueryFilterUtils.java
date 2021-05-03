package com.softserveinc.ita.homeproject.api.tests.utils;

import java.util.StringJoiner;

import com.softserveinc.ita.homeproject.ApiException;

public final class QueryFilterUtils {

    public static String getBetweenPredicate(String selector, String arg1, String arg2) {
        StringJoiner joiner = new StringJoiner(", ", "(", ")");
        joiner.add(arg1);
        joiner.add(arg2);
        return selector
            .concat(FilterPredicateEnum.BETWEEN.getPredicate())
            .concat(joiner.toString());
    }

    public static String getIgnoreCaseLikePredicate(String selector, String arg) {
        return selector
            .concat(FilterPredicateEnum.IGNORE_CASE_LIKE.getPredicate())
            .concat(arg);
    }

}
