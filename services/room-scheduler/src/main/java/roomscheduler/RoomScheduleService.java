package roomscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;
import javax.annotation.PostConstruct;

@SpringBootApplication
public class RoomScheduleService {

    public static void main(String[] args) {
        SpringApplication.run(RoomScheduleService.class, args);
    }

    @PostConstruct
    void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
