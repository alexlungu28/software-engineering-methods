package op29sem58.courses;

import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Set;

public class LectureInfo {
    private String name;
    private int coursesId;
    private String coursesHasTeachersNetId;
    private LocalDate date;
    private int nSlots;

    public LectureInfo(String name, int coursesId, String coursesHasTeachersNetId, LocalDate date, int nSlots){
        this.name = name;
        this.coursesId = coursesId;
        this.coursesHasTeachersNetId = coursesHasTeachersNetId;
        this.date = date;
        this.nSlots = nSlots;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(int coursesId) {
        this.coursesId = coursesId;
    }

    public String getCoursesHasTeachersNetId() {
        return coursesHasTeachersNetId;
    }

    public void setCoursesHasTeachersNetId(String coursesHasTeachersNetId) {
        this.coursesHasTeachersNetId = coursesHasTeachersNetId;
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
}
