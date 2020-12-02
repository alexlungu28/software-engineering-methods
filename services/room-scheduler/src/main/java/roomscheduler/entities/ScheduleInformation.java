package roomscheduler.entities;

import java.sql.Timestamp;
import java.util.Objects;

public class ScheduleInformation implements ScheduleInfo{

    Integer roomScheduleId;
    Timestamp startTime;
    Timestamp endTime;
    Integer lectureId;
    Integer roomId;
    Integer coronaCapacity;

    public ScheduleInformation(Integer roomScheduleId, Timestamp startTime, Timestamp endTime, Integer lectureId, Integer roomId, Integer coronaCapacity) {
        this.roomScheduleId = roomScheduleId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.lectureId = lectureId;
        this.roomId = roomId;
        this.coronaCapacity = coronaCapacity;
    }

    @Override
    public Integer getRoomScheduleId() {
        return roomScheduleId;
    }

    public void setRoomScheduleId(Integer roomScheduleId) {
        this.roomScheduleId = roomScheduleId;
    }

    @Override
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Override
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Override
    public Integer getLectureId() {
        return lectureId;
    }

    public void setLectureId(Integer lectureId) {
        this.lectureId = lectureId;
    }

    @Override
    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    @Override
    public Integer getCoronaCapacity() {
        return coronaCapacity;
    }

    public void setCoronaCapacity(Integer coronaCapacity) {
        this.coronaCapacity = coronaCapacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ScheduleInformation that = (ScheduleInformation) o;
        return Objects.equals(roomScheduleId, that.roomScheduleId) &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(lectureId, that.lectureId) &&
                Objects.equals(roomId, that.roomId) &&
                Objects.equals(coronaCapacity, that.coronaCapacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomScheduleId, startTime, endTime, lectureId, roomId, coronaCapacity);
    }

    @Override
    public String toString() {
        return "ScheduleInformation{" +
                "roomScheduleId=" + roomScheduleId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", lectureId=" + lectureId +
                ", roomId=" + roomId +
                ", coronaCapacity=" + coronaCapacity +
                '}';
    }
}
