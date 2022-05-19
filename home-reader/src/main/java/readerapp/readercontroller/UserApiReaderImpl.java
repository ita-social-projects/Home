package readerapp.readercontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import readerapp.readermodels.UserForMongo;
import readerapp.readerrepository.UserReaderRepository;


@RestController
public class UserApiReaderImpl {

    //    private UserReaderServise userReaderServise;
    @Autowired
    private UserReaderRepository userReaderRepository;

    @GetMapping("/users")
    public List<UserForMongo> getUsers() {
        return userReaderRepository.findAll();
    }


    @PostMapping("/users")
    public void saveUsers(@RequestBody UserForMongo userForMongo) {
        userReaderRepository.save(userForMongo);
    }
}
