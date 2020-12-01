package op29sem58.courses.database.entities;

import java.time.LocalDate;
import javax.persistence.*;


@Entity
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Course course;
    private LocalDate date;
    private int nslots;
    private int minNoStudents;

    public Lecture(){}

    /**
     * Constructor for the lecture entity.
     *
     * @param course the course of which this lecture is
     * @param date the preferred date at which this lecture should be held
     * @param nslots the number of slots this lecture takes
     * @param minNumberOfStudents the minimum number of students that should attend this lecture
     */
    public Lecture(Course course, LocalDate date, int nslots, int minNumberOfStudents) {
        this.course = course;
        this.date = date;
        this.nslots = nslots;
        this.minNoStudents = minNumberOfStudents;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getNslots() {
        return nslots;
    }

    public void setNslots(int nslots) {
        this.nslots = nslots;
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
