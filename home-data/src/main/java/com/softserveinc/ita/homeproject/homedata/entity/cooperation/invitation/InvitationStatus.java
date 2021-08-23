package com.softserveinc.ita.homeproject.homedata.entity.cooperation.invitation;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum InvitationStatus {

    PENDING("pending"),

    PROCESSING("processing"),

    ACCEPTED("accepted"),

    DECLINED("declined"),

    DEACTIVATED("deactivated"),

    ERROR("error");


    private final String value;

    private static final Map<String, InvitationStatus> STATUSES = Stream.of(InvitationStatus.values())
            .collect(Collectors.toMap(InvitationStatus::getValue, Function.identity()));

    InvitationStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public String getValue() {
        return value;
    }

    public static InvitationStatus getEnum(String value) {
        return STATUSES.get(value);
    }

}
