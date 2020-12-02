package op29sem58.student.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import op29sem58.student.AssignUntilOptions;
import op29sem58.student.CourseLectures;
import op29sem58.student.Lecture;
import op29sem58.student.communication.ServerCommunication;
import op29sem58.student.database.entities.RoomSchedule;
import op29sem58.student.database.entities.Student;
import op29sem58.student.database.entities.StudentEnrollment;
import op29sem58.student.database.repos.StudentBookingRepo;
import op29sem58.student.database.repos.StudentEnrollmentRepo;
import op29sem58.student.database.repos.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StudentController {
    @Autowired
    private transient StudentRepo students;

    @Autowired
    private transient StudentBookingRepo studentBookings;

    @Autowired
    private transient StudentEnrollmentRepo studentEnrollments;

    private transient Map<Integer, Integer> lectureIdToCourseId;

    private transient ServerCommunication serverCommunication = new ServerCommunication();

    /**
     * Assigns all students up until the date given in the options.
     *
     * @param options Contains options for the request.
     */
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    @PostMapping(path = "/assignStudentsUntil")
    public void assignStudentsUntil(@RequestBody AssignUntilOptions options) {
        // initialize all courses
        this.initializeCourses();

        // get all lectures to assign students to, sorted by their starttime
        final List<Lecture> lectures = this.getAllScheduledSortedLecturesUntil(options.date);

        // get all students, where the highest priority students are at the start
        final List<Student> students = this.students.findByWantsToGoTrueOrderByLastVisitedAsc();

        // assign students to lectures
        final List<RoomSchedule> studentSchedule = new ArrayList<>();
        for (final Lecture lecture : lectures) {
            final RoomSchedule roomSchedule = lecture.getRoomSchedule();

            int assignedStudents = 0;
            final int allowedStudents = roomSchedule.getCoronaCapacity();
            for (int i = 0; i < students.size(); i++) {
                final Student student = students.get(i);
                if (this.studentIsEnrolledFor(student, lecture)) {
                    roomSchedule.addStudent(student);
                    student.setLastVisited(lecture.getRoomSchedule().getStartTime());
                    this.students.save(student);
                    students.remove(i);
                    students.add(student);
                    i--;
                    assignedStudents++;
                    if (assignedStudents >= allowedStudents) {
                        break;
                    }
                }
            }
            studentSchedule.add(roomSchedule);
        }
        // save in database
        this.studentBookings.saveAll(studentSchedule);
        this.studentBookings.flush();
    }

    private void initializeCourses() {
        // get all lectures via /getAllLectures endpoint
        final List<CourseLectures> courseLecturesList = this.serverCommunication.getAllLectures();

        // fill map
        this.lectureIdToCourseId = new HashMap<>();
        for (CourseLectures courseLectures : courseLecturesList) {
            @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
            final int courseId = courseLectures.getCourseId();
            for (final int lectureId : courseLectures.getLectureIds()) {
                this.lectureIdToCourseId.put(lectureId, courseId);
            }
        }
    }

    private boolean studentIsEnrolledFor(Student student, Lecture lecture) {
        final int courseId = this.getCourseIdForLecture(lecture);
        final Optional<StudentEnrollment> maybeStudentEnrollment =
            this.studentEnrollments.findByCourseIdAndStudent(courseId, student);
        return maybeStudentEnrollment.isPresent();
    }

    private int getCourseIdForLecture(Lecture lecture) {
        return this.lectureIdToCourseId.get(lecture.getId());
    }

    private List<Lecture> getAllScheduledSortedLecturesUntil(LocalDateTime date) {
        // get the schedule via getSchedule endpoint
        final List<RoomSchedule> schedule = this.serverCommunication.getSchedule();

        // sort the lectures by their start time
        schedule.sort(Comparator.comparing(RoomSchedule::getStartTime));

        // collect lectures that matter
        return schedule.stream()
            .filter(roomSchedule -> roomSchedule.getStartTime().isBefore(date))
            .map(roomSchedule -> new Lecture(roomSchedule.getLectureId(), roomSchedule))
            .collect(Collectors.toList());
    }
}
