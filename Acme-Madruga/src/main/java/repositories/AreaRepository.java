
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Area;

@Repository
public interface AreaRepository extends JpaRepository<Area, Integer> {

	@Query("select a from Area a where a.name = ?1")
	Collection<Area> findByName(String name);

	@Query("select count(c)/(select count(a) from Area a)*1.0 from Chapter c join c.area")
	Double coordinateAreasRatio();
	
	@Query("select avg(b.parades.size), max(b.parades.size), min(b.parades.size),stddev(b.parades.size) from Brotherhood b")
	Double[] getParadesPerChapter();

	@Query("select b.area from Brotherhood b where b.parades.size> (select avg(br.parades.size)*1.1 from Brotherhood br)")
	Collection<Area> areaMAverage();

}
