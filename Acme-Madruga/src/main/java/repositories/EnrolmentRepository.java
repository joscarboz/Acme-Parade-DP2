
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Enrolment;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment, Integer> {

	@Query("select e from Enrolment e where e.member.id=?1 and e.position is not null")
	Collection<Enrolment> activeEnrolments(int memberId);

	@Query("select e from Enrolment e where e.brotherhood.id=?1 and e.position is not null")
	Collection<Enrolment> activeEnrolmentsBrotherhood(int brotherhoodId);

	@Query("select e from Enrolment e where e.brotherhood.id=?1 and e.position is null")
	Collection<Enrolment> pendingEnrolmentsBrotherhood(int brotherhoodId);

	@Query("select e from Enrolment e where (e.dropOutDate is not null and e.brotherhood.id=?1)")
	Collection<Enrolment> dropOutEnrolmentsBrotherhood(int brotherhoodId);

	@Query("select e from Enrolment e where (e.dropOutDate is not null and e.member.id=?1)")
	Collection<Enrolment> dropOutEnrolmentsMember(int memberId);
}
