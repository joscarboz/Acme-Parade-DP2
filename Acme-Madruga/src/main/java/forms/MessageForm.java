
package forms;

import org.hibernate.validator.constraints.NotBlank;

public class MessageForm {

	private String	subject;
	private String	body;
	private String	priority;
	private String	tags;
	private String	recipient;


	@NotBlank
	public String getSubject() {
		return this.subject;
	}
	public void setSubject(final String subject) {
		this.subject = subject;
	}
	@NotBlank
	public String getBody() {
		return this.body;
	}
	public void setBody(final String body) {
		this.body = body;
	}

	@NotBlank
	public String getPriority() {
		return this.priority;
	}
	public void setPriority(final String priority) {
		this.priority = priority;
	}
	public String getTags() {
		return this.tags;
	}
	public void setTags(final String tags) {
		this.tags = tags;
	}
	public String getRecipient() {
		return this.recipient;
	}
	public void setRecipient(final String recipient) {
		this.recipient = recipient;
	}

}
