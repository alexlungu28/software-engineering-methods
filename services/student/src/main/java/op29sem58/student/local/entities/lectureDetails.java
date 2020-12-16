package op29sem58.student.local.entities;

public class lectureDetails {
    private int lectureID;
    private String courseName;
    private int roomID;
    private boolean onCampus;

    public lectureDetails(int lectureID, String courseName, int roomID, boolean onCampus) {
        this.lectureID = lectureID;
        this.courseName = courseName;
        this.roomID = roomID;
        this.onCampus = onCampus;
    }

    public int getLectureID() {
        return lectureID;
    }

    public void setLectureID(int lectureID) {
        this.lectureID = lectureID;
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

    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }
}
