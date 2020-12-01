package op29sem58.courses.database.entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Course course;
    private LocalDate date;
    private int nSlots;
    private int minNoStudents;

    public Lecture(){}

    public Lecture(Course course, LocalDate date, int nSlots, int minNumberOfStudents){
        this.course = course;
        this.date = date;
        this.nSlots = nSlots;
        this.minNoStudents = minNumberOfStudents;
    }

    public int getId(){
        return id;
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

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getMinNoStudents() {
        return minNoStudents;
    }

    public void setMinNoStudents(int minNumberOfStudents) {
        this.minNoStudents = minNumberOfStudents;
    }
}
