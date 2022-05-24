package com.softserveinc.ita.homeproject.readerapp.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

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
