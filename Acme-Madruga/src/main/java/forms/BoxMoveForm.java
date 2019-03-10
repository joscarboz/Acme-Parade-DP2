
package forms;

import org.hibernate.validator.constraints.NotBlank;

public class BoxMoveForm {

	private int		messageId;
	private String	title;


	public int getMessageId() {
		return this.messageId;
	}

	public void setMessageId(final int messageId) {
		this.messageId = messageId;
	}

	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

}
