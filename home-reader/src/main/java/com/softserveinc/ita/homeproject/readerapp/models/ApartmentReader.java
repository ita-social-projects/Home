package com.softserveinc.ita.homeproject.readerapp.models;


import javax.persistence.SequenceGenerator;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
@SequenceGenerator(name = "sequence", sequenceName = "apartments_sequence")
public class ApartmentReader {

    @Id
    private String apartmentNumber;

    private Boolean enabled;
}
