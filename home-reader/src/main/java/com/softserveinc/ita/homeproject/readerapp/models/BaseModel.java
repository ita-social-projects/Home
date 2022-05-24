package com.softserveinc.ita.homeproject.readerapp.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public abstract class BaseModel {
    @Id
    private Long id;
}
