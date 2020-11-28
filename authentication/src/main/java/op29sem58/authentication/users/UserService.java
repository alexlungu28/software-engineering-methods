package op29sem58.authentication.users;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

/*    *//**
     * Register user method.
     * @param usr input username
     * @return result of operation
     *//*
    public String register(User usr) {
        //check database values with the supplied values
        //return true if netID and password matches an entry
        Optional<User> tempUser = userRepository.findByNetID(usr.getNetID());
        if (!tempUser.isEmpty()) {
            System.out.println("This NetID already exists");
            return "This NetID already exists";
        } else {
            usr.setPassword("" + usr.getPassword().hashCode());
            userRepository.save(usr);
            System.out.println("successfully added");
            return "successfully added";
        }
    }*/

    /**
     * Get all users method.
     * @return returns all users from database
     */
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get user information by netID.
     * @param netID - netID
     * @return the user from database
     */
    public Optional<User> getUser(String netID) {
        Optional<User> optionalUser = userRepository.findById(netID);
        User user = optionalUser.get();
        user.setPassword("");
        return Optional.of(user);
    }

    /**
     * This method updates the user.
     * @param user the user with all fields, including role
     * @return result of operation
     */
    public String modifyUser(User user) {
        if (!userRepository.existsById(user.getNetID())) {
            return "User does not exist";
        }
        User temp = userRepository.findById(user.getNetID())
                .orElse(null);
        temp.setRole(user.getRole());
        userRepository.save(temp);
        return "User Updated";
    }
}
