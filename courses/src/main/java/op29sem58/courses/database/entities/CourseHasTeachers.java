package op29sem58.courses.database.entities;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class CourseHasTeachers{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToMany
    private Set<Course> coursesId;

    @ManyToOne(targetEntity = Lecture.class)
    private String userNetId;
    private String comments;

    public CourseHasTeachers(Set<Course> coursesId, String userNetId, String comments){
        this.coursesId = coursesId;
        this.userNetId = userNetId;
        this.comments = comments;
    }

    public Set<Course> getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(Set<Course> coursesId) {
        this.coursesId = coursesId;
    }

    public String getUserNetId() {
        return userNetId;
    }

    public void setUserNetId(String userNetId) {
        this.userNetId = userNetId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

/*
class PK {
    private List<Integer> coursesId;
    private String userNetId;

    public PK(List<Integer> coursesId, String userNetId){
        this.coursesId = coursesId;
        this.userNetId = userNetId;
    }

    public List<Integer> getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(List<Integer> coursesId) {
        this.coursesId = coursesId;
    }

    public String getUserNetId() {
        return userNetId;
    }

    public void setUserNetId(String userNetId) {
        this.userNetId = userNetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PK pk = (PK) o;
        return coursesId.equals(pk.coursesId) &&
                userNetId.equals(pk.userNetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coursesId, userNetId);
    }
}*/
