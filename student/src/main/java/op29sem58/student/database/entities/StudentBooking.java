package op29sem58.student.database.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "StudentBooking")
public class StudentBooking {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique=true, nullable=false)
	private Integer id;

	private Integer roomScheduleId;

	@OneToMany(
            mappedBy = "netID",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
            orphanRemoval = true
    )
	private Set<Student> students;

	public StudentBooking(Set<Student> students, int roomScheduleId) {
		this.students = students;
		this.roomScheduleId = roomScheduleId;
	}

	public Set<Student> getStudents() {
		return this.students;
	}

	public void addStudent(Student student) {
		this.students.add(student);
	}

	public Integer getId() {
		return this.id;
	}

	public Integer getRoomScheduleId() {
		return this.roomScheduleId;
	}
}
