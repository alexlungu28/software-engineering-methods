package op29sem58.courses;

import java.time.LocalDate;

public class LectureInfo {
    private int courseId;
    private LocalDate date;
    private int nSlots;
    private int minNoStudents;

    public LectureInfo(){}

    public LectureInfo(int courseId, LocalDate date, int nSlots, int minNoStudents){
        this.courseId = courseId;
        this.date = date;
        this.nSlots = nSlots;
        this.minNoStudents = minNoStudents;
    }

    public int getCoursesId() {
        return this.courseId;
    }

    public void setCoursesId(int coursesId) {
        this.courseId = coursesId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getnSlots() {
        return nSlots;
    }

    public void setnSlots(int nSlots) {
        this.nSlots = nSlots;
    }

    public int getMinNoStudents() {
        return minNoStudents;
    }

    public void setMinNoStudents(int minNoStudents) {
        this.minNoStudents = minNoStudents;
    }

    @Override
    public String toString() {
        return "LectureInfo{" +
                "courseId=" + courseId +
                ", date=" + date +
                ", nSlots=" + nSlots +
                ", minNoStudents=" + minNoStudents +
                '}';
    }
}
