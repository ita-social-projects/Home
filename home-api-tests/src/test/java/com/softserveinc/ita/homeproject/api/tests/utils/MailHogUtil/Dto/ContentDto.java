package com.softserveinc.ita.homeproject.api.tests.utils.MailHogUtil.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class ContentDto {
    HeadersDto headers;
    String body;
    String size;
    @JsonProperty("MIME")
    String mime;
}
