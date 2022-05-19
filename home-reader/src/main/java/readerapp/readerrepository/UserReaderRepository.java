package readerapp.readerrepository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import readerapp.readermodels.UserForMongo;

import java.util.Optional;

@Repository
public interface UserReaderRepository extends MongoRepository<UserForMongo, String> {

//    public List<UserForMongo> findUser() {
//        return mongoTemplate.findAll(UserForMongo.class);
//    }
    Optional<UserForMongo> findById(String Id);
}
