package pl.slusarczyk.ignacy.CommunicatorServer.clientHandleEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * Ŭ���̾�Ʈ ���� ���α׷����� �⺻ ��ȭâ�� �� ������ ����
 */
public class AfterConnectionServerEvent extends ClientHandleEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** ������ ����� �̸� */
	private final UserIdData userIDData;
	/** ������ ���� �̸� */
	private final String roomName;

	/**
	 * ������ �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param userIdData
	 * @param roomName
	 */
	public AfterConnectionServerEvent(final UserIdData userIdData, final String roomName) {
		this.userIDData = userIdData;
		this.roomName = roomName;
	}

	/**
	 * userIDData�� ��ȯ
	 * 
	 * @return userIDData
	 */
	public UserIdData getUserIDData() {
		return userIDData;
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
