package op29sem58.courses;

import op29sem58.courses.database.repos.CoursesHasTeachersRepo;
import op29sem58.courses.database.repos.CoursesRepo;
import op29sem58.courses.database.repos.LecturesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CoursesController {

    private CoursesRepo coursesRepo;
    private LecturesRepo lecturesRepo;
    private CoursesHasTeachersRepo coursesHasTeachersRepo;

}
