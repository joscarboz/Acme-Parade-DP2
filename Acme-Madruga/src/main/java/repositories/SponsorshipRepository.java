
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

	@Query("select s from Sponsorship s where s.parade.id = ?1")
	Collection<Sponsorship> findByParadeId(int id);

	@Query("select s from Sponsorship s where s.parade.id = ?1 and s.active=1")
	Collection<Sponsorship> findActiveByParadeId(int id);

	@Query("select count(s)/ (select COUNT(sp) from Sponsorship sp)*1.0 from Sponsorship s where s.active=true")
	Double activeSponsorshipRatio();

	//@Query("select s from Sponsorship s where s.sponsor.id = ?1")
	//Collection<Sponsorship> findBySponsorId(int id);

}
