package op29sem58.student.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;

@Entity(name = "Student")
public class Student {

    @Id
    private String netID;

    private LocalDateTime last_visited;
    private boolean wantsToGo;

}
