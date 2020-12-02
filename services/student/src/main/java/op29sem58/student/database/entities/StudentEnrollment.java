package op29sem58.student.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
