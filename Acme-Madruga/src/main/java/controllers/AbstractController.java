/*
 * AbstractController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import security.Authority;
import services.ActorService;
import services.BrotherhoodService;
import services.ChapterService;
import services.SystemConfigService;
import domain.SystemConfig;

@Controller
@ControllerAdvice
public class AbstractController {

	// Panic handler ----------------------------------------------------------

	@Autowired
	private SystemConfigService systemConfigService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private BrotherhoodService brotherhoodService;

	@Autowired
	private ChapterService chapterService;

	@ExceptionHandler(Throwable.class)
	public ModelAndView panic(final Throwable oops) {
		ModelAndView result;

		result = new ModelAndView("misc/panic");
		result.addObject("name", ClassUtils.getShortName(oops.getClass()));
		result.addObject("exception", oops.getMessage());
		result.addObject("stackTrace", ExceptionUtils.getStackTrace(oops));

		return result;
	}

	@ModelAttribute("bannerUrl")
	public String getBanner() {
		String result;
		final SystemConfig systemConfig = this.systemConfigService.findSystemConfiguration();
		result = systemConfig.getBanner();
		return result;
	}

	@ModelAttribute("isAreaSet")
	public Boolean areaSelected() {
				
		Boolean isLogged;
		Authentication authentication;
		SecurityContext context;
		Object principal;
		context = SecurityContextHolder.getContext();
		authentication = context.getAuthentication();
		context = SecurityContextHolder.getContext();
		authentication = context.getAuthentication();
		principal = authentication.getPrincipal();
		isLogged = principal.equals("anonymousUser");
		
		Boolean isAreaSet = true;
		Authority authB = new Authority();
		Authority authC = new Authority();
		authB.setAuthority(Authority.BROTHERHOOD);
		authC.setAuthority(Authority.CHAPTER);
		

		if(!isLogged){
			if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(authB)) {
				if(this.brotherhoodService.findByPrincipal().getArea().getName().equals("defaultArea"))
					isAreaSet = false;
			}
		
			if (this.actorService.findByPrincipal().getUserAccount().getAuthorities().contains(authC)) {
				if(this.chapterService.findByPrincipal().getArea() == null)
					isAreaSet = false;
			}
		}
		return isAreaSet;
	}

}
