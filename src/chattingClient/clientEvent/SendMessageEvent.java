package chattingClient.clientEvent;

import java.io.Serializable;

/**
 * ������ �޼����� �۽��ϱ� ���ؼ� ��ư�� ������ �̺�Ʈ�� ����ϴ� Ŭ����
 */
public class SendMessageEvent extends ClientdEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** ���� �̸� */
	private final String roomName;
	/** ������ ����� �̸� */
	private final String userName;
	/** ����ڰ� �������� �޽��� */
	private final String message;

	/**
	 * �־��� �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param roomName
	 * @param userID
	 * @param message
	 */
	public SendMessageEvent(final String roomName, final String userName, final String message) {
		this.roomName = roomName;
		this.userName = userName;
		this.message = message;
	}

	/**
	 * ������ ����ڰ��ִ� ���� �̸��� ��ȯ
	 * 
	 * @return roomName
	 */
	public String getRoomName() {
		return roomName;
	}

	/**
	 * �޽����� ���� ������� ID�� ��ȯ
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * ����ڰ� ���� �޽����� ��ȯ
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}
}
