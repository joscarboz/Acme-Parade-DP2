
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.InceptionRecord;

@Repository
public interface InceptionRecordRepository extends JpaRepository<InceptionRecord, Integer> {

	@Query("select i from InceptionRecordRecord i where i.id=?1")
	InceptionRecord findOne(int id);
}
