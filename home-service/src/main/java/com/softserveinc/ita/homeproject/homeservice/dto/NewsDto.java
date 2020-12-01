package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NewsDto {

    private Long id;

    private String title;

    private String photoUrl;

    private String description;

    private String source;

    private String text;
}
