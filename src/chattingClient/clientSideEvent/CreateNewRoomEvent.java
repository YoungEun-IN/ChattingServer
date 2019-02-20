package chattingClient.clientSideEvent;

import java.io.Serializable;

/**
 * ���� ���� ������ �� �߻��ϴ� �̺�Ʈ
 */
public class CreateNewRoomEvent extends ClientSideEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** ���� �̸� */
	private final String roomName;
	/** userName */
	private final String userName;

	/**
	 * ������ �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param roomName
	 * @param userID
	 */
	public CreateNewRoomEvent(final String roomName, final String userName) {
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
	public String getUserName() {
		return userName;
	}
}
