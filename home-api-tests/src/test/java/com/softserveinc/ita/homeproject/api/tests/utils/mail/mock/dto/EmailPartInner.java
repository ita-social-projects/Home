package com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(value = {"Headers","Size","MIME"})
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class EmailPartInner {
    String body;
}
