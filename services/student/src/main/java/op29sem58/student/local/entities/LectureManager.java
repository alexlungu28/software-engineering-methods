package op29sem58.student.local.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import op29sem58.student.database.entities.RoomSchedule;
import op29sem58.student.database.entities.Student;
import op29sem58.student.database.repos.StudentBookingRepo;
import op29sem58.student.database.repos.StudentEnrollmentRepo;

public class LectureManager {
    private transient List<Course> courses;
    private transient List<Lecture> lectures;
    private transient StudentBookingRepo studentBookings;
    private transient StudentEnrollmentRepo studentEnrollments;

    /**
     * Constructor for a LectureManager.
     *
     * @param courses A list so we know what courses exist.
     * @param lectures a list of lectures that exist.
     * @param studentEnrollments So we know what a student is enrolled to.
     * @param studentBookings so we know what rooms are assigned to students.
     */
    public LectureManager(List<Course> courses,
                          List<Lecture> lectures,
                          StudentEnrollmentRepo studentEnrollments,
                          StudentBookingRepo studentBookings) {
        this.courses = courses;
        this.lectures = lectures;
        this.studentEnrollments = studentEnrollments;
        this.studentBookings = studentBookings;
    }

    public List<LectureDetails> getAllLectures(Student student) {
        return LectureDetails.merge(this.getCampusLectures(student), getOnlineLectures(student));
    }

    /**
     * This retrieves all the campus lectures a student has.
     *
     * @param student used to retrieve the student bookings.
     * @return a list of all the lectures the student can attend on campus.
     */
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

    /**
     * This retrieves all the online lecture a student has,
     * it is dependent on the campusLectures for the student.
     *
     * @param student to get all his online lectures.
     * @return a list of all online lectures for the student.
     */
    @SuppressWarnings("PMD") //DU anomaly
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
