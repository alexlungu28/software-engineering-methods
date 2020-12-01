package op29sem58.courses;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import op29sem58.courses.database.entities.Course;
import op29sem58.courses.database.entities.Lecture;
import op29sem58.courses.database.repos.CoursesRepo;
import op29sem58.courses.database.repos.LecturesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;




@Controller
public class CoursesController {

    @Autowired
    private CoursesRepo coursesRepo;
    @Autowired
    private LecturesRepo lecturesRepo;

    @GetMapping(path = "/getAllCourses")
    public @ResponseBody Iterable<Course> getAllCourses() {
        return coursesRepo.findAll();
    }

    @GetMapping(path = "/getAllLectures")
    public @ResponseBody Iterable<Lecture> getAllLectures() {
        return lecturesRepo.findAll();
    }

    @PostMapping(path = "/createCourse")
    public @ResponseBody String createCourse(@RequestBody Course c) {
        coursesRepo.saveAndFlush(c);
        return "Course saved successfully!";
    }

    /**
     * Creates a lecture for the course of CourseID.
     *
     * @param courseId the id of the course this lecture is for
     * @param l the lecture to be scheduled
     * @return a string with information about the status of the request
     */
    @PostMapping(path = "/createLecture/{courseId}")
    public @ResponseBody String createCourse(@PathVariable int courseId,
                                             @RequestBody LectureInfo l) {
        l.setCourseId(courseId);
        Optional<Course> courseOpt = coursesRepo
                .findById(l.getCourseId());

        if (courseOpt.isEmpty()) {
            return "Incorrect course id, there is no course with this id. \n"
                    + "Try /getAllCourses to check if your courseId is correct.";
        }
        Course course = courseOpt.get();

        Lecture lecture = new Lecture(course, l.getDate(), l.getNslots(), l.getMinNoStudents());

        lecturesRepo.saveAndFlush(lecture);
        return "Lecture saved successfully!";
    }

    /**
     * Schedules the all lectures for the coming two weeks.
     *
     * @return A string with a message about the status of the request
     */
    @GetMapping(path = "/scheduleForTwoWeeks")
    public @ResponseBody String scheduleForTwoWeeks() {
        List<Lecture> lectures = lecturesRepo.findAll();
        LocalDate inTwoWeeks = LocalDate.now().plusWeeks(2);

        lectures.removeIf(lecture -> lecture.getDate().isAfter(inTwoWeeks));

        if (lectures.size() == 0) {
            return "There are no lectures planned in the coming two weeks.";
        }

        for (Lecture l : lectures) {
            String path = ServerCommunication.getRoomScheduleServiceUrl() + "/scheduleLecture/"
                    + l.getDate() + "/" + l.getNslots() + "/" + l.getMinNoStudents()
                    + "/" + l.getId() + "/" + l.getCourse().getYearOfStudy();
            System.out.println(path);
            String response = ServerCommunication.makeGetRequest(path);
            if (response == null) {
                return "Something went wrong on our end. Please try again later.";
            }
        }

        return "Lectures for the coming two weeks are scheduled!";
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
