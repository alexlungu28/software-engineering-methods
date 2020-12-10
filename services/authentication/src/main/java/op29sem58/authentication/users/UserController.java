package op29sem58.authentication.users;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class UserController {

    @Autowired
    private transient UserService userService;

    @GetMapping("/users")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{netid}")
    public Optional<User> getUser(@PathVariable String netid) {
        return userService.getUser(netid);
    }

    @PutMapping("/users/update")
    public String updateUser(@RequestBody User user) {
        return userService.modifyUser(user);
    }
}
