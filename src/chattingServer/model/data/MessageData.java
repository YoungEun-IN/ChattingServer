package chattingServer.model.data;

import java.io.Serializable;
import java.util.Date;

/**
 * �޽��� ������ �����ϴ� Ŭ����
 */
public class MessageData implements Comparable<MessageData>, Serializable {
	private static final long serialVersionUID = 1L;
	/** message */
	private String message;
	/** timeStamp */
	private final Date timeStamp;

	/**
	 * ������ �Ķ���Ϳ� �ٰ� �� ������Ʈ�� �����ϴ� ������
	 * 
	 * @param message
	 * @param timeStamp
	 */
	public MessageData(final String message, final Date timeStamp) {
		this.message = message;
		this.timeStamp = timeStamp;
	}

	/**
	 * �޽��� ������ ��ȯ
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Ÿ�� �������� ��ȯ
	 * 
	 * @return timeStamp
	 */
	public Date getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Ŭ���̾�Ʈ�� ���� �� ǥ�� ���� ����ڿ��� �г����� �߰��ϴ� �� �ʿ��� �޽����� ������ �����ϴ� �޼ҵ�
	 * 
	 * @param newMessage
	 */
	public void setUserMessage(final String newMessage) {
		message = newMessage;
	}

	/**
	 * �޽����� ���� �� ���ִ� �޼ҵ�
	 */
	public int compareTo(final MessageData o) {
		return getTimeStamp().compareTo(o.getTimeStamp());
	}
}
