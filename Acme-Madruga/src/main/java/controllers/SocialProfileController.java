
package controllers;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.SocialProfileService;
import domain.SocialProfile;

@Controller
@RequestMapping("/socialProfile")
public class SocialProfileController {

	@Autowired
	private SocialProfileService	socialProfileService;


	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		SocialProfile socialProfile;

		socialProfile = this.socialProfileService.create();
		result = this.createEditModelAndView(socialProfile);
		result.addObject("socialProfile", socialProfile);
		result.addObject("requestURI", "socialProfile/create.do");

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int socialProfileId) {
		ModelAndView result;
		SocialProfile socialProfile;

		socialProfile = this.socialProfileService.findOne(socialProfileId);
		Assert.notNull(socialProfile);
		result = this.createEditModelAndView(socialProfile);
		result.addObject("socialProfile", socialProfile);
		result.addObject("requestURI", "socialProfile/edit.do");

		return result;

	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final SocialProfile socialProfile, final BindingResult binding) {
		ModelAndView result;
		Collection<SocialProfile> socialProfiles;

		if (binding.hasErrors())
			result = this.createEditModelAndView(socialProfile);
		else
			try {
				this.socialProfileService.save(socialProfile);
				socialProfiles = this.socialProfileService.findByPincipal();
				result = new ModelAndView("redirect:/actor/display.do");
				result.addObject("socialProfiles", socialProfiles);

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(socialProfile, "socialProfile.commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int socialProfileId) {
		ModelAndView result;
		SocialProfile socialProfile;
		Collection<SocialProfile> socialProfiles;
	
		socialProfiles = this.socialProfileService.findByPincipal();

		socialProfile = this.socialProfileService.findOne(socialProfileId);
		this.socialProfileService.delete(socialProfile);

		result = new ModelAndView("redirect:/actor/display.do");
		result.addObject("socialProfiles", socialProfiles);

		return result;
	}

	// Ancillary Methods ------------------------------------------------------
	protected ModelAndView createEditModelAndView(final SocialProfile socialProfile) {
		ModelAndView result;

		result = this.createEditModelAndView(socialProfile, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final SocialProfile socialProfile, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("socialProfile/edit");
		result.addObject("socialProfile", socialProfile);
		result.addObject("message", messageCode);

		return result;
	}

}
