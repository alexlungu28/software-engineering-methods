package roomscheduler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import roomscheduler.entities.Rule;

@Repository
public interface RuleRepository extends JpaRepository<Rule, Integer> {
}
