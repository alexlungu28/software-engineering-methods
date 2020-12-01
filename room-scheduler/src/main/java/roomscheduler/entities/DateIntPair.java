package roomscheduler.entities;

import java.sql.Timestamp;
import java.util.Objects;

public class DateIntPair {

    public Timestamp date;
    public Integer roomId;

    public DateIntPair(Timestamp date, Integer roomId) {
        this.date = date;
        this.roomId = roomId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateIntPair that = (DateIntPair) o;
        return Objects.equals(date, that.date) &&
                Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, roomId);
    }

    @Override
    public String toString() {
        return "DateIntPair{" +
                "date=" + date +
                ", id=" + roomId +
                '}';
    }
}
