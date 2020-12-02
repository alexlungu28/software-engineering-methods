package project.op29sem58.courses.database.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.op29sem58.courses.database.entities.Course;

@Repository
public interface CoursesRepo extends JpaRepository<Course, Integer> { }
