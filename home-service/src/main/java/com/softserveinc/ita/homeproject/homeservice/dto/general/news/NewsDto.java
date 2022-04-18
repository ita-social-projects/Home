package com.softserveinc.ita.homeproject.homeservice.dto.general.news;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NewsDto extends BaseDto {

    private String title;

    private String photoUrl;

    private String description;

    private String source;

    private String text;
}
