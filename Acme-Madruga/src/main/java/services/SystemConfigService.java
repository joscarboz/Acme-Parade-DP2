
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SystemConfigRepository;
import domain.SystemConfig;
import forms.SystemConfigForm;

@Service
@Transactional
public class SystemConfigService {

	// Managed Repository
	@Autowired
	private SystemConfigRepository	systemConfigRepository;


	// Constructor
	public SystemConfigService() {
		super();
	}

	// CRUD

	public SystemConfig findSystemConfiguration() {
		SystemConfig result = null;
		result = this.systemConfigRepository.findAll().get(0);
		return result;
	}

	public SystemConfig save(final SystemConfig systemConfig) {
		Assert.notNull(systemConfig);
		Assert.isTrue(systemConfig.getId() != 0);
		SystemConfig result;
		//Assert.isTrue(LoginService.getPrincipal().getAuthorities().contains(Authority.ADMIN));
		result = this.systemConfigRepository.save(systemConfig);

		return result;
	}

	public void delete(final SystemConfig systemConfig) {
		Assert.notNull(systemConfig);
		Assert.isTrue(systemConfig.getId() != 0);
		Assert.isTrue(this.systemConfigRepository.exists(systemConfig.getId()));

		this.systemConfigRepository.delete(systemConfig);
	}

	public Collection<SystemConfig> findAll() {
		Collection<SystemConfig> result;

		result = this.systemConfigRepository.findAll();

		return result;
	}

	@SuppressWarnings("deprecation")
	public String generateTicker() {
		String result, month, year, day, letters;
		SystemConfig systemConfig;
		Date today;

		systemConfig = this.findSystemConfiguration();
		today = new Date();

		year = String.valueOf(today.getYear() - 100);
		if (today.getMonth() < 10)
			month = "0" + String.valueOf(today.getMonth() + 1);
		else
			month = String.valueOf(today.getMonth() + 1);
		if (today.getDate() < 10)
			day = "0" + String.valueOf(today.getDate());
		else
			day = String.valueOf(today.getDate());

		//Generate letters
		letters = RandomStringUtils.randomAlphabetic(5).toUpperCase();

		result = year + month + day + "-" + letters;
		this.save(systemConfig);
		return result;
	}

	public SystemConfigForm toSystemConfigForm() {
		final SystemConfig systemConfig = this.findSystemConfiguration();
		final SystemConfigForm result = new SystemConfigForm();
		result.setBanner(systemConfig.getBanner());
		result.setFinderCacheHours(systemConfig.getFinderCacheHours());
		result.setFinderMaxResults(systemConfig.getFinderMaxResults());
		result.setName(systemConfig.getName());
		result.setPhonePrefix(systemConfig.getPhonePrefix());
		result.setWelcomeMessageEng(systemConfig.getWelcomeMessageEng());
		result.setWelcomeMessageEsp(systemConfig.getWelcomeMessageEsp());
		result.setVAT(systemConfig.getVAT());
		result.setFareCharge(systemConfig.getFareCharge());

		String spamWords = "";
		String positiveWords = "";
		String negativeWords = "";
		String creditCardMakes = "";
		for (final String s : systemConfig.getSpamWords())
			if (spamWords == "")
				spamWords = s;
			else
				spamWords = spamWords + ", " + s;

		for (final String s : systemConfig.getPositiveWords())
			if (positiveWords == "")
				positiveWords = s;
			else
				positiveWords = positiveWords + ", " + s;

		for (final String s : systemConfig.getNegativeWords())
			if (negativeWords == "")
				negativeWords = s;
			else
				negativeWords = negativeWords + ", " + s;

		for (final String s : systemConfig.getCreditCardMakes())
			if (creditCardMakes == "")
				creditCardMakes = s;
			else
				creditCardMakes = creditCardMakes + ", " + s;
		result.setSpamWords(spamWords);
		result.setPositiveWords(positiveWords);
		result.setNegativeWords(negativeWords);
		result.setCreditCardMakes(creditCardMakes);
		return result;
	}

	public SystemConfig reconstruct(final SystemConfigForm systemConfigForm) {
		final SystemConfig result = this.findSystemConfiguration();

		result.setBanner(systemConfigForm.getBanner());
		result.setFinderCacheHours(systemConfigForm.getFinderCacheHours());
		result.setFinderMaxResults(systemConfigForm.getFinderMaxResults());
		result.setName(systemConfigForm.getName());
		result.setPhonePrefix(systemConfigForm.getPhonePrefix());
		result.setWelcomeMessageEng(systemConfigForm.getWelcomeMessageEng());
		result.setWelcomeMessageEsp(systemConfigForm.getWelcomeMessageEsp());
		result.setFareCharge(systemConfigForm.getFareCharge());
		result.setVAT(systemConfigForm.getVAT());

		final Set<String> negativeWords = new HashSet<String>();
		final Set<String> positiveWords = new HashSet<String>();
		final Set<String> spamWords = new HashSet<String>();
		final Set<String> creditCardMakes = new HashSet<String>();

		final String[] negwords = systemConfigForm.getNegativeWords().split(",");
		for (final String string : negwords)
			negativeWords.add(string.trim());

		final String[] poswords = systemConfigForm.getPositiveWords().split(",");
		for (final String string : poswords)
			positiveWords.add(string.trim());

		final String[] spwords = systemConfigForm.getSpamWords().split(",");
		for (final String string : spwords)
			spamWords.add(string.trim());

		final String[] cCardMakes = systemConfigForm.getCreditCardMakes().split(",");
		for (final String string : cCardMakes)
			creditCardMakes.add(string.trim());

		result.setNegativeWords(negativeWords);
		result.setPositiveWords(positiveWords);
		result.setSpamWords(spamWords);
		result.setCreditCardMakes(creditCardMakes);
		return result;
	}
	public void flush() {
		this.systemConfigRepository.flush();
	}
}
