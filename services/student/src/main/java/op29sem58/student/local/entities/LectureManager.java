package op29sem58.student.local.entities;

import op29sem58.student.database.entities.RoomSchedule;
import op29sem58.student.database.entities.Student;
import op29sem58.student.database.repos.StudentBookingRepo;
import op29sem58.student.database.repos.StudentEnrollmentRepo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LectureManager {
    private List<Course> courses;
    private List<Lecture> lectures;
    private StudentBookingRepo studentBookings;
    private StudentEnrollmentRepo studentEnrollments;

    public LectureManager(List<Course> courses, List<Lecture> lectures, StudentEnrollmentRepo studentEnrollments,
                          StudentBookingRepo studentBookings) {
        this.courses = courses;
        this.lectures = lectures;
        this.studentEnrollments = studentEnrollments;
        this.studentBookings = studentBookings;
    }

    public List<LectureDetails> getAllLectures(Student student) {
        return LectureDetails.merge(this.getCampusLectures(student), getOnlineLectures(student));
    }

    public List<LectureDetails> getCampusLectures(Student student) {
        List<LectureDetails> campusLectures = new ArrayList<>();
        for (RoomSchedule roomSchedule : this.studentBookings.findByStudents(student)) {
            String courseName = this.getCourseName(roomSchedule.getLectureId());
            LectureDetails tempDetails = new LectureDetails(
                    roomSchedule.getLectureId(), courseName, roomSchedule.getRoomId(), true,
                    roomSchedule.getStartTime(), roomSchedule.getEndTime());
            campusLectures.add(tempDetails);
        }
        return campusLectures;
    }

    public List<LectureDetails> getOnlineLectures(Student student) {
        // We iterate through the list of lectures already sorted by date.
        // For every lecture we check if the student is enrolled.
        // If student is enrolled we check if we already had a lecture scheduled in a room
        // for the student, if not we can add the lecture to a list of onlineLectures.
        // we end by calling a helper function to merge the two lists.
        List<LectureDetails> campusLectures = this.getCampusLectures(student);
        List<LectureDetails> onlineLectures = new ArrayList<>();
        for (Lecture lecture : lectures) {
            if (student.isEnrolledFor(courses, lecture, this.studentEnrollments)) {
                Optional<LectureDetails> alreadyAssigned = campusLectures.stream()
                        .filter(e -> e.getLectureId() == lecture.getId()).findFirst();
                if (alreadyAssigned.isPresent()) {
                    continue;
                }
                String courseName = this.getCourseName(lecture.getId());
                RoomSchedule rs = lecture.getRoomSchedule();
                LectureDetails tempDetails = new LectureDetails(lecture.getId(), courseName,
                        0, false, rs.getStartTime(), rs.getEndTime());
                onlineLectures.add(tempDetails);
            }
        }
        return onlineLectures;
    }

    /**
     * inefficient code just to get the courseName, due to lack of time this is the way we do it.
     *
     * @param lectureId to find the courseName
     * @return name of the course.
     */
    public String getCourseName(int lectureId) throws IllegalStateException {
        for (Course c : this.courses) {
            if (c.courseHasLecture(lectureId)) {
                return c.getName();
            }
        }
        throw new IllegalStateException("Lecture must be linked with a course.");
    }
}
