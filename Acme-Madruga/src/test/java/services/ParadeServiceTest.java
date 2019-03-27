
package services;

import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.Float;
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

	@Autowired
	private FloatService	floatService;


	@Test
	public void createAndSaveDriver() {
		final Object testingData[][] = {
			{	//Creación correcta de una parade
				"brotherhood1", "sampleTitle", "sampleDescription", new Date(System.currentTimeMillis() + 10000), true, "sampleStatus", null
			}, {//Anonimo no puede crear una parade
				null, "sampleTitle", "sampleDescription", new Date(System.currentTimeMillis() + 10000), true, "sampleStatus", IllegalArgumentException.class
			}, {//Solo brotherhood puede crear una parade
				"member1", "sampleTitle", "sampleDescription", new Date(System.currentTimeMillis() + 10000), true, "sampleStatus", IllegalArgumentException.class
			}, { // Title vacío
				"brotherhood1", "", "sampleDescription", new Date(System.currentTimeMillis() + 10000), true, "sampleStatus", ConstraintViolationException.class
			}, { // Moment pasado
				"brotherhood1", "sampleTitle", "sampleDescription", new Date(System.currentTimeMillis() - 10000), true, "sampleStatus", IllegalArgumentException.class
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
				"brotherhood1", "parade1", null
			}, { //Una brotherhood no puede eliminar la parade de otra
				"brotherhood1", "parade3", IllegalArgumentException.class
			}, { //Sólo una brotherhood puede eliminar una parade
				"member1", "parade1", IllegalArgumentException.class
			}, { //Un anónimo no puede eliminar una parade
				null, "parade1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	@Test
	public void acceptDriver() {
		final Object testingData[][] = {
			{	//Un chapter acepta una parade
				"chapter1", "parade2", null
			}, { //Un chapter no puede aceptar una parade de otro area
				"chapter2", "parade2", IllegalArgumentException.class
			}, { //Un chapter sin area no puede aceptar una parade
				"chapter3", "parade2", NullPointerException.class
			}, { //Un anónimo no puede aceptar una parade
				null, "parade2", IllegalArgumentException.class
			}, { //Un member no puede aceptar una parade
				"member1", "parade2", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.acceptTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}
	@Test
	public void rejectDriver() {
		final Object testingData[][] = {
			{	//Un chapter rechaza una parade
				"chapter1", "parade2", "rejected", null
			}, { //Un chapter no puede rechazar una parade de otro area
				"chapter2", "parade2", "rejected", IllegalArgumentException.class
			}, { //Un chapter sin area no puede rechazar una parade
				"chapter3", "parade2", "rejected", NullPointerException.class
			}, { //Un anónimo no puede rechazar una parade
				null, "parade2", "rejected", IllegalArgumentException.class
			}, { //Un member no puede rechazar una parade
				"member1", "parade2", "rechazar", IllegalArgumentException.class
			}, { //No se puede rechazar una parade sin razon
				"member1", "parade2", "", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.rejectTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String userName, final String title, final String description, final Date moment, final boolean draftMode, final String status, final Class<?> expected) {
		Class<?> caught;
		Parade parade;

		caught = null;
		try {
			this.authenticate(userName);
			parade = this.paradeService.create();

			final Float floatt = this.floatService.create();
			floatt.setTitle("sampleTitle");
			floatt.setDescription("sampleDescription");
			final Float res = this.floatService.save(floatt);

			final Collection<Float> floats = parade.getFloats();
			floats.add(res);

			parade.setFloats(floats);
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
	protected void acceptTemplate(final String userName, final String paradeBeanName, final Class<?> expected) {
		Class<?> caught;
		int paradeId;
		Parade parade;

		caught = null;
		try {
			this.authenticate(userName);
			paradeId = super.getEntityId(paradeBeanName);
			parade = this.paradeService.findOne(paradeId);
			parade.setStatus("accepted");
			this.paradeService.save(parade);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void rejectTemplate(final String userName, final String paradeBeanName, final String rejectReason, final Class<?> expected) {
		Class<?> caught;
		int paradeId;
		Parade parade;

		caught = null;
		try {
			this.authenticate(userName);
			paradeId = super.getEntityId(paradeBeanName);
			parade = this.paradeService.findOne(paradeId);
			parade.setStatus("rejected");
			parade.setRejectionReason(rejectReason);
			this.paradeService.save(parade);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

}
