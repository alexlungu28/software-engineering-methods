package project.op29sem58.courses.database.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String teacherNetId;
    private String name;
    private String code;
    private int yearOfStudy;

    public Course(){}

    /**
     * Constructor for the course entity.
     *
     * @param teacherNetId the netId of the teacher of this course
     * @param name the name of the course
     * @param code the code of the course
     * @param yearOfStudy the study year in which this course is held
     */
    public Course(String teacherNetId, String name, String code, int yearOfStudy) {
        this.teacherNetId = teacherNetId;
        this.name = name;
        this.code = code;
        this.yearOfStudy = yearOfStudy;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTeacherNetId() {
        return teacherNetId;
    }

    public void setTeacherNetId(String teacherNetId) {
        this.teacherNetId = teacherNetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }
}
