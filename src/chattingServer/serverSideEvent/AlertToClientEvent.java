package chattingServer.serverSideEvent;

import java.io.Serializable;

/**
 * ����ڿ��� �˸��� �� �� �߻��ϴ� �̺�Ʈ
 */
public class AlertToClientEvent extends ServerSideEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** ����ڿ��� ǥ�� �� �޽��� */
	private final String message;
	/** �޽����� ǥ�� �� ������� ���� ��Ű�� �̸� */
	private final String userName;

	public AlertToClientEvent(final String message, final String userName) {
		this.message = message;
		this.userName = userName;
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
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}
}
