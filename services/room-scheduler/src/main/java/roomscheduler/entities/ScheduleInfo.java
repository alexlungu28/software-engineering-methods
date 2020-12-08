package roomscheduler.entities;

import java.sql.Timestamp;

public interface ScheduleInfo {

    Integer getRoomScheduleId();

    Timestamp getStartTime();

    Timestamp getEndTime();

    Integer getLectureId();

    Integer getRoomId();

    Integer getCoronaCapacity();
}
