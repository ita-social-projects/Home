package com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class EmailHeader {

    @JsonProperty("Content-Type")
    List<String> contentType;

    List <String> date;

    List <String> from;

    @JsonProperty("MIME-Version")
    List <String> mimeVersion;

    @JsonProperty("Message-ID")
    List <String> messageId;

    List <String> received;

    @JsonProperty("Return-Path")
    List <String> returnPath;

    List <String> subject;

    List <String> to;
}
