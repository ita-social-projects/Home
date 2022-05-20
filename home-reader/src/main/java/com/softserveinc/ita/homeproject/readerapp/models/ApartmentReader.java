package com.softserveinc.ita.homeproject.readerapp.models;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.SequenceGenerator;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Document
@SequenceGenerator(name = "sequence", sequenceName = "apartments_sequence")
public class ApartmentReader {
    private String apartmentNumber;

    private BigDecimal apartmentArea;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private Boolean enabled;
}
