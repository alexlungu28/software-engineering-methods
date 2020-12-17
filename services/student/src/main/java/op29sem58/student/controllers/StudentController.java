package op29sem58.student.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import op29sem58.student.communication.ServerCommunication;
import op29sem58.student.database.entities.RoomSchedule;
import op29sem58.student.database.entities.Student;
import op29sem58.student.database.entities.StudentEnrollment;
import op29sem58.student.database.repos.StudentBookingRepo;
import op29sem58.student.database.repos.StudentEnrollmentRepo;
import op29sem58.student.database.repos.StudentRepo;
import op29sem58.student.local.entities.CourseLectures;
import op29sem58.student.local.entities.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

// Here is the main Student Service code. This handles all the requests required to assign students.
@RestController
public class StudentController {

    //The Repo's are annotated with @Autowired, this helps
    // Spring connect the Repository's so that hibernate can retrieve
    // the students, studentBookings and studentEnrollments
    @Autowired
    private transient StudentRepo students;

    @Autowired
    private transient StudentBookingRepo studentBookings;

    @Autowired
    private transient StudentEnrollmentRepo studentEnrollments;

    final transient String authHeader = "Authorization";

    transient String errorMessage = "You do not have the privilege to perform this action.";

    //This is a list consisting of all the courses with their lectures.
    private transient List<CourseLectures> courseLecturesList;

    //This is our class to communicate with other microservices
    private transient ServerCommunication serverCommunication = new ServerCommunication();
    
    /**
     * Initialize a default student set.
     */
    @PostMapping(path = "/initializeStudents")
    public void initializeStudents(@RequestHeader(authHeader) String token,
                                   @RequestBody List<Student> students) {
        if (Authorization.authorize(token, "Admin")) {
            this.students.saveAll(students);
            this.students.flush();
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * Retieves a list with all the students stored in the database.
     *
     * @return list with all students in the database
     */
    @GetMapping(path = "/getStudents")
    public List<Student> getStudents(@RequestHeader(authHeader) String token) {
        if (Authorization.authorize(token, "Admin")) {
            return this.students.findAll();
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * Assigns all students up until the date given in the options.
     *
     * @param date so that we can request all lectures up till that date
     */
    @PostMapping(path = "/assignStudentsUntil")
    @SuppressWarnings("PMD") //DU anomaly
    public void assignStudentsUntil(@RequestHeader(authHeader) String token,
                                    @RequestBody LocalDateTime date) {
        if (Authorization.authorize(token, "Admin")) {
            // Request all courses with their lectures from coursesService,
            // so courseLecturesList is initialized.
            this.initializeCourses();

            // get all lectures to assign students to, sorted by their startTime
            final List<Lecture> lectures = this.getAllScheduledSortedLecturesUntil(date);

            // Now that we have a list with all upcoming lectures sorted by earliest startTime.
            // We can allocate students to them. We do this by iterating through the lectures and
            // getting the scheduled room for each lecture. For each lecture we also call our
            // repository to get all students, sorted by last visited. This ensures that we always
            // allocate the last visited students. We retrieve the room of the lecture and also keep
            // track of the allocatedStudents for that room and use the coronaCapacity as the
            // upperbound. Knowing this, we can iterate through all students to check if they are
            // enrolled for the lecture, if so we add them to the roomSchedule, modify the student
            // lastVisited by the startTime of the lecture. Increment the assignedStudents variable
            // and then check if the assigned students equals the allowedStudents. if so we break.
            // We end with a list of RoomSchedules which are linked with students and we save this
            // list in the database. This Creates the many to many relationship in the database.
            // To be clear this stores all the students who go to campus.
            final List<RoomSchedule> studentSchedule = new ArrayList<>();
            for (final Lecture lecture : lectures) {
                // get all students, where the highest priority students are at the start
                final List<Student> students =
                        this.students.findByWantsToGoTrueOrderByLastVisitedAsc();
                final RoomSchedule roomSchedule = lecture.getRoomSchedule();

                int assignedStudents = 0;
                final int allowedStudents = roomSchedule.getCoronaCapacity();
                Iterator<Student> i = students.iterator();
                while (i.hasNext()) {
                    Student student = i.next();
                    if (this.studentIsEnrolledFor(student, lecture)) {
                        roomSchedule.addStudent(student);
                        student.setLastVisited(lecture.getRoomSchedule().getStartTime());
                        this.students.save(student);
                        assignedStudents++;
                        if (assignedStudents >= allowedStudents) {
                            break;
                        }
                    }
                    i.remove();
                }
                studentSchedule.add(roomSchedule);
            }
            // save in database
            this.studentBookings.saveAll(studentSchedule);
            this.studentBookings.flush();
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * This initializes all courses, by sending a request to the Courses Service.
     */
    @SuppressWarnings("PMD") //DU anomaly
    private void initializeCourses() {
        courseLecturesList = this.serverCommunication.getAllLectures();
    }

    /**
     * This checks if the student is enrolled for the lecture, by iterating though the
     * list of courseLectures and retrieving the courseID by the use of the lectureID.
     * Then send a request to enrollments with the courseID and student.
     * If student is enrolled a boolean true will be returned.
     *
     * @param student a Student to check if enrolled
     * @param lecture a lecture to get the courseID
     * @return a boolean if the student is enrolled.
     */
    private boolean studentIsEnrolledFor(Student student, Lecture lecture) {
        Optional<CourseLectures> courseLecture = courseLecturesList.stream()
                .filter(e -> e.courseHasLecture(lecture.getId()))
                .findFirst();
        if (courseLecture.isEmpty()) {
            return false;
        }
        final Optional<StudentEnrollment> maybeStudentEnrollment =
            this.studentEnrollments.findByCourseIdAndStudent(courseLecture.get()
                    .getCourseId(), student);
        return maybeStudentEnrollment.isPresent();
    }

    /**
     * This gets a sorted list of lectures sorted by upcoming.
     *
     * @param date to get all lectures before date
     * @return return all lecture before date
     */
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
