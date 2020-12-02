package roomscheduler.entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

public class NameDateInfo {

    Timestamp date;
    String roomName;
    Integer roomSlotId;

    /**
     * Constructor for NameDateInfo.
     *

     * @param date date
     * @param roomName roomName
     * @param roomSlotId roomSlotId
     */
    public NameDateInfo(Timestamp date, String roomName, Integer roomSlotId) {
        this.date = date;
        this.roomName = roomName;
        this.roomSlotId = roomSlotId;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getRoomSlotId() {
        return roomSlotId;
    }

    public void setRoomSlotId(Integer roomSlotId) {
        this.roomSlotId = roomSlotId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NameDateInfo that = (NameDateInfo) o;
        return Objects.equals(date, that.date)
                && Objects.equals(roomName, that.roomName)
                && Objects.equals(roomSlotId, that.roomSlotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, roomName, roomSlotId);
    }

    @Override
    public String toString() {
        return "NameDateInfo{"
                + "date=" + date
                + ", roomName='" + roomName + '\''
                + ", roomSlotId=" + roomSlotId
                + '}';
    }
}
