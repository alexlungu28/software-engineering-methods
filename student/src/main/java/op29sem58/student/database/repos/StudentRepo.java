package op29sem58.student.database.repos;

import op29sem58.student.database.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Create's a Repository.
 */
@Repository
public interface StudentRepo extends JpaRepository<Student, String> {
    Student findByUserName(String userName);

}
