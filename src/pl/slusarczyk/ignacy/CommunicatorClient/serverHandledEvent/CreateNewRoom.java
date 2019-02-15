package pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent;

import java.io.Serializable;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * ������ ���ο� ���� �ۼ��ϱ� ���ؼ� ��ư�� ������ �̺�Ʈ�� ��Ÿ���� Ŭ����
 */
public class CreateNewRoom extends ServerHandledEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** ���� �̸� */
	private final String roomName;
	/** userIDData */
	private final UserIdData userIDData;

	/**
	 * ������ �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param roomName
	 * @param userID
	 */
	public CreateNewRoom(final String roomName, final UserIdData userIDData) {
		this.roomName = roomName;
		this.userIDData = userIDData;
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
	 * @return userIDData
	 */
	public UserIdData getUserIdData() {
		return userIDData;
	}
}
