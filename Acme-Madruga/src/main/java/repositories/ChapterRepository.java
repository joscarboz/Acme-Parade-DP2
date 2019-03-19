package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Chapter;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Integer> {

	@Query("select c from Chapter c where c.area IN(select b.area from Brotherhood b where b.parades.size> (select avg(br.parades.size)*1.1 from Brotherhood br))")
	Collection<Chapter> chapterMoreAVG();

}
