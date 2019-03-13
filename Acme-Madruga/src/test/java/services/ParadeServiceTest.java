
package services;

import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Parade;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ParadeServiceTest extends AbstractTest {

	//SUT
	@Autowired
	private ParadeService	paradeService;


	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Creaci�n correcta de una parade
				"brotherhood1", "sampleTitle", "sampleDescription", new Date(System.currentTimeMillis() + 10000), true, "sampleStatus", null
			}, {//Anonimo no puede crear una parade
				null, "sampleTitle", "sampleDescription", new Date(System.currentTimeMillis() + 10000), true, "sampleStatus", IllegalArgumentException.class
			}, {//Solo brotherhood puede crear una parade
				"member1", "sampleTitle", "sampleDescription", new Date(System.currentTimeMillis() + 10000), true, "sampleStatus", IllegalArgumentException.class
			}, { // Title vac�o
				"brotherhood1", "", "sampleDescription", new Date(System.currentTimeMillis() + 10000), true, "sampleStatus", ConstraintViolationException.class
			}, { // Description vac�o
				"brotherhood1", "sampleTitle", "", new Date(System.currentTimeMillis() + 10000), true, "sampleStatus", ConstraintViolationException.class
			}, { // Moment pasado
				"brotherhood1", "sampleTitle", "sampleDescription", new Date(System.currentTimeMillis() - 10000), true, "sampleStatus", ConstraintViolationException.class
			}, { // Draft mode null
				"brotherhood1", "sampleTitle", "sampleDescription", new Date(System.currentTimeMillis() + 10000), null, "sampleStatus", ConstraintViolationException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Date) testingData[i][3], (boolean) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void deleteDriver() {
		final Object testingData[][] = {
			{	//Una brotherhood elimina correctamente una parade
				"brotherhood1", "parade2", null
			}, { //Una brotherhood no puede eliminar la parade de otra
				"brotherhood1", "parade3", IllegalArgumentException.class
			}, { //S�lo una brotherhood puede eliminar una parade
				"member1", "parade2", IllegalArgumentException.class
			}, { //Un an�nimo no puede eliminar una parade
				null, "parade2", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String userName, final String title, final String description, final Date moment, final boolean draftMode, final String status, final Class<?> expected) {
		Class<?> caught;
		Parade parade;

		caught = null;
		try {
			this.authenticate(userName);
			parade = this.paradeService.create();
			parade.setTitle(title);
			parade.setDescription(description);
			parade.setMoment(moment);
			parade.setDraftMode(draftMode);
			parade.setStatus(status);
			this.paradeService.save(parade);
			this.paradeService.flush();
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void deleteTemplate(final String userName, final String paradeBeanName, final Class<?> expected) {
		Class<?> caught;
		int paradeId;
		Parade parade;

		caught = null;
		try {
			this.authenticate(userName);
			paradeId = super.getEntityId(paradeBeanName);
			parade = this.paradeService.findOne(paradeId);
			this.paradeService.delete(parade);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
