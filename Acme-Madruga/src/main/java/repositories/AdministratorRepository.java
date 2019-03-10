package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;
import domain.Area;
import domain.Position;
import domain.Procession;

@Repository
public interface AdministratorRepository extends
		JpaRepository<Administrator, Integer> {

	@Query("select avg(b.enrolments.size), max(b.enrolments.size), min(b.enrolments.size),sqrt(sum(b.enrolments.size*b.enrolments.size)/count(b.enrolments.size)-(avg(b.enrolments.size)*avg(b.enrolments.size))) from Brotherhood b group by 'b'")
	Double[] getMembersPerBrotherhood();

	@Query("select count(r)/(select COUNT(r) from Request r)*1.0 from Request r where r.status='REJECTED'")
	Double rejectedRequestRatio();

	@Query("select count(r)/(select COUNT(r) from Request r)*1.0 from Request r where r.status='APPROVED'")
	Double approvedRequestRatio();

	@Query("select count(r)/(select COUNT(r) from Request r)*1.0 from Request r where r.status='PENDING'")
	Double pendingRequestRatio();

	@Query("select p from Procession p where p.moment between CURRENT_DATE and CURRENT_DATE + 30")
	Collection<Procession> upcomingProcessions();

	@Query("select p from Position p, Enrolment e where e.position=p group by p")
	Collection<Position> positionHistogram();

	@Query("select count(p) from Position p, Enrolment e where e.position=p group by p")
	Collection<Long> longHistogram();

	@Query("select count(r)/(select COUNT(r) from Request r)*1.0 from Request r where r.status='REJECTED' and r.procession.id=?1")
	Double rejectedRequestRatioPerProcession(int id);

	@Query("select count(r)/(select COUNT(r) from Request r)*1.0 from Request r where r.status='APPROVED' and r.procession.id=?1")
	Double approvedRequestRatioPerProcession(int id);

	@Query("select count(r)/(select COUNT(r) from Request r)*1.0 from Request r where r.status='PENDING' and r.procession.id=?1")
	Double pendingRequestRatioPerProcession(int id);

	@Query("select count(r)/(select COUNT(r) from Request r)*1.0 from Request r where r.status='APPROVED' and r.member.id=?1")
	Double approvedRequestRatioPerMemberID(int id);

	@Query("select count(r)/(select COUNT(r) from Request r)*1.0 from Request r where r.member.id=?1")
	Double RequestRatioPerMemberID(int id);

	@Query("select a from Area a, Brotherhood b where b.area=a group by a")
	Collection<Area> BrotherhoodsPerArea();

	@Query("select count(a) from Area a, Brotherhood b where b.area=a group by a")
	Collection<Long> BrotherhoodsPerAreaNumber();
}
