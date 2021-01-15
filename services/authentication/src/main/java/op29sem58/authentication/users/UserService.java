package op29sem58.authentication.users;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private transient UserRepository userRepository;

    /**
     * Get all users method.
     *
     * @return returns all users from database
     */
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get user information by netID.
     *
     * @param netid - netID
     *
     * @return the user from database
     */
    public Optional<User> getUser(String netid) {
        Optional<User> optionalUser = userRepository.findById(netid);
        User user = optionalUser.get();
        user.setPassword("");
        return Optional.of(user);
    }

    private void saveNewUser(User user, User temp) {
        temp.setRole(user.getRole());
        userRepository.save(temp);
    }

    /**
     * This method updates the user.
     *
     * @param user the user with all fields, including role
     *
     * @return result of operation
     */
    public String modifyUser(User user) {
        String netid = user.getNetid();
        if (!userRepository.existsById(netid)) {
            return "User does not exist";
        }
        User temp = userRepository.findById(netid)
                .orElse(null);
        saveNewUser(user, temp);
        return "User Updated";
    }
}
