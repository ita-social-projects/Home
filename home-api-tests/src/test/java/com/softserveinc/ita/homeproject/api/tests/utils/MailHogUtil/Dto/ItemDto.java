package com.softserveinc.ita.homeproject.api.tests.utils.MailHogUtil.Dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(value = { "MIME","Body","Raw"})
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class ItemDto {

    @JsonProperty("ID")
    String id;

    FromDto from;

    List<FromDto> to;

    ContentDto content;

    String created;
}