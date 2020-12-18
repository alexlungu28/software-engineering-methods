package project.op29sem58.courses;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @GetMapping(path = "/getAllCourses")
    public @ResponseBody
    Iterable<Course> getAllCourses() {
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
        Optional<Course> courseOpt = coursesRepo
                .findById(courseId);

        if (courseOpt.isEmpty()) {
            return "Incorrect course id, there is no course with this id. \n"
                    + "Try /getAllCourses to check if your courseId is correct.";
        }
        Course course = courseOpt.get();

        Lecture lecture = new Lecture(
			course, l.getDate(), l.getNumberOfTimeslots(), l.getMinNoStudents()
		);

        coursesRepo.saveAndFlush(course);
        lecturesRepo.saveAndFlush(lecture);
        return "Lecture saved successfully!";
    }

    /**
     * Schedules the all lectures for the coming two weeks.
     *
     * @return A string with a message about the status of the request
     */
    @PostMapping(path = "/scheduleLecturesUntil")
    public @ResponseBody String scheduleLecturesUntil(@RequestBody LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            return "Please specify a date after the current date.";
        }

        List<Lecture> lectures = lecturesRepo.findAll();

        lectures.removeIf(lecture -> lecture.getDate().isAfter(date));

        if (lectures.size() == 0) {
            return "There are no lectures planned in the coming two weeks.";
        }

        for (Lecture l : lectures) {
            String path = ServerCommunication.getRoomScheduleServiceUrl() + "/scheduleLecture/"
                    + l.getDate() + "/" + l.getNumberOfTimeslots() + "/" + l.getMinNoStudents()
                    + "/" + l.getId() + "/" + l.getCourse().getYearOfStudy();
            String response = ServerCommunication.makeGetRequest(path);
            if (response == null) {
                return "Something went wrong on our end. Please try again later.";
            }
        }

        return "Lectures until " + date.toString() + " are scheduled!";
    }

    /**
     * This method schedules a lecture in a room using the lecture id.
     *
     * @param lectureId The id of the lecture, specified in the path
     * @return A string with info about the status of the request
     */
    @GetMapping(path = "/scheduleLecture/{lectureId}")
    public @ResponseBody String scheduleLecture(@PathVariable int lectureId) {
        Optional<Lecture> lecture = lecturesRepo.findById(lectureId);
        if (lecture.isEmpty()) {
            return "There is no lecture with this id, please check if the id is correct.";
        }

        Lecture l = lecture.get();
        String path = ServerCommunication.getRoomScheduleServiceUrl() + "/scheduleLecture/"
                + l.getDate() + "/" + l.getNumberOfTimeslots() + "/" + l.getMinNoStudents()
                + "/" + l.getId() + "/" + l.getCourse().getYearOfStudy();
        String response = ServerCommunication.makeGetRequest(path);
        if (response == null) {
            return "Something went wrong on our end. Please try again later.";
        }

        return response;
    }

    /**
     * Schedules the all lectures for the coming two weeks.
     *
     * @return A string with a message about the status of the request
     */
    @GetMapping(path = "/scheduleForTwoWeeks")
    public @ResponseBody String scheduleForTwoWeeks() {
        String s = scheduleLecturesUntil(LocalDate.now().plusWeeks(2));
        String correctReturn = "Lectures until " + LocalDate.now().plusWeeks(2).toString()
                + " are scheduled!";


        if (!s.equals(correctReturn)) {
            return "Something went wrong on our end, please try again later.";
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
