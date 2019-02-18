package pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent;

import java.io.Serializable;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserName;

/**
 * ä�� ���� ��ư�� ���� ������� �̺�Ʈ�� �����ϴ� Ŭ����
 */
public class QuitChattingEvent extends ServerHandledEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** ä���� ���� ������� �̸� */
	private final UserName userName;
	/** ����ڸ� ã�� ���� �̸� */
	private final String roomName;

	/**
	 * �־��� �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param userName
	 * @param roomName
	 */
	public QuitChattingEvent(final UserName userName, final String roomName) {
		this.userName = userName;
		this.roomName = roomName;
	}

	/**
	 * ������� �̸��� ��ȯ
	 *
	 * @return userName
	 */
	public UserName getUserName() {
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
