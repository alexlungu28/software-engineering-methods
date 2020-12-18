package op29sem58.student.controllers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import op29sem58.student.communication.ServerCommunication;
import op29sem58.student.communication.authorization.Authorization;
import op29sem58.student.communication.authorization.Role;
import op29sem58.student.database.entities.RoomSchedule;
import op29sem58.student.database.entities.Student;
import op29sem58.student.database.entities.StudentEnrollment;
import op29sem58.student.database.repos.StudentBookingRepo;
import op29sem58.student.database.repos.StudentEnrollmentRepo;
import op29sem58.student.database.repos.StudentRepo;
import op29sem58.student.local.entities.Course;
import op29sem58.student.local.entities.Lecture;
import op29sem58.student.local.entities.LectureDetails;
import op29sem58.student.local.entities.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    //This is a list consisting of all the courses with their lectures.
    private transient List<Course> courseList;

    //This is a list consisting of all the lectures.
    private transient List<Lecture> lectureList;

    //This is our class to communicate with other microservices
    private transient ServerCommunication serverCommunication = new ServerCommunication();
    
    /**
     * Initialize a default student set.
     */
    @PostMapping(path = "/initializeStudents")
    public ResponseEntity<Void> initializeStudents(@RequestHeader(authHeader) String token,
                                   @RequestBody List<Student> students) {
        if (!Authorization.authorize(token, Role.Admin)) {
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
        }

        this.students.saveAll(students);
        this.students.flush();
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * Retieves a list with all the students stored in the database.
     *
     * @return list with all students in the database
     */
    @GetMapping(path = "/getStudents")
    public ResponseEntity<List<Student>> getStudents(@RequestHeader(authHeader) String token) {
        if (!Authorization.authorize(token, Role.Admin)) {
            return new ResponseEntity<List<Student>>(HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<List<Student>>(this.students.findAll(), HttpStatus.OK);
    }


    /**
     * This should return all the lectures of the student sending the request,
     * This however is not the most optimal solution I could think of. But
     * will have to do, because of time constraint.
     *
     * @param token This is used to retrieve the user name.
     * @return a list with all it's lecture sorted ascending by date.
     */
    @GetMapping(path = "/allMyLectures")
    @SuppressWarnings("PMD") //DU anomaly
    public List<LectureDetails> getMyLectures(@RequestHeader("Authorization") String token) {
        // We first create an empty ArrayList, We then get the student by studentID.
        List<LectureDetails> campusLectures = new ArrayList<>();
        Student currentStudent = getStudentbyToken(token);

        // Here we iterate through all the allocated rooms for the student.
        // We then just add it to our lectureDetail list(This will be sorted by date).
        for (RoomSchedule roomSchedule : this.studentBookings.findByStudents(currentStudent)) {
            String courseName = getCourseName(roomSchedule.getLectureId());
            LectureDetails tempDetails = new LectureDetails(
                    roomSchedule.getLectureId(), courseName, roomSchedule.getRoomId(), true,
                    roomSchedule.getStartTime(), roomSchedule.getEndTime());
            campusLectures.add(tempDetails);
        }
        // We iterate through the list of lectures already sorted by date.
        // For every lecture we check if the student is enrolled.
        // If student is enrolled we check if we already had a lecture scheduled in a room
        // for the student, if not we can add the lecture to a list of onlineLectures.
        // we end by calling a helper function to merge the two lists.
        List<LectureDetails> onlineLectures = new ArrayList<>();
        for (Lecture lecture : this.lectureList) {
            if (studentIsEnrolledFor(currentStudent, lecture)) {
                Optional<LectureDetails> alreadyAssigned = campusLectures.stream()
                        .filter(e -> e.getLectureId() == lecture.getId()).findFirst();
                if (alreadyAssigned.isPresent()) {
                    continue;
                }
                String courseName = getCourseName(lecture.getId());
                RoomSchedule rs = lecture.getRoomSchedule();
                LectureDetails tempDetails = new LectureDetails(lecture.getId(), courseName,
                        0, false, rs.getStartTime(), rs.getEndTime());
                onlineLectures.add(tempDetails);
            }
        }
        merge(campusLectures, onlineLectures);

        return campusLectures;
    }

    /**
     * This should return all the courses the student sending the request,
     * is enrolled in.
     *
     * @param token This is used to retrieve the user name.
     * @return a list with all the student's courses
     */
    @GetMapping(path = "/allMyCourses")
    @SuppressWarnings("PMD") //DU anomaly
    public List<Pair<String, Integer>> getMyCourses(@RequestHeader("Authorization") String token) {
        // We first create an empty ArrayList, We then get the student by the token.
        List<Pair<String, Integer>> studentCourses = new ArrayList<>();
        Student currentStudent = getStudentbyToken(token);

        // Here we iterate through all the courses.
        // We then just create a pair of the name and year and put it in the list
        for (Course course : this.courseList) {
            if (this.studentEnrollments.findByCourseIdAndStudent(course.getCourseId(),
                    currentStudent).isEmpty()) {
                continue;
            }
            Pair<String, Integer> courseY = new Pair<>(course.getName(), course.getYearOfStudy());
            studentCourses.add(courseY);
        }
        return studentCourses;
    }

    /**
     * Small helper function to get the student by token.
     *
     * @param token required to send to the auth server
     * @return the student using the token
     */
    public Student getStudentbyToken(String token) {
        String studentId = serverCommunication.getUserId(token);
        return this.students.getOne(studentId);
    }

    /**
     * To merge two LectureDetails list into one, so that it stay's sorted.
     *
     * @param l1 first list.
     * @param l2 second list to merge.
     */
    @SuppressWarnings("PMD") //DU anomaly
    public static void merge(List<LectureDetails> l1, List<LectureDetails> l2) {
        for (int i = 0, j = 0; j < l2.size(); i++) {
            if (i == l1.size() ||
                    l1.get(i).getStartTime().isAfter(l2.get(j).getStartTime())) {
                l1.add(i, l2.get(j++));
            }
        }
    }

    /**
     * inefficient code just to get the courseName, due to lack of time this is the way we do it.
     *
     * @param lectureId to find the courseName
     * @return name of the course.
     */
    public String getCourseName(int lectureId) throws IllegalStateException {
        for (Course c : this.courseList) {
            if (c.courseHasLecture(lectureId)) {
                return c.getName();
            }
        }
        throw new IllegalStateException("Lecture must be linked with a course.");
    }

    /**
     * Assigns all students up until the date given in the options.
     *
     * @param date so that we can request all lectures up till that date
     */
    @PostMapping(path = "/assignStudentsUntil")
    @SuppressWarnings("PMD") //DU anomaly
    public ResponseEntity<Void> assignStudentsUntil(@RequestHeader(authHeader) String token,
                                    @RequestBody LocalDateTime date) {
        if (!Authorization.authorize(token, Role.Admin)) {
            return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
        }

        // Request all courses with their lectures from coursesService,
        // so courseLecturesList is initialized.
        this.initializeCourses(token);

        // get all lectures to assign students to, sorted by their startTime
        this.initializeAllScheduledLecturesUntil(date, token);

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
        for (final Lecture lecture : this.lectureList) {
            // get all students, where the highest priority students are at the start
            final List<Student> students =
                    this.students.findByWantsToGoTrueOrderByLastVisitedAsc();
            final RoomSchedule roomSchedule = lecture.getRoomSchedule();

            int assignedStudents = 0;
            final int allowedStudents = roomSchedule.getCoronaCapacity();
            for (Student student : students) {
                if (this.studentIsEnrolledFor(student, lecture)) {
                    roomSchedule.addStudent(student);
                    student.setLastVisited(lecture.getRoomSchedule().getStartTime());
                    this.students.save(student);
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
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    /**
     * This initializes all courses, by sending a request to the Courses Service.
     */
    @SuppressWarnings("PMD") //DU anomaly
    private void initializeCourses(String token) {
        courseList = this.serverCommunication.getAllCourses(token);
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
        Optional<Course> courseLecture = this.courseList.stream()
                .filter(e -> e.courseHasLecture(lecture.getId()))
                .findFirst();
        if (courseLecture.isEmpty()) {
            return false;
        }
        final List<StudentEnrollment> maybeStudentEnrollment =
            this.studentEnrollments.findByCourseIdAndStudent(courseLecture.get()
                    .getCourseId(), student);
        return !maybeStudentEnrollment.isEmpty();
    }


    /**
     * This gets a sorted list of lectures sorted by upcoming.
     *
     * @param date to get all lectures before date
     * @return return all lecture before date
     */
    private void initializeAllScheduledLecturesUntil(LocalDateTime date,
                                                     @RequestHeader("Authorization") String token) {
        // get the schedule via getSchedule endpoint
        final List<RoomSchedule> schedule = this.serverCommunication.getSchedule(token);

        // sort the lectures by their start time
        schedule.sort(Comparator.comparing(RoomSchedule::getStartTime));

        // collect lectures that matter
        this.lectureList = schedule.stream()
            .filter(roomSchedule -> roomSchedule.getStartTime().isBefore(date))
            .map(roomSchedule -> new Lecture(roomSchedule.getLectureId(), roomSchedule))
            .collect(Collectors.toList());
    }
}
