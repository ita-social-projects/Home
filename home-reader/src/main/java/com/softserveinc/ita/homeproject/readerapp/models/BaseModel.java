package com.softserveinc.ita.homeproject.readerapp.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public abstract class BaseModel implements Serializable {
    @Id
    private Long id;
}
