package op29sem58.student.database.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity(name = "RoomSchedule")
public class RoomSchedule {
    @Id
    @Column(unique=true, nullable=false)
	private Integer roomScheduleId;

	@ManyToMany()
	private Set<Student> students;

	@Transient
	private int lectureId;
	@Transient
	private int roomId;
	@Transient
	private LocalDateTime startTime;
	@Transient
	private LocalDateTime endTime;
	@Transient
	private int coronaCapacity;

	public RoomSchedule(LocalDateTime startTime, LocalDateTime endTime, int id, int lectureId, int roomId, int coronaCapacity) {
		this.startTime = startTime;
		this.endTime = endTime;
		this.roomScheduleId = id;
		this.lectureId = lectureId;
		this.roomId = roomId;
		this.coronaCapacity = coronaCapacity;
	}

	public RoomSchedule() {
		this.students = new HashSet<Student>();
	}

	public int getCoronaCapacity() {
		return coronaCapacity;
	}

	public void setCoronaCapacity(int coronaCapacity) {
		this.coronaCapacity = coronaCapacity;
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

	public LocalDateTime getStartTime() {
		return this.startTime;
	}

	public LocalDateTime getEndTime() {
		return this.endTime;
	}

	public int getId() {
		return this.roomScheduleId;
	}

	public int getLectureId() {
		return this.lectureId;
	}

	public int getRoomId() {
		return this.roomId;
	}
}
