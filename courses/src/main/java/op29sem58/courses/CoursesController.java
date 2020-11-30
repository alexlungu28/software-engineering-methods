package op29sem58.courses;

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
    public @ResponseBody Iterable<Course> getAllCourses(){
        return coursesRepo.findAll();
    }

    @GetMapping(path = "/getAllLectures")
    public @ResponseBody Iterable<Lecture> getAllLectures(){
        return lecturesRepo.findAll();
    }

    @PostMapping(path = "/createCourse")
    public @ResponseBody String createCourse(@RequestBody CourseInfo c){
        coursesRepo.saveAndFlush(new Course(c.getTeacherNetId(), c.getName(), c.getCode(), c.getYearOfStudy()));
        return "Course saved succesfully";
    }

    @PostMapping(path = "/createLecture")
    public @ResponseBody String createCourse(@RequestBody LectureInfo l){
        return "Failed to create a new lecture";
    }
}
