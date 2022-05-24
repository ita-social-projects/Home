package com.softserveinc.ita.homeproject.readerapp.repositories;

import com.softserveinc.ita.homeproject.readerapp.models.ApartmentReader;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApartmentReaderRepository extends MongoRepository<ApartmentReader, String> {
}
