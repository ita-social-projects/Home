package com.softserveinc.ita.homeproject.api.tests.utils;

import com.softserveinc.ita.homeproject.client.model.Address;

public class CreateSampleUtil {
    public static Address createAddress() {
        return new Address().city("Dnepr")
                .district("District")
                .houseBlock("block")
                .houseNumber("number")
                .region("Dnipro")
                .street("street")
                .zipCode("zipCode");
    }
}
