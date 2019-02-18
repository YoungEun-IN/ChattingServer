package pl.slusarczyk.ignacy.CommunicatorServer.clientHandleEvent;

import java.io.Serializable;

/**
 * Ŭ���̾�Ʈ ���� ���α׷����� �⺻ ��ȭâ�� �� ������ ����
 */
public class AfterConnectionServerEvent extends ClientHandleEvent implements Serializable {
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
	public AfterConnectionServerEvent(final String userName, final String roomName) {
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
