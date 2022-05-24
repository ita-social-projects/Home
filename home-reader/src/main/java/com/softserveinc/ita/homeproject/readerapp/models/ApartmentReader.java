package com.softserveinc.ita.homeproject.readerapp.models;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class ApartmentReader {

    @Id
    private String apartmentNumber;

    private Boolean enabled;

    public ApartmentReader(String apartmentNumber, Boolean enabled) {
        this.apartmentNumber = apartmentNumber;
        this.enabled = enabled;
    }

    public ApartmentReader() {
    }

}
