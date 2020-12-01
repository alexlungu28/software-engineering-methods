package op29sem58.courses;

import op29sem58.courses.database.entities.Course;
import op29sem58.courses.database.entities.Lecture;
import op29sem58.courses.database.repos.CoursesRepo;
import op29sem58.courses.database.repos.LecturesRepo;
import org.apache.catalina.Server;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.apache.http.impl.client.CloseableHttpClient;


import java.io.Closeable;
import java.net.URI;
import java.net.http.HttpRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
    public @ResponseBody String createCourse(@RequestBody Course c){
        coursesRepo.saveAndFlush(c);
        return "Course saved successfully!";
    }

    @PostMapping(path = "/createLecture/{courseId}")
    public @ResponseBody String createCourse(@PathVariable int courseId, @RequestBody LectureInfo l){
        //TODO: actually save the lecture
        l.setCoursesId(courseId);
        Optional<Course> courseOpt = coursesRepo.findById(l.getCoursesId());

        if(courseOpt.isEmpty()){
            return "Incorrect course id, there is no course with this id. \n" +
                    "Try /getAllCourses to check if your courseId is correct.";
        }
        Course course = courseOpt.get();

        Lecture lecture = new Lecture(course, l.getDate(), l.getnSlots(), l.getMinNoStudents());

        lecturesRepo.saveAndFlush(lecture);
        return "Lecture saved successfully!";
    }

    //TODO: make a method that schedules lectures for the coming two weeks
    //path for schedulelecture is: 8081/scheduleLecture/{prefDate}/{numSlots}/{numOfStudents}/{lectureId}/{yearOfStudy}
    @GetMapping(path = "/scheduleForTwoWeeks")
    public @ResponseBody String scheduleForTwoWeeks(){
        List<Lecture> lectures = lecturesRepo.findAll();
        LocalDate now = LocalDate.now();
        LocalDate inTwoWeeks = now.plusWeeks(2);

        lectures.removeIf(lecture -> lecture.getDate().isAfter(inTwoWeeks));

        if(lectures.size() == 0){
            return "There are no lectures planned in the coming two weeks.";
        }

        for(Lecture l : lectures){
            String path = ServerCommunication.getRoomScheduleServiceUrl() + "/scheduleLecture/" + l.getDate() + "/" + l.getnSlots() + "/"
                    + l.getMinNoStudents() + "/" + l.getId() + "/" + l.getCourse().getYearOfStudy();
            System.out.println(path);
            String response = ServerCommunication.makeGetRequest(path);
            if(response == null){
                return "Something went wrong on our end. Please try again later.";
            }
        }

        return "Lectures for the coming two weeks are scheduled!";
    }

}
