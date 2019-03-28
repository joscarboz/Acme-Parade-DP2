
package services;

import java.util.ArrayList;
import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import domain.Area;
import domain.Chapter;
import domain.Proclaim;

import utilities.AbstractTest;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ChapterServiceTest extends AbstractTest {

	//SUT
	@Autowired
	private ChapterService	chapterService;
	
	@Autowired
	private AreaService	areaService;
	
	@Autowired
	private ProclaimService	proclaimService;

	// Tests ------------------------------------------------------------------

	@Test
	public void createAndSaveDriver() {
		
		final Object testingData[][] = {
			{	//Creación correcta de un Chapter
				"chapter1","area1","titulo","proclaim1", null
			},{	//Creación incorrecta de un Chapter area ya asignada
				"chapter1","area2","titulo","proclaim1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			try {
				super.startTransaction();
				this.createAndSaveTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
			} catch (final Throwable oops) {
				throw new RuntimeException(oops);
			} finally {
				super.rollbackTransaction();
			}
	}

	// Ancillary methods ------------------------------------------------------
	protected void createAndSaveTemplate(final String name, final String areaName, final String title, final String procalimName, final Class<?> expected) {
		Class<?> caught;
		Chapter chapter;
		Area area;
		Proclaim proclaim;
		Collection<Proclaim> proclaims = new ArrayList<>();
		final int chapterId;
		final int areaId;
		final int proclaimId;

		caught = null;
		try {
			chapterId = super.getEntityId(name);
			chapter = this.chapterService.findOne(chapterId);
			chapter.setArea(null);
			chapter.setTitle(title);
			chapter.setProclaims(proclaims);
			this.chapterService.save(chapter);
			this.chapterService.flush();
			areaId = super.getEntityId(areaName);
			area = this.areaService.findOne(areaId);
			chapter.setArea(area);
			proclaimId = super.getEntityId(procalimName);
			proclaim = this.proclaimService.findOne(proclaimId);
			proclaims.add(proclaim);
			chapter.setProclaims(proclaims);
			this.chapterService.save(chapter);
			this.chapterService.flush();

		} catch (final Throwable oops) {
			caught = oops.getClass();
		}

		this.checkExceptions(expected, caught);
	}
}
