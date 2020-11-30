package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UpdateNewsDto {

    private String title;

    private String photoUrl;

    private String description;

    private String source;

    private String text;

    private LocalDateTime updateTime;
}
