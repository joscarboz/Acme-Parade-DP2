
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.PeriodRecord;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class PeriodRecordServiceTest extends AbstractTest {

	@Autowired
	private PeriodRecordService	periodRecordService;


	@Test
	public void createAndSavePeriodRecordDriver() {
		final Object testingData[][] = {
			{	//Creación correcta de un period record
				"brotherhood1", "title", "description", 1939, 1949, "https://www.photo.com", null
			}, {//Anonimo no puede crear un period record
				null, "title", "description", 1939, 1949, "https://www.photo.com", IllegalArgumentException.class
			}, {//Solo brotherhood puede crear un period record
				"member1", "title", "description", 1939, 1949, "https://www.photo.com", IllegalArgumentException.class
			}, {//El end year no puede ser anterior al start year
				"brotherhood1", "title", "description", 1959, 1949, "https://www.photo.com", IllegalArgumentException.class
			}, {//El title no puede ser nulo
				"brotherhood1", null, "description", 1939, 1949, "https://www.photo.com", ConstraintViolationException.class
			}, {//El end year no puede ser negativo
				"brotherhood1", "title", "description", 1939, -1949, "https://www.photo.com", IllegalArgumentException.class
			}, {//Una brotherhood sin inception record no puede tener un period record
				"brotherhood2", "title", "description", 1939, 1949, "https://www.photo.com", NullPointerException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSavePeriodRecordTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (Integer) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}
	@Test
	public void deleteDriver() {
		final Object testingData[][] = {
			{	//Una brotherhood elimina correctamente un period recod
				"brotherhood1", "periodRecord1", null
			}, { //Una brotherhood no puede eliminar un period record de otra
				"brotherhood2", "periodRecord1", IllegalArgumentException.class
			}, { //Sólo una brotherhood puede eliminar un period record
				"member1", "periodRecord1", IllegalArgumentException.class
			}, { //Un anónimo no puede eliminar un period record
				null, "periodRecord1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	protected void deleteTemplate(final String userName, final String periodRecordBeanName, final Class<?> expected) {
		Class<?> caught;
		int periodRecordId;
		PeriodRecord periodRecord;

		caught = null;
		try {
			this.authenticate(userName);
			periodRecordId = super.getEntityId(periodRecordBeanName);
			periodRecord = this.periodRecordService.findOne(periodRecordId);
			this.periodRecordService.delete(periodRecord);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
	protected void createAndSavePeriodRecordTemplate(final String userName, final String title, final String description, final Integer startYear, final Integer endYear, final String picture, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			final PeriodRecord periodRecord = this.periodRecordService.create();
			periodRecord.setDescription(description);
			periodRecord.setEndYear(endYear);
			periodRecord.setStartYear(startYear);
			periodRecord.setTitle(title);
			final List<String> pictures = new ArrayList<String>();
			pictures.add(picture);
			final Collection<String> photos = pictures;
			periodRecord.setPictures(photos);
			this.periodRecordService.save(periodRecord);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
