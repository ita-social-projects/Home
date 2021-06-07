package com.softserveinc.ita.homeproject.api.tests.utils.MailHogUtil.Dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class FromDto {
    String relays;
    String mailbox;
    String domain;
    String params;
}
