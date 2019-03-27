
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsor;

@Repository
public interface SponsorRepository extends JpaRepository<Sponsor, Integer> {

	@Query("select s from Sponsor s where s.userAccount.id = ?1")
	Sponsor findbyUserAccountID(int id);
	
	@Query("select avg(s.sponsorships.size), max(s.sponsorships.size), min(s.sponsorships.size),stddev(s.sponsorships.size) from Sponsor s join s.sponsorships sp where sp.active=true")
	Double[] activeSponsorshipPerSponsor();
	
	@Query("select distinct s from Sponsor s join s.sponsorships sp where sp.active=true order by s.sponsorships.size")
	Collection<Sponsor> findTopSponsorSponsorshipActive();
	
	
	
}
