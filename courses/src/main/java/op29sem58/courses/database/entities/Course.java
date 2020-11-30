package op29sem58.courses.database.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @ManyToOne(targetEntity = Lecture.class)
    private int id;

    private String name;
    private String code;
    private int yearOfStudy;

    public Course(String name, String code, int yearOfStudy){
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
