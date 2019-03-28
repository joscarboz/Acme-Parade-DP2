
package forms;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

public class SystemConfigForm {

	private String	name;
	private String	banner;
	private String	welcomeMessageEng;
	private String	welcomeMessageEsp;
	private String	phonePrefix;
	private String	positiveWords;
	private String	negativeWords;
	private String	spamWords;
	private int		finderMaxResults;
	private Double	finderCacheHours;
	private String	creditCardMakes;
	private int		VAT;
	private double	fareCharge;


	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	@NotBlank
	@URL
	public String getBanner() {
		return this.banner;
	}

	public void setBanner(final String banner) {
		this.banner = banner;
	}

	@NotBlank
	public String getWelcomeMessageEng() {
		return this.welcomeMessageEng;
	}

	public void setWelcomeMessageEng(final String welcomeMessageEng) {
		this.welcomeMessageEng = welcomeMessageEng;
	}

	@NotBlank
	public String getWelcomeMessageEsp() {
		return this.welcomeMessageEsp;
	}

	public void setWelcomeMessageEsp(final String welcomeMessageEsp) {
		this.welcomeMessageEsp = welcomeMessageEsp;
	}

	@NotBlank
	public String getPhonePrefix() {
		return this.phonePrefix;
	}

	public void setPhonePrefix(final String phonePrefix) {
		this.phonePrefix = phonePrefix;
	}

	public String getPositiveWords() {
		return this.positiveWords;
	}
	public void setPositiveWords(final String positiveWords) {
		this.positiveWords = positiveWords;
	}

	public String getNegativeWords() {
		return this.negativeWords;
	}
	public void setNegativeWords(final String negativeWords) {
		this.negativeWords = negativeWords;
	}

	public String getSpamWords() {
		return this.spamWords;
	}
	public void setSpamWords(final String spamWords) {
		this.spamWords = spamWords;
	}

	@Range(min = 1, max = 100)
	public int getFinderMaxResults() {
		return this.finderMaxResults;
	}

	public void setFinderMaxResults(final int finderMaxResults) {
		this.finderMaxResults = finderMaxResults;
	}

	@Range(min = 1, max = 24)
	public Double getFinderCacheHours() {
		return this.finderCacheHours;
	}

	public void setFinderCacheHours(final Double finderCacheHours) {
		this.finderCacheHours = finderCacheHours;
	}

	public String getCreditCardMakes() {
		return this.creditCardMakes;
	}

	public void setCreditCardMakes(final String creditCardMakes) {
		this.creditCardMakes = creditCardMakes;
	}

	public int getVAT() {
		return this.VAT;
	}

	public void setVAT(final int vAT) {
		this.VAT = vAT;
	}

	public double getFareCharge() {
		return this.fareCharge;
	}

	public void setFareCharge(final double fareCharge) {
		this.fareCharge = fareCharge;
	}

}
