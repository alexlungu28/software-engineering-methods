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
    private UserService userService;

    @GetMapping("/users")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/user/{netID}")
    public Optional<User> getUser(@PathVariable String netID) {
        return userService.getUser(netID);
    }

    @PutMapping("/users/update")
    public String updateUser(@RequestBody User user) {
        return userService.modifyUser(user);
    }

/*    @PostMapping("/register")
    public String register(@RequestBody User usr) {
        System.out.println(usr.toString());
        return userService.register(usr);
    }*/
}
