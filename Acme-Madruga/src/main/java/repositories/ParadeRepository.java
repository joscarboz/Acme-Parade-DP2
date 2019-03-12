
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import domain.Parade;

@Repository
public interface ParadeRepository extends JpaRepository<Parade, Integer> {

	@Query("select p from Parade p where p.ticker LIKE %:keyword% or p.description LIKE %:keyword% or p.title LIKE %:keyword%")
	Collection<Parade> findByKeyword(@Param("keyword") String keyword);
	@Query("select b.parades from Brotherhood b where b.area.name like %:area%")
	Collection<Parade> findByArea(@Param("area") String area);
	@Query("select p from Parade p join p.requests r where r.member.id=?1")
	Collection<Parade> findByMemberId(int id);

	@Query("select b.parades from Brotherhood b where b.id=?1")
	Collection<Parade> findByBrotherhoodId(int id);

	@Query("select p from Parade p where p.moment >= :minimumDate and p.moment <= :maximumDate")
	Collection<Parade> findByDate(@Param("minimumDate") @DateTimeFormat(pattern = "dd/MM/yyyy") Date minimumDate, @Param("maximumDate") @DateTimeFormat(pattern = "dd/MM/yyyy") Date maximumDate);
}
