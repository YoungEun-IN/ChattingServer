package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;
import java.util.HashSet;

/**
 * ����� ��Ű�� Ŭ����
 */
public class UserData implements Serializable {
	private static final long serialVersionUID = 1L;
	/** userName */
	private final String userName;
	/** ������� ��Ű�� �޽��� ��Ʈ */
	private final HashSet<MessageData> usersMessages;
	/** ����ڰ� Ȱ�� �������� ���θ� ��Ÿ���� �÷��� */
	private final boolean isActive;

	public UserData(final String userName, final HashSet<MessageData> userMessages, final boolean isActive) {
		this.userName = userName;
		this.usersMessages = userMessages;
		this.isActive = isActive;
	}

	/**
	 * ������� userID�� ��ȯ
	 * 
	 * @return userId ID
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * ������� ��Ű�� �޽��� ������ ��ȯ
	 * 
	 * @return usersMessages
	 */
	public HashSet<MessageData> getUsersMessages() {
		return usersMessages;
	}

	/**
	 * ����ڰ� Ȱ�� �������� ���ο� ���� ������ ��ȯ
	 * 
	 * @return isActive
	 */
	public boolean isActive() {
		return isActive;
	}
}
