package op29sem58.student.database.repos;

import java.util.Optional;
import op29sem58.student.database.entities.Student;
import op29sem58.student.database.entities.StudentEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is some basic code to let hibernate know we want a repository
 * for the StudentEnrollments, and that we can retrieve them.
 * Specifically we can find an enrollment by providing the course ID
 * and Student. Hibernates does the magic of creating a query for it.
 */
@Repository
public interface StudentEnrollmentRepo extends JpaRepository<StudentEnrollment, Integer> {
    Optional<StudentEnrollment> findByCourseIdAndStudent(int courseId, Student student);
}
