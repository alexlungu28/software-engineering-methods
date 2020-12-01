package op29sem58.student;

import java.time.LocalDateTime;

public class RoomSchedule {
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private int id;
	private int lectureId;
	private int roomId;


	public LocalDateTime getStartTime() {
		return this.startTime;
	}

	public LocalDateTime getEndTime() {
		return this.endTime;
	}

	public int getId() {
		return this.id;
	}

	public int getLectureId() {
		return this.lectureId;
	}

	public int getRoomId() {
		return this.roomId;
	}
}
