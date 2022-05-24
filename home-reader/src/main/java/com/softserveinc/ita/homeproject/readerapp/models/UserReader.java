package com.softserveinc.ita.homeproject.readerapp.models;

import lombok.*;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class UserReader extends BaseModel{

    private String firstName;

    private String middleName;

    private String lastName;

    private String email;

    private Boolean expired;

    private Boolean locked;

    private Boolean credentialsExpired;

    private Boolean enabled;

    private String password;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;


}
