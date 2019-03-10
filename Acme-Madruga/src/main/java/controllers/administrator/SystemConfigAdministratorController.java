
package controllers.administrator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.SystemConfigService;
import domain.SystemConfig;
import forms.SystemConfigForm;

@Controller
@RequestMapping("/systemConfig/administrator")
public class SystemConfigAdministratorController {

	@Autowired
	private SystemConfigService	systemConfigService;


	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit() {
		ModelAndView result;
		final SystemConfigForm systemConfigForm = this.systemConfigService.toSystemConfigForm();

		result = this.createEditModelAndView(systemConfigForm);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView edit(@Valid final SystemConfigForm systemConfigForm, final BindingResult binding) {
		ModelAndView result;
		if (binding.hasErrors())
			result = this.createEditModelAndView(systemConfigForm);
		else
			try {
				final SystemConfig systemConfig = this.systemConfigService.reconstruct(systemConfigForm);

				this.systemConfigService.save(systemConfig);

				result = new ModelAndView("redirect:edit.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(systemConfigForm, "systemConfig.commit.error");
			}
		return result;
	}

	protected ModelAndView createEditModelAndView(final SystemConfigForm systemConfigForm) {
		return this.createEditModelAndView(systemConfigForm, null);
	}

	protected ModelAndView createEditModelAndView(final SystemConfigForm systemConfigForm, final String message) {
		ModelAndView result;

		result = new ModelAndView("systemConfig/edit");
		result.addObject("systemConfigForm", systemConfigForm);
		result.addObject("message", message);
		return result;
	}
}
