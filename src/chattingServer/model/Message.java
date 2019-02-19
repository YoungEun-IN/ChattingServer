package chattingServer.model;

import java.util.Date;

/**
 * ������� ��ü ��ȭ�� ������ �޽����� ���� �κ��� ��Ÿ���� Ŭ����
 */
class Message implements Comparable<Message> {
	/** message */
	private final String message;
	/** timeStamp */
	private final Date timeStamp;

	/**
	 * ����� Ÿ�� �������� ������� �޽����� �����ϴ� ������
	 * 
	 * @param message
	 * @param timestamp
	 */
	public Message(final String message, final Date timestamp) {
		this.message = message;
		this.timeStamp = timestamp;
	}

	/**
	 * Ư�� �޽����� ������ ��ȯ
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Ÿ�� �������� ����
	 * 
	 * @return timeStamp
	 */
	public Date getDate() {
		return timeStamp;
	}

	/**
	 * �޽����� ���� �� ���ִ� �޼ҵ�
	 */
	public int compareTo(Message o) {
		return getDate().compareTo(o.getDate());
	}
}