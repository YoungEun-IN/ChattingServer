package pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent;

import java.io.Serializable;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * ä�� ���� ��ư�� ���� ������� �̺�Ʈ�� �����ϴ� Ŭ�����Դϴ�.
 */
public class ClientLeftRoomEvent extends ServerHandledEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** ä���� ���� ������� �̸� */
	private final UserIdData userIdData;
	/** ����ڸ� ã�� ���� �̸� */
	private final String roomName;

	/**
	 * �־��� �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param userName
	 * @param roomName
	 */
	public ClientLeftRoomEvent(final UserIdData userIDData, final String roomName) {
		this.userIdData = userIDData;
		this.roomName = roomName;
	}

	/**
	 * ������� �̸��� ��ȯ
	 *
	 * @return userIdData
	 */
	public UserIdData getUserIDData() {
		return userIdData;
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
