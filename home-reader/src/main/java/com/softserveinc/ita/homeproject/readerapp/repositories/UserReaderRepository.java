package com.softserveinc.ita.homeproject.readerapp.repositories;

import com.softserveinc.ita.homeproject.readerapp.models.ReaderUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReaderRepository extends MongoRepository<ReaderUser, String> {
}
