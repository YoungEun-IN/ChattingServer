package chattingServer.serverEvent;

import java.io.Serializable;

import chattingServer.model.data.RoomData;

/**
 * ������ �޽��� ������ ��Ÿ���� Ŭ������ Ŭ���̾�Ʈ �����쿡�� ��ȭ�� ������Ʈ�ؾ��ϴ� �ʿ伺, �� �޽����� �߰� �� �濡 ���� ���� �� �����͸� �����ϴ�.
 */
public class ConversationBuildEvent extends ServerEvent implements Serializable {
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
