
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;

@Repository
public interface BrotherhoodRepository extends JpaRepository<Brotherhood, Integer> {

	@Query("select b from Brotherhood b group by b.enrolments.size order by count(b.enrolments.size) desc")
	Collection<Brotherhood> findLargestBrotherhoods();

	@Query("select e.brotherhood from Enrolment e where e.member.id=?1")
	Collection<Brotherhood> findByMemberId(int id);

	@Query("select e.brotherhood from Enrolment e where e.member.id=?1 and e.position is not null")
	Collection<Brotherhood> findByActiveMemberId(int id);

	@Query("select b from Brotherhood b group by b.enrolments.size")
	Collection<Brotherhood> findSmallestBrotherhoods();

	@Query("select b from Brotherhood b where b.userAccount.id = ?1")
	Brotherhood findbyUserAccountID(int id);

	@Query("select b from Brotherhood b join b.parades p join p.requests r where r.id = ?1")
	Brotherhood findbyRequestId(int id);

	@Query("select b from Brotherhood b where b.area.id = ?1")
	Collection<Brotherhood> findByAreaId(int areaId);

}
