package readerapp.readerrepository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import readerapp.readermodels.UserForMongo;

@Repository
public interface UserReaderRepository extends MongoRepository<UserForMongo, String> {
}
