package com.softserveinc.ita.homeproject.readerapp.models;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

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
