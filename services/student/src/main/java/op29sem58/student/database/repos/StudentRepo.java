package op29sem58.student.database.repos;

import java.util.List;
import op29sem58.student.database.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepo extends JpaRepository<Student, String> {
    List<Student> findByWantsToGoTrueOrderByLastVisitedAsc();
}