
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	@Query("select s from Sponsorship s where s.creditCard.expirationYear < ?1 or (s.creditCard.expirationYear = ?1 and s.creditCard.expirationMonth < ?2)")
	Collection<Sponsorship> findExpiredCreditCards(int expirationYear, int expirationMonth);
}
