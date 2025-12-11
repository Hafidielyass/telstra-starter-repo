package au.com.telstra.simcardactivator.repository;

import au.com.telstra.simcardactivator.entity.SimCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for SimCard entities.
 */
@Repository
public interface SimCardRepository extends JpaRepository<SimCard, Long> {
}
