package project.op29sem58.courses;

import java.time.LocalDate;

public class LectureInfo {
    private LocalDate date;
    private int numberOfTimeslots;
    private int minNoStudents;

    public LectureInfo(){}

    /**
     * Constructor for lectureInfo.
     *
     * @param date the preferred date for this lecture
     * @param numberOfTimeslots the number of timeslots this lecture takes
     * @param minNoStudents the minimum number of students that should attend this lecture
     */
    public LectureInfo(LocalDate date, int numberOfTimeslots, int minNoStudents) {
        this.date = date;
        this.numberOfTimeslots = numberOfTimeslots;
        this.minNoStudents = minNoStudents;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getNumberOfTimeslots() {
        return numberOfTimeslots;
    }

    public void setNumberOfTimeslots(int numberOfTimeslots) {
        this.numberOfTimeslots = numberOfTimeslots;
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
                "date=" + date +
                ", numberOfTimeslots=" + numberOfTimeslots +
                ", minNoStudents=" + minNoStudents +
                '}';
    }
}
