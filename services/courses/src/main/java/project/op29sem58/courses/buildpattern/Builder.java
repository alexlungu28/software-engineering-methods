package project.op29sem58.courses.buildpattern;

import java.time.LocalDate;
import project.op29sem58.courses.database.entities.Course;
import project.op29sem58.courses.database.entities.Lecture;

public interface Builder {

    public void setCourse(Course course);

    public void setDate(LocalDate date);

    public void setMinNoStudents(int minNoStudents);

    public void setNumberOfTimeslots(int numberOfTimeslots);

    Lecture build();

}
