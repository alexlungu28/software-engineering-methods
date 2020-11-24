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
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique=true, nullable=false)
	private Integer id;
	
	private Integer courseId;

	@ManyToOne
	@JoinColumn(name = "student_net_id")
	private Student student;
	
	public Integer getId() {
		return this.id;
	}

	public Integer getCourseId() {
		return this.courseId;
	}

	public Student getStudent() {
		return this.student;
	}
}
