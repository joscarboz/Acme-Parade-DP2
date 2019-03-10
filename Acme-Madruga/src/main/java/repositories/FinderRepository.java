
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Finder;

@Repository
public interface FinderRepository extends JpaRepository<Finder, Integer> {

	@Query("select min(f.result.size),max(f.result.size), avg(f.result.size), stddev(f.result.size) from Finder f")
	public Double[] getNumberResultFinders();

	@Query("select f from Finder f where f.result is empty")
	public Double countEmptyFinders();

	@Query("select f from Finder f where f.member.id = ?1")
	Finder findByMemberID(int id);

}
