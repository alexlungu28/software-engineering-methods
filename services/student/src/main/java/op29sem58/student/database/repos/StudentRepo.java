package op29sem58.student.database.repos;

import java.util.List;
import op29sem58.student.database.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This is some basic code to let hibernate know we want a repository for the Student, and that we can retrieve
 * them. Specifically we can find students by the fact the boolean wantsToGo is true and sort the list by last visited.
 * Hibernates does the magic of creating a query for it.
 */
@Repository
public interface StudentRepo extends JpaRepository<Student, String> {
    List<Student> findByWantsToGoTrueOrderByLastVisitedAsc();
}
