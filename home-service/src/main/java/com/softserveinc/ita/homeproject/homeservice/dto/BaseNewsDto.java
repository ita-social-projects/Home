package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class BaseNewsDto {

    private String title;

    private String photoUrl;

    private String description;

    private String source;

    private String text;

}
