package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class CreateOrUpdateNewsDto extends BaseNewsDto {

    private LocalDateTime createOrUpdateTime;
}
