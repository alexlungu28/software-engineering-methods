package op29sem58.student;

public class CourseLectures {
    private int courseId;
    private int[] lectureIds;

    public CourseLectures(int courseId, int[] lectureIds) {
        this.courseId = courseId;
        this.lectureIds = lectureIds;
    }

    public boolean courseHasLecture(int lectureId) {
        for(int id : this.lectureIds) {
            if (id == lectureId) return true;
        }
        return false;
    }
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int[] getLectureIds() {
        return this.lectureIds;
    }
    
    public void setLectureIds(int[] lectureIds) {
        this.lectureIds = lectureIds;
    }
}
