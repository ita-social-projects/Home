package com.softserveinc.ita.homeproject.api.tests.utils;

import io.github.perplexhub.rsql.RSQLOperators;

import java.util.StringJoiner;

public class QueryFilterUtil {

    public static String between(String selector, String arg1, String arg2) {
        StringJoiner joiner = new StringJoiner(", ", "(", ")");
        joiner.add(arg1);
        joiner.add(arg2);
        return selector
                .concat(RSQLOperators.BETWEEN.toString())
                .concat(joiner.toString());
    }

    public static String ignoreCaseLike(String selector, String arg) {
        return selector
                .concat(RSQLOperators.IGNORE_CASE_LIKE.toString())
                .concat(arg);
    }
}
