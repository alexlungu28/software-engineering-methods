package op29sem58.student.database.entities;

import java.util.Set;

import javax.persistence.*;

@Entity(name = "RoomSchedule")
public class RoomSchedule {
    @Id
    @Column(unique=true, nullable=false)
	private Integer roomScheduleId;

	@ManyToMany()
	private Set<Student> students;

	public RoomSchedule(Set<Student> students, int roomScheduleId) {
		this.students = students;
		this.roomScheduleId = roomScheduleId;
	}

	public Set<Student> getStudents() {
		return this.students;
	}

	public void addStudent(Student student) {
		this.students.add(student);
	}

	public Integer getRoomScheduleId() {
		return this.roomScheduleId;
	}
}
