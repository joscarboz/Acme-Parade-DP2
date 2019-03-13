package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Brotherhood;
import domain.History;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

	@Query("select avg(periodRecords.size+ legalRecords.size+ linkRecords.size+ miscellaneousRecords.size), min(periodRecords.size+ legalRecords.size+ linkRecords.size+ miscellaneousRecords.size), max(periodRecords.size+ legalRecords.size+ linkRecords.size+ miscellaneousRecords.size), stddev(periodRecords.size+ legalRecords.size+ linkRecords.size+ miscellaneousRecords.size) from History h")
	Double[] getRecordsPerHistory();

	// TODO probar como se devuelve esta query en Java, si no hacerlo separado
	@Query("select max(h.periodRecords.size+ h.legalRecords.size+ h.linkRecords.size+ h.miscellaneousRecords.size), b from Brotherhood b join b.history h")
	Collection<Object[]> largestHistory();

	@Query("select b from Brotherhood b join b.history h where (h.periodRecords.size+ h.legalRecords.size+ h.linkRecords.size+ h.miscellaneousRecords.size)> (select avg(periodRecords.size+ legalRecords.size+ linkRecords.size+ miscellaneousRecords.size)from History h)")
	Collection<Brotherhood> largerAverage();

}
