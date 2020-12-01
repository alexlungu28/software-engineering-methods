package op29sem58.student;

public class Lecture {
	private RoomSchedule roomSchedule;
	private int id;

	public Lecture(int id, RoomSchedule roomSchedule) {
		this.id = id;
		this.roomSchedule = roomSchedule;
	}

	public RoomSchedule getRoomSchedule() {
		return this.roomSchedule;
	}

	public int getId() {
		return this.id;
	}
}
