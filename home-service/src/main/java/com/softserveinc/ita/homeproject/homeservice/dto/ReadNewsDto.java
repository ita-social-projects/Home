package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ReadNewsDto {

    private Long id;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String title;

    private String photoUrl;

    private String description;

    private String source;

    private String text;

}
