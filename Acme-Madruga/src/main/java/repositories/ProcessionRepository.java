
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Repository;

import domain.Procession;

@Repository
public interface ProcessionRepository extends JpaRepository<Procession, Integer> {

	@Query("select p from Procession p where p.ticker LIKE %:keyword% or p.description LIKE %:keyword% or p.title LIKE %:keyword%")
	Collection<Procession> findByKeyword(@Param("keyword") String keyword);
	@Query("select b.processions from Brotherhood b where b.area.name like %:area%")
	Collection<Procession> findByArea(@Param("area") String area);
	@Query("select p from Procession p join p.requests r where r.member.id=?1")
	Collection<Procession> findByMemberId(int id);

	@Query("select b.processions from Brotherhood b where b.id=?1")
	Collection<Procession> findByBrotherhoodId(int id);

	@Query("select p from Procession p where p.moment >= :minimumDate and p.moment <= :maximumDate")
	Collection<Procession> findByDate(@Param("minimumDate") @DateTimeFormat(pattern = "dd/MM/yyyy") Date minimumDate, @Param("maximumDate") @DateTimeFormat(pattern = "dd/MM/yyyy") Date maximumDate);
}
