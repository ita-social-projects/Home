package com.softserveinc.ita.homeproject.readerApp.repositories;

import com.softserveinc.ita.homeproject.readerApp.models.UserForMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserReaderRepository extends MongoRepository<UserForMongo, String> {
}
