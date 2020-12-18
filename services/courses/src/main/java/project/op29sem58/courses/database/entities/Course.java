package project.op29sem58.courses.database.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String teacherNetId;
    private String name;
    private String code;
    private int yearOfStudy;

    @SuppressWarnings("PMD") //DU anomaly
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Lecture> lectures;

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
        this.lectures = new ArrayList<>();
    }

    public void addLectures(Lecture lecture) {
        this.lectures.add(lecture);
        lecture.setCourse(this);
    }

    public void removeLecture(Lecture lecture) {
        this.lectures.remove(lecture);
        lecture.setCourse(null);
    }

    public List<Lecture> getLectures() {
        return lectures;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Course course = (Course) o;
        return Objects.equals(teacherNetId, course.teacherNetId)
                && Objects.equals(id, course.id)
                && Objects.equals(name, course.name)
                && Objects.equals(code, course.code)
                && Objects.equals(yearOfStudy, course.yearOfStudy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teacherNetId, name, code, yearOfStudy);
    }
}
