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
import op29sem58.student.local.entities.Course;
import op29sem58.student.local.entities.Lecture;
import op29sem58.student.local.entities.LectureDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    //This is a list consisting of all the courses with their lectures.
    private transient List<Course> courseList;

    //This is a list consisting of all the courses with their lectures.
    private transient List<Lecture> lectureList;

    //This is our class to communicate with other microservices
    private transient ServerCommunication serverCommunication = new ServerCommunication();
    
    /**
     * Initialize a default student set.
     */
    @PostMapping(path = "/initializeStudents")
    public void initializeStudents(@RequestBody List<Student> students) {
        this.students.saveAll(students);
        this.students.flush();
    }

    /**
     * Retieves a list with all the students stored in the database.
     *
     * @return list with all students in the database
     */
    @GetMapping(path = "/getStudents")
    public List<Student> getStudents() {
        return this.students.findAll();
    }

    /**
     * This should return all the lectures of the student sending the request,
     * This however is not the most optimal solution I could think of. But
     * will have to due, because of time constraint.
     *
     * @param studentId This is temporarily, should get the studentID by the token
     * @return a list with all it's lecture sorted ascending by date.
     */
    @PostMapping(path = "/AllMyLectures")
    @SuppressWarnings("PMD") //DU anomaly
    public List<LectureDetails> getMyLectures(@RequestBody String studentId) {
        // We first create an empty ArrayList, We then get the student by studentID.
        List<LectureDetails> result = new ArrayList<LectureDetails>();
        Student currentStudent = students.getOne(studentId);

        // Here we iterate through all the allocated rooms for the student.
        // We then just add it to our lectureDetail list(This will be sorted by date).
        Iterator<RoomSchedule> i = studentBookings.findByStudents(currentStudent).iterator();
        while (i.hasNext()) {
            RoomSchedule roomSchedule = i.next();
            String courseName = getCourseName(roomSchedule.getLectureId());
            LectureDetails tempDetails = new LectureDetails(
                    roomSchedule.getLectureId(), courseName, roomSchedule.getRoomId(), true,
                    roomSchedule.getStartTime(), roomSchedule.getEndTime());
            result.add(tempDetails);
            i.remove();
        }
        // Now comes the hard part, we will iterate through all the course
        Iterator<Lecture> p = lectureList.iterator();
        while (p.hasNext()) {
            Lecture lecture = p.next();
            if (studentIsEnrolledFor(currentStudent, lecture)) {
                Optional<LectureDetails> alreadyAssigned = result.stream()
                        .filter(e -> e.getLectureId() == lecture.getId()).findFirst();
                if (alreadyAssigned.isPresent()) {
                    continue;
                }
                String courseName = getCourseName(lecture.getId());
                RoomSchedule rs = lecture.getRoomSchedule();
                LectureDetails tempDetails = new LectureDetails(lecture.getId(), courseName,
                        0, false, rs.getStartTime(), rs.getEndTime());
                result.add(tempDetails);
            }
            p.remove();
        }
        return result;
    }

    /**
     * inefficient code just to get the courseName, due to lack of time this is the way we do it.
     *
     * @param lectureId to find the courseName
     * @return name of the course.
     */
    public String getCourseName(int lectureId) {
        for (Course c : courseList) {
            if (c.courseHasLecture(lectureId)) {
                return c.getName();
            }
        }
        return "Course less";
    }

    /**
     * Assigns all students up until the date given in the options.
     *
     * @param date so that we can request all lectures up till that date
     */
    @PostMapping(path = "/assignStudentsUntil")
    @SuppressWarnings("PMD") //DU anomaly
    public void assignStudentsUntil(@RequestBody LocalDateTime date) {
        // Request all courses with their lectures from coursesService,
        // so courseLecturesList is initialized.
        this.initializeCourses();

        // get all lectures to assign students to, sorted by their startTime
        this.getAllScheduledSortedLecturesUntil(date);

        // Now that we have a list with all upcoming lectures sorted by earliest startTime.
        // We can allocate students to them. We do this by iterating through the lectures and 
        // getting the scheduled room for each lecture. For each lecture we also make a call to our
        // repository to get all students, sorted by last visited. This makes sure that we always
        // allocate the last visited students. We retrieve the room of the lecture and also keep
        // track of the allocatedStudents for that room and use the retrieved coronaCapacity as the
        // upperbound. Knowing this, we can iterate through all the students to check if they are
        // enrolled for the lecture, if so we add them to the roomSchedule, modify the student 
        // lastVisited by the startTime of the lecture. Increment the assignedStudents variable
        // and then check if the assigned students equals the allowedStudents. if so we break.
        // We end with a list of RoomSchedules which are linked with students and we save this
        // list in the database. This Creates the many to many relationship in the database.
        // To be clear this stores all the students who go to campus.
        final List<RoomSchedule> studentSchedule = new ArrayList<>();
        for (final Lecture lecture : lectureList) {
            // get all students, where the highest priority students are at the start
            final List<Student> students = this.students.findByWantsToGoTrueOrderByLastVisitedAsc();
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
    }

    /**
     * This initializes all courses, by sending a request to the Courses Service.
     */
    @SuppressWarnings("PMD") //DU anomaly
    private void initializeCourses() {
        courseList = this.serverCommunication.getAllCourse();
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
        Optional<Course> courseLecture = courseList.stream()
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
    private void getAllScheduledSortedLecturesUntil(LocalDateTime date) {
        // get the schedule via getSchedule endpoint
        final List<RoomSchedule> schedule = this.serverCommunication.getSchedule();

        // sort the lectures by their start time
        schedule.sort(Comparator.comparing(RoomSchedule::getStartTime));

        // collect lectures that matter
        lectureList = schedule.stream()
            .filter(roomSchedule -> roomSchedule.getStartTime().isBefore(date))
            .map(roomSchedule -> new Lecture(roomSchedule.getLectureId(), roomSchedule))
            .collect(Collectors.toList());
    }
}
