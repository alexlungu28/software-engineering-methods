package op29sem58.courses.database.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToMany
    private Set<Course> courses;
    private LocalDate date;
    private int nSlots;

    public Lecture(){}

    public Lecture(Set<Course> courses, LocalDate date, int nSlots){
        this.courses = courses;
        this.date = date;
        this.nSlots = nSlots;
    }

    public int getId(){
        return id;
    }

    public Set<Course> getCoursesId() {
        return courses;
    }

    public void setCoursesId(Set<Course> coursesId) {
        this.courses = coursesId;
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
