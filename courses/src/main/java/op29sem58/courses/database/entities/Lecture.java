package op29sem58.courses.database.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @OneToMany
    private Set<Integer> coursesId;

    @OneToMany
    private Set<String> coursesHasTeachersNetId;

    private LocalDate date;
    private int nSlots;

    public Lecture(String name, Set<Integer> coursesId, Set<String> coursesHasTeachersNetId, LocalDate date, int nSlots){
        this.name = name;
        this.coursesId = coursesId;
        this.coursesHasTeachersNetId = coursesHasTeachersNetId;
        this.date = date;
        this.nSlots = nSlots;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Integer> getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(Set<Integer> coursesId) {
        this.coursesId = coursesId;
    }

    public Set<String> getCoursesHasTeachersNetId() {
        return coursesHasTeachersNetId;
    }

    public void setCoursesHasTeachersNetId(Set<String> coursesHasTeachersNetId) {
        this.coursesHasTeachersNetId = coursesHasTeachersNetId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getnSlots() {
        return nSlots;
    }

    public void setnSlots(int nSlots) {
        this.nSlots = nSlots;
    }
}
