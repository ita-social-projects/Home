package com.softserveinc.ita.homeproject.readerapp.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ApartmentReader {

    @Id
    private String apartmentNumber;

    private Boolean enabled;
}
