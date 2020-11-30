package op29sem58.courses.database.entities;

import org.springframework.jmx.export.annotation.ManagedNotification;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private String teacherNetId;
    private String name;
    private String code;
    private int yearOfStudy;

    public Course(){}

    public Course(String teacherNetId, String name, String code, int yearOfStudy){
        this.teacherNetId = teacherNetId;
        this.name = name;
        this.code = code;
        this.yearOfStudy = yearOfStudy;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }
}
