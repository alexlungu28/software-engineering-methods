package op29sem58.courses.database.repos;

import op29sem58.courses.database.entities.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturesRepo extends JpaRepository<Lecture, Integer> { }
