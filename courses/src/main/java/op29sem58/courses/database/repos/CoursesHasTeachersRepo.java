package op29sem58.courses.database.repos;

import op29sem58.courses.database.entities.CourseHasTeachers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoursesHasTeachersRepo extends JpaRepository<CourseHasTeachers, Integer>{
}
