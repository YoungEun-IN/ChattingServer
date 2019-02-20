package chattingServer.serverSideEvent;

import java.io.Serializable;

import chattingServer.model.data.RoomData;

/**
 * ��ȭ�� ������ �� �߻��ϴ� �̺�Ʈ
 */
public class ConversationBuildEvent extends ServerSideEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** �濡 ���� ������ ���� */
	private final RoomData roomData;

	/**
	 * ������ �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param roomData
	 */
	public ConversationBuildEvent(final RoomData roomData) {
		this.roomData = roomData;
	}

	/**
	 * �濡 ���� ��Ű�� �����͸� ��ȯ
	 * 
	 * @return roomData
	 */
	public RoomData getRoom() {
		return roomData;
	}
}
