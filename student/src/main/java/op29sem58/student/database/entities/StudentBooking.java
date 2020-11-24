package op29sem58.student.database.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "StudentBooking")
public class StudentBooking {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique=true, nullable=false)
	private Integer id;

	private Integer roomScheduleId;

	public StudentBooking(int roomScheduleId) {
		this.roomScheduleId = roomScheduleId;
	}

	public Integer getId() {
		return this.id;
	}

	public Integer getRoomScheduleId() {
		return this.roomScheduleId;
	}
}
