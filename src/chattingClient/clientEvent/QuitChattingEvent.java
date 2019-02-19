package chattingClient.clientEvent;

import java.io.Serializable;

/**
 * ä�� ���� ��ư�� ���� ������� �̺�Ʈ�� �����ϴ� Ŭ����
 */
public class QuitChattingEvent extends ClientdEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** ä���� ���� ������� �̸� */
	private final String userName;
	/** ����ڸ� ã�� ���� �̸� */
	private final String roomName;

	/**
	 * �־��� �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param userName
	 * @param roomName
	 */
	public QuitChattingEvent(final String userName, final String roomName) {
		this.userName = userName;
		this.roomName = roomName;
	}

	/**
	 * ������� �̸��� ��ȯ
	 *
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * �� �̸��� ��ȯ
	 * 
	 * @return roomName
	 */
	public String getRoomName() {
		return roomName;
	}
}
