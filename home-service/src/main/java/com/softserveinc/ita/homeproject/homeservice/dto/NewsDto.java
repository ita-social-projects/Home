package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDto extends BaseDto {

    private Long id;

    private String title;

    private String photoUrl;

    private String description;

    private String source;

    private String text;
}
