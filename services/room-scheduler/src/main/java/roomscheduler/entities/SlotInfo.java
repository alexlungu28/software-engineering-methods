package roomscheduler.entities;

import java.sql.Timestamp;
import java.util.Objects;

public class SlotInfo {

    public Timestamp date;
    public Integer roomId;
    public Integer roomSlotId;

    /**
     * Constructor for Slot Info.
     *
     * @param date date
     * @param roomId roomId
     * @param roomSlotId roomSlotId
     */
    public SlotInfo(Timestamp date, Integer roomId, Integer roomSlotId) {
        this.date = date;
        this.roomId = roomId;
        this.roomSlotId = roomSlotId;
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
        SlotInfo slotInfo = (SlotInfo) o;
        return Objects.equals(date, slotInfo.date) &&
                Objects.equals(roomId, slotInfo.roomId) &&
                Objects.equals(roomSlotId, slotInfo.roomSlotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, roomId, roomSlotId);
    }

    @Override
    public String toString() {
        return "SlotInfo{" +
                "date=" + date +
                ", roomId=" + roomId +
                ", roomSlotId=" + roomSlotId +
                '}';
    }
}
