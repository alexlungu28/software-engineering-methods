package op29sem58.courses;

import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.Set;

public class LectureInfo {
    private int coursesId;
    private LocalDate date;
    private int nSlots;

    public LectureInfo(int coursesId, LocalDate date, int nSlots){
        this.coursesId = coursesId;
        this.date = date;
        this.nSlots = nSlots;
    }

    public int getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(int coursesId) {
        this.coursesId = coursesId;
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
