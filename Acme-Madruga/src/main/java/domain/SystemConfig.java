package domain;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

@Entity
@Access(AccessType.PROPERTY)
public class SystemConfig extends DomainEntity {

	private String name;
	private String banner;
	private String welcomeMessageEng;
	private String welcomeMessageEsp;
	private String phonePrefix;
	private Collection<String> positiveWords;
	private Collection<String> negativeWords;
	private Collection<String> spamWords;
	private int finderMaxResults;
	private Double finderCacheHours;
	private int VAT;
	private double fareCharge;
	private Collection<String> creditCardMakes;

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

	@ElementCollection(targetClass = String.class)
	public Collection<String> getPositiveWords() {
		return this.positiveWords;
	}

	public void setPositiveWords(final Collection<String> positiveWords) {
		this.positiveWords = positiveWords;
	}

	@ElementCollection(targetClass = String.class)
	public Collection<String> getNegativeWords() {
		return this.negativeWords;
	}

	public void setNegativeWords(final Collection<String> negativeWords) {
		this.negativeWords = negativeWords;
	}

	@ElementCollection(targetClass = String.class)
	public Collection<String> getSpamWords() {
		return this.spamWords;
	}

	public void setSpamWords(final Collection<String> spamWords) {
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

	public int getVAT() {
		return VAT;
	}

	public void setVAT(int vAT) {
		VAT = vAT;
	}

	public double getFareCharge() {
		return fareCharge;
	}

	public void setFareCharge(double fareCharge) {
		this.fareCharge = fareCharge;
	}

	@ElementCollection
	public Collection<String> getCreditCardMakes() {
		return creditCardMakes;
	}

	public void setCreditCardMakes(Collection<String> creditCardMakes) {
		this.creditCardMakes = creditCardMakes;
	}

}
