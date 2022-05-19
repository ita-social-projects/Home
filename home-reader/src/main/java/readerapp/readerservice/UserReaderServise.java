package readerapp.readerservice;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import readerapp.readermodels.UserForMongo;
import readerapp.readerrepository.UserReaderRepository;


@Service
public class UserReaderServise {
    @Autowired
    UserReaderRepository userReaderRepository;

    private static List<UserForMongo> list = new ArrayList<>();

    static {
        list.add(new UserForMongo("SomeId1", 100, "SomeName1", "SomeSurname1"));
        list.add(new UserForMongo("SomeId2", 100, "SomeName2", "SomeSurname2"));
        list.add(new UserForMongo("SomeId3", 100, "SomeName3", "SomeSurname3"));
        list.add(new UserForMongo("SomeId4", 100, "SomeName4", "SomeSurname4"));

    }

    @PostConstruct
    public void init() {
        userReaderRepository.saveAll(list);
    }

    public List<UserForMongo> findAll() {
        return userReaderRepository.findAll();
    }

//    public UserForMongo findById() {
//      return   userReaderRepository.findById();
//    }
}
