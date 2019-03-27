
package forms;

import java.util.Collection;
import java.util.Date;

import javax.persistence.ElementCollection;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

public class EditBrotherhoodForm {

	private String				name;
	private String				middleName;
	private String				surname;
	private String				photo;
	private String				email;
	private String				address;
	private String				phone;
	private String				title;
	private Date				establishment;
	private Collection<String>	pictures;

	@NotBlank
	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(final String middleName) {
		this.middleName = middleName;
	}

	@NotBlank
	public String getSurname() {
		return this.surname;
	}

	public void setSurname(final String surname) {
		this.surname = surname;
	}

	@URL
	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(final String photo) {
		this.photo = photo;
	}

	@Pattern(regexp = "^((([A-z0-9_-]+(?:\\.[A-z0-9_-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]*[a-z0-9]))|([A-z]+ [<]([A-z0-9_-]+(?:\\.[A-z0-9_-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9][a-z0-9-]*[a-z0-9])[>])){1}$")
	public String getEmail() {
		return this.email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}
	
	public String getAddress() {
		return this.address;
	}

	public void setAddress(final String address) {
		this.address = address;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(final String phone) {
		this.phone = phone;
	}
	
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	@Past
	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getEstablishment() {
		return this.establishment;
	}

	public void setEstablishment(final Date establishment) {
		this.establishment = establishment;
	}

	@ElementCollection
	public Collection<String> getPictures() {
		return this.pictures;
	}
	public void setPictures(final Collection<String> pictures) {
		this.pictures = pictures;
	}
	
}
