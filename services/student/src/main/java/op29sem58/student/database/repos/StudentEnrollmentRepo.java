package op29sem58.student.database.repos;

import op29sem58.student.database.entities.Student;
import op29sem58.student.database.entities.StudentEnrollment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentEnrollmentRepo extends JpaRepository<StudentEnrollment, Integer> {
	Optional<StudentEnrollment> findByCourseIdAndStudent(int courseId, Student student);
}
