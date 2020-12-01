package op29sem58.courses;

import java.time.LocalDate;

public class LectureInfo {
    private int courseId;
    private LocalDate date;
    private int nslots;
    private int minNoStudents;

    public LectureInfo(){}

    /**
     * Constructor for lectureInfo.
     *
     * @param courseId the id of the course for which this lecture is
     * @param date the preferred date for this lecture
     * @param nslots the number of timeslots this lecture takes
     * @param minNoStudents the minimum number of students that should attend this lecture
     */
    public LectureInfo(int courseId, LocalDate date, int nslots, int minNoStudents) {
        this.courseId = courseId;
        this.date = date;
        this.nslots = nslots;
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

    public int getNslots() {
        return nslots;
    }

    public void setNslots(int nslots) {
        this.nslots = nslots;
    }

    public int getMinNoStudents() {
        return minNoStudents;
    }

    public void setMinNoStudents(int minNoStudents) {
        this.minNoStudents = minNoStudents;
    }

    @Override
    public String toString() {
        return "LectureInfo{"
                + "courseId=" + courseId
                + ", date=" + date
                + ", nSlots=" + nslots
                + ", minNoStudents=" + minNoStudents
                + '}';
    }
}
