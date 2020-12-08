package op29sem58.student.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * This it a database entry to keep track of the courses each student is enrolled to.
 * We have a manyToOne relationship to the student. As a Student can have many courses
 * he is enrolled to. We do not store Courses on our side, only the courseID which is
 * why it is not marked as a manyToMany relationship. As a course can have multiple
 * students as well of course.
 */
@Entity(name = "StudentEnrollment")
public class StudentEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int id;
    
    private int courseId;

    @ManyToOne
    @JoinColumn(name = "student_net_id")
    private Student student;
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return this.courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Student getStudent() {
        return this.student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
}
