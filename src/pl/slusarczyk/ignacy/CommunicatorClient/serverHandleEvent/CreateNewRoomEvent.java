package pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent;

import java.io.Serializable;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserName;

/**
 * ������ ���ο� ���� �ۼ��ϱ� ���ؼ� ��ư�� ������ �̺�Ʈ�� ��Ÿ���� Ŭ����
 */
public class CreateNewRoomEvent extends ServerHandledEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** ���� �̸� */
	private final String roomName;
	/** userName */
	private final UserName userName;

	/**
	 * ������ �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param roomName
	 * @param userID
	 */
	public CreateNewRoomEvent(final String roomName, final UserName userName) {
		this.roomName = roomName;
		this.userName = userName;
	}

	/**
	 * �� �̸��� ��ȯ
	 * 
	 * @return roomName
	 */
	public String getRoomName() {
		return roomName;
	}

	/**
	 * ������� ID�� ��ȯ
	 * 
	 * @return userName
	 */
	public UserName getUserName() {
		return userName;
	}
}
