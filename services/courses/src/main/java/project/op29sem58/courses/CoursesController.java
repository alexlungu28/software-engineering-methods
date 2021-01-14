package project.op29sem58.courses;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import project.op29sem58.courses.buildpattern.Builder;
import project.op29sem58.courses.buildpattern.Director;
import project.op29sem58.courses.buildpattern.LectureBuilder;
import project.op29sem58.courses.communication.authorization.Authorization;
import project.op29sem58.courses.communication.authorization.Role;
import project.op29sem58.courses.database.entities.Course;
import project.op29sem58.courses.database.entities.Lecture;
import project.op29sem58.courses.database.repos.CoursesRepo;
import project.op29sem58.courses.database.repos.LecturesRepo;

@Controller
public class CoursesController {

    @Autowired
    private CoursesRepo coursesRepo;
    @Autowired
    private LecturesRepo lecturesRepo;

    //PMD nonsense
    private static final int FIRST_YEAR = 1;
    private static final int SECOND_YEAR = 2;

    final transient String authHeader = "Authorization";
    final transient ResponseEntity<String> internalError = new ResponseEntity<String>("Something " +
            "went wrong on our end, please try again later.",
            HttpStatus.INTERNAL_SERVER_ERROR);
    private static final String ROOM_SCHEDULE_SERVICE_PORT = "8081";

    transient String errorMessage = "You do not have the privilege to perform this action.";

    transient String teacher = "Teacher";

    transient LocalDate date;

    /**
     * Retrieve a list of all courses.
     *
     * @param token jwt token
     * @return all courses
     */
    @GetMapping(path = "/getAllCourses")
    public ResponseEntity<Iterable<Course>> getAllCourses(@RequestHeader(authHeader) String token) {
        if (!Authorization.authorize(token, Role.Student)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(coursesRepo.findAll());
    }

    /**
     * Retrieve a list of all lectures.
     *
     * @return all lectures
     */
    @GetMapping(path = "/getAllLectures")
    public @ResponseBody ResponseEntity<Iterable<Lecture>>
    getAllLectures(@RequestHeader(authHeader) String token) {
        if (!Authorization.authorize(token, Role.Student)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return ResponseEntity.ok(lecturesRepo.findAll());
    }

    /**
     * Creates a course in the database.
     *
     * @param token jwt token
     * @param c course
     * @return saved if successful, error otherwise
     */
    @PostMapping(path = "/createCourse")
    public ResponseEntity<Course> createCourse(@RequestHeader(authHeader) String token,
                                       @RequestBody Course c) {
        if (!Authorization.authorize(token, Role.Teacher)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        coursesRepo.saveAndFlush(c);
        return new ResponseEntity<>(c, HttpStatus.CREATED);
    }

    /**
     * Creates a lecture for the course of CourseID.
     *
     * @param courseId the id of the course this lecture is for
     * @param l the lecture to be scheduled
     * @return a string with information about the status of the request
     */
    @PostMapping(path = "/createLecture/{courseId}")
    public ResponseEntity<Lecture> createLecture(@RequestHeader(authHeader) String token,
                                             @PathVariable int courseId,
                                             @RequestBody LectureInfo l) {
        if (!Authorization.authorize(token, Role.Teacher)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Optional<Course> courseOpt = coursesRepo
                .findById(courseId);

        if (courseOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Course course = courseOpt.get();

        Builder builder = new LectureBuilder();
        builder.setCourse(course);
        builder.setDate(l.getDate());
        builder.setNumberOfTimeslots(l.getNumberOfTimeslots());

        if (course.getYearOfStudy() == FIRST_YEAR) {
            Director.constructFirstYear(builder);
        } else if (course.getYearOfStudy() == SECOND_YEAR) {
            Director.constructSecondYear(builder);
        } else {
            builder.setMinNoStudents(l.getMinNoStudents());
        }

        Lecture lecture = builder.build();
        coursesRepo.saveAndFlush(course);
        lecturesRepo.saveAndFlush(lecture);
        return new ResponseEntity<>(lecture, HttpStatus.CREATED);
    }

    /**
     * Schedules the all lectures for the coming two weeks.
     *
     * @return A string with a message about the status of the request
     */
    @PostMapping(path = "/scheduleLecturesUntil")
    public ResponseEntity<String> scheduleLecturesUntil(@RequestBody LocalDate date,
                                                        @RequestHeader(authHeader) String token) {
        if (date.isBefore(LocalDate.now())) {
            return new ResponseEntity<String>("Please specify a date"
                    + " after the current date.", HttpStatus.BAD_REQUEST);
        }

        List<Lecture> lectures = lecturesRepo.findAll();

        lectures.removeIf(lecture -> lecture.getDate().isAfter(date));

        if (lectures.size() == 0) {
            return new ResponseEntity<String>("There are no lectures planned in "
                    + "the coming two weeks.", HttpStatus.BAD_REQUEST);
        }

        for (Lecture l : lectures) {
            String response = sendLecture(l, token);
            if (response == null) {
                return internalError;
            }
        }

        return new ResponseEntity<>("Lectures until " + date.toString()
                + " are scheduled!", HttpStatus.OK);
    }

    /**
     * This method schedules a lecture in a room using the lecture id.
     *
     * @param lectureId The id of the lecture, specified in the path
     * @return A string with info about the status of the request
     */
    @GetMapping(path = "/scheduleLecture/{lectureId}")
    public ResponseEntity<String> scheduleLecture(@PathVariable int lectureId,
                                                  @RequestHeader(authHeader) String token) {
        Optional<Lecture> lecture = lecturesRepo.findById(lectureId);
        if (lecture.isEmpty()) {
            return new ResponseEntity<>("There is no lecture with this id, "
                    + "please check if the id is correct.", HttpStatus.BAD_REQUEST);
        }

        Lecture l = lecture.get();
        String response = sendLecture(l, token);
        if (response == null) {
            return internalError;
        }

        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    /**
     * Schedules the all lectures for the coming two weeks.
     *
     * @return A string with a message about the status of the request
     */
    @GetMapping(path = "/scheduleForTwoWeeks")
    public ResponseEntity<String> scheduleForTwoWeeks(@RequestHeader(authHeader) String token) {
        ResponseEntity<String> s = scheduleLecturesUntil(LocalDate.now().plusWeeks(2), token);
        ResponseEntity<String> correctReturn = new ResponseEntity<String>("Lectures until "
                + LocalDate.now().plusWeeks(2).toString() + " are scheduled!", HttpStatus.OK);


        if (!s.equals(correctReturn)) {
            return internalError;
        }

        return s;
    }

    //Stop PMD complaining about the toReturn variable
    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private String sendLecture(Lecture l, String token) {
        String path = "scheduleLecture/" + l.getDate() + "/" + l.getNumberOfTimeslots()
                + "/" + l.getMinNoStudents() + "/" + l.getId() + "/"
                + l.getCourse().getYearOfStudy();


        String toReturn = null;
        try (CloseableHttpResponse response = Authorization.sendGetRequest(
                ROOM_SCHEDULE_SERVICE_PORT, path, token)) {
            assert (response != null);
            toReturn = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    public CoursesRepo getCoursesRepo() {
        return coursesRepo;
    }

    public void setCoursesRepo(CoursesRepo coursesRepo) {
        this.coursesRepo = coursesRepo;
    }

    public LecturesRepo getLecturesRepo() {
        return lecturesRepo;
    }

    public void setLecturesRepo(LecturesRepo lecturesRepo) {
        this.lecturesRepo = lecturesRepo;
    }
}
