
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import utilities.AbstractTest;
import domain.InceptionRecord;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class InceptionRecordServiceTest extends AbstractTest {

	//SUT
	@Autowired
	private InceptionRecordService	inceptionRecordService;


	@Test
	public void createAndSaveInceptionRecordDriver() {
		final Object testingData[][] = {
			{	//Creación correcta de una history con su inception record
				"brotherhood2", null
			}, {//Anonimo no puede crear una history con su inception record
				null, IllegalArgumentException.class
			}, {//Solo brotherhood puede crear una history con su inception record
				"member1", IllegalArgumentException.class
			}, {//Un brotherhood solo puede tener una history con su inception record
				"brotherhood1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveInceptionRecordTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	@Test
	public void editDriver() {
		final Object testingData[][] = {
			{	//Una brotherhood edita correctamente su inception
				"brotherhood1", "inceptionRecord1", null
			}, { //Una brotherhood no puede editar el inception de otra
				"brotherhood1", "inceptionRecord2", IllegalArgumentException.class
			}, { //Sólo una brotherhood puede editar un inception record
				"member1", "inceptionRecord1", IllegalArgumentException.class
			}, { //Un anónimo no puede editar un inception record
				null, "inceptionRecord1", IllegalArgumentException.class
			}

		};

		for (int i = 0; i < testingData.length; i++)
			this.editTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	//Ancillary method

	protected void createAndSaveInceptionRecordTemplate(final String userName, final Class<?> expected) {
		Class<?> caught;

		caught = null;
		try {
			this.authenticate(userName);
			final InceptionRecord inceptionRecord = this.inceptionRecordService.create();
			inceptionRecord.setDescription("description");
			inceptionRecord.setTitle("title");
			final String photo1 = "https://www.photo.com";
			final List<String> pictures = new ArrayList<String>();
			pictures.add(photo1);
			final Collection<String> photos = pictures;
			inceptionRecord.setPictures(photos);
			this.inceptionRecordService.save(inceptionRecord);

			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}

	protected void editTemplate(final String userName, final String inceptionRecordBeanName, final Class<?> expected) {
		Class<?> caught;
		int inceptionRecordId;
		InceptionRecord inceptionRecord;

		caught = null;
		try {
			this.authenticate(userName);
			inceptionRecordId = super.getEntityId(inceptionRecordBeanName);
			inceptionRecord = this.inceptionRecordService.findOne(inceptionRecordId);
			inceptionRecord.setTitle("modified title");
			this.inceptionRecordService.save(inceptionRecord);
			this.unauthenticate();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
