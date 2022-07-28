package com.softserveinc.ita.homeproject.homedata.user.password.enums;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Getter;


@Getter
public enum PasswordRecoveryTokenStatus {

    PENDING("pending"),

    ACTIVE("active"),

    EXPIRED("expired");

    private final String value;

    private static final Map<String, PasswordRecoveryTokenStatus> STATUSES =
        Stream.of(PasswordRecoveryTokenStatus.values())
            .collect(Collectors.toMap(PasswordRecoveryTokenStatus::getValue, Function.identity()));

    PasswordRecoveryTokenStatus(String value) {
        this.value = value;
    }

    public static PasswordRecoveryTokenStatus getEnum(String value) {
        return STATUSES.get(value);
    }
}
