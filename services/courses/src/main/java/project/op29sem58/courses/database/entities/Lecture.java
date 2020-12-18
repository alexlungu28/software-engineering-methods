package project.op29sem58.courses.database.entities;

import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;



@Entity
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Course course;
    private LocalDate date;
    private int numberOfTimeslots;
    private int minNoStudents;

    public Lecture(){}

    /**
     * Constructor for the lecture entity.
     *
     * @param course the course of which this lecture is
     * @param date the preferred date at which this lecture should be held
     * @param numberOfTimeSlots the number of slots this lecture takes
     * @param minNumberOfStudents the minimum number of students that should attend this lecture
     */
    public Lecture(Course course, LocalDate date, int numberOfTimeSlots, int minNumberOfStudents) {
        this.course = course;
        this.date = date;
        this.numberOfTimeslots = numberOfTimeSlots;
        this.minNoStudents = minNumberOfStudents;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getNumberOfTimeslots() {
        return numberOfTimeslots;
    }

    public void setNumberOfTimeslots(int numberOfTimeslots) {
        this.numberOfTimeslots = numberOfTimeslots;
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
