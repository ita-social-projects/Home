package com.softserveinc.ita.homeproject.readerapp.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class UserReader extends BaseModel{

    private String firstName;

    private String middleName;

    private String lastName;

    //    private String email;
    //
    //    private Boolean expired;
    //
    //    private Boolean locked;
    //
    //    private Boolean credentialsExpired;
    //
    //    private Boolean enabled;
    //
    //    private String password;
    //
    //    private LocalDateTime createDate;
    //
    //    private LocalDateTime updateDate;
}
