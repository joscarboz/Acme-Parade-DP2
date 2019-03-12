
package controllers.member;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import repositories.FinderRepository;
import services.FinderService;
import services.MemberService;
import services.SystemConfigService;
import controllers.AbstractController;
import domain.Finder;
import domain.Member;
import domain.Parade;

@Controller
@RequestMapping("/finder/member")
public class FinderMemberController extends AbstractController {

	@Autowired
	private MemberService		memberService;

	@Autowired
	private FinderService		finderService;

	@Autowired
	private SystemConfigService	systemConfigService;

	@Autowired
	private FinderRepository	finderRepository;


	//Constructor
	public FinderMemberController() {
		super();
	}

	// ------------------- Finder ------------------------------
	//Edit

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView create() {
		final ModelAndView result;
		final Member member = this.memberService.findByPrincipal();

		Finder finder = this.finderService.findByMember(member);
		if (finder == null) {
			finder = this.finderService.create();
			finder.setFinderTime(new Date());
			finder.setMember(member);
		}
		final Date date = new Date();
		final Date finderDate = finder.getFinderTime();
		final long currentTime = date.getTime();
		final long finderTime = finderDate.getTime();
		final long diff = currentTime - finderTime;
		if (diff > this.systemConfigService.findSystemConfiguration().getFinderCacheHours() * 3600000) {
			finder.getResult().clear();
			this.finderService.save(finder);
		}

		result = new ModelAndView("finder/member/edit");
		result.addObject("finder", finder);
		result.addObject("requestURI", "finder/member/edit.do");
		result.addObject("parades", finder.getResult());
		return result;

	}

	//Save
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Finder finder, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(finder);
		else
			try {
				this.finderService.save(finder);
				this.finderService.flush();
				result = new ModelAndView("redirect:/finder/member/edit.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(finder, "finder.commit.error");
				oops.printStackTrace();
			}
		return result;
	}

	//Clean

	@RequestMapping(value = "/clean", method = RequestMethod.GET)
	public ModelAndView clean() {
		final Member member = this.memberService.findByPrincipal();

		Finder finder = this.finderService.findByMember(member);
		if (finder != null) {
			this.finderService.delete(finder);
			finder = this.finderService.create();
			this.finderService.save(finder);
		}
		return new ModelAndView("redirect:/finder/member/edit.do");

	}

	//Arcillary methods

	protected ModelAndView createEditModelAndView(final Finder finder) {
		ModelAndView result;

		result = this.createEditModelAndView(finder, null);

		return result;
	}
	protected ModelAndView createEditModelAndView(final Finder finder, final String messageCode) {
		ModelAndView result;
		final Collection<Parade> parades = this.finderService.findOne(finder.getId()).getResult();

		result = new ModelAndView("finder/member/edit");
		result.addObject("finder", finder);
		result.addObject("message", messageCode);
		result.addObject("fixUpTasks", parades);

		return result;
	}

}
