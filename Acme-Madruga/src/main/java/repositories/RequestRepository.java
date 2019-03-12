
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {

	@Query("select r from Parade p join p.requests r where p.id=?1 and r.status = '?2'")
	Collection<Request> findByParadeAndStatus(int id, String status);

	@Query("select count(r) from Request r where r.member.id=?1")
	int countByMember(int memberId);

	@Query("select count(r) from Request r where r.member.id=?1 and r.status='APPROVED'")
	int countAcceptedByMember(int memberId);

	@Query("select r from Request r where r.member.id = ?1")
	Collection<Request> findByMemberId(int id);

	@Query("select p.requests from Brotherhood b join b.parades p where b.id = ?1")
	Collection<Request> findByBrotherhoodId(int id);

}
