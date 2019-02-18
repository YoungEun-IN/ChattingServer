package pl.slusarczyk.ignacy.CommunicatorServer.clientHandleEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.RoomData;

/**
 * ������ �޽��� ������ ��Ÿ���� Ŭ������ Ŭ���̾�Ʈ �����쿡�� ��ȭ�� ������Ʈ�ؾ��ϴ� �ʿ伺, �� �޽����� �߰� �� �濡 ���� ���� �� �����͸� �����ϴ�.
 */
public class ConversationServerEvent extends ClientHandleEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** �濡 ���� ������ ���� */
	private final RoomData roomData;

	/**
	 * ������ �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param userConversation ����� ���� ��ȭ
	 */
	public ConversationServerEvent(final RoomData room) {
		this.roomData = room;
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
