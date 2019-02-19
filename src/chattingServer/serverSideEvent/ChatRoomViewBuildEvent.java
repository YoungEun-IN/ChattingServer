package chattingServer.serverSideEvent;

import java.io.Serializable;

/**
 * Ŭ���̾�Ʈ ���� ���α׷����� �⺻ ��ȭâ�� �� ������ ����
 */
public class ChatRoomViewBuildEvent extends ServerSideEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** ������ ����� �̸� */
	private final String userName;
	/** ������ ���� �̸� */
	private final String roomName;

	/**
	 * ������ �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param userName
	 * @param roomName
	 */
	public ChatRoomViewBuildEvent(final String userName, final String roomName) {
		this.userName = userName;
		this.roomName = roomName;
	}

	/**
	 * userName�� ��ȯ
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
