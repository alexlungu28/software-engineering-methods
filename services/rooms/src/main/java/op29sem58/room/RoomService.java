package op29sem58.room;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;
import javax.annotation.PostConstruct;

@SpringBootApplication
public class RoomService {

    public static void main(String[] args) {
        SpringApplication.run(RoomService.class, args);
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
