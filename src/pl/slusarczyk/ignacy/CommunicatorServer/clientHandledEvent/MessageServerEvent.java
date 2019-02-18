package pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * �� Ŭ������ ����ڿ��� ������ ������ �� ���˴ϴ�.
 */
public class MessageServerEvent extends ClientHandledEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** ����ڿ��� ǥ�� �� �޽��� */
	private final String message;
	/** �޽����� ǥ�� �� ������� ���� ��Ű�� �̸� */
	private final UserIdData userIDData;

	public MessageServerEvent(final String message, final UserIdData userIDData) {
		this.message = message;
		this.userIDData = userIDData;
	}

	/**
	 * �޽��� ������ ��ȯ
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * ���� ����� userId�� ��ȯ
	 * 
	 * @return userIDData
	 */
	public UserIdData getUserIDData() {
		return userIDData;
	}
}
