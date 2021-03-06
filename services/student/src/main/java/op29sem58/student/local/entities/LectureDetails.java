package op29sem58.student.local.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LectureDetails {
    private int lectureId;
    private String courseName;
    private int roomId;
    private boolean onCampus;
    private transient LocalDateTime startTime;
    private transient LocalDateTime endTime;

    /**
     * This is to store the details of a lecture.
     *
     * @param lectureId The id of the lecture
     * @param courseName the name of the course
     * @param roomId the id of the room
     * @param onCampus if the lecture is on campus
     */
    public LectureDetails(int lectureId, String courseName, int roomId, boolean onCampus,
                          LocalDateTime startTime, LocalDateTime endTime) {
        this.lectureId = lectureId;
        this.courseName = courseName;
        this.roomId = roomId;
        this.onCampus = onCampus;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * To merge two LectureDetails list into one, so that it stays sorted.
     *
     * @param l1 first list to merge.
     * @param l2 second list to merge.
     * @return Merged list of lecturedetails, keeping the list sorted by ascending start time.
     */
    public static List<LectureDetails> merge(List<LectureDetails> l1, List<LectureDetails> l2) {
        List<LectureDetails> merged = new ArrayList<>();
        merged.addAll(l1);
        merged.addAll(l2);
        Collections.sort(merged, Comparator.comparing(LectureDetails::getStartTime));
        return merged;
    }

    public int getLectureId() {
        return lectureId;
    }

    public void setLectureId(int lectureId) {
        this.lectureId = lectureId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public boolean isOnCampus() {
        return onCampus;
    }

    public void setOnCampus(boolean onCampus) {
        this.onCampus = onCampus;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
