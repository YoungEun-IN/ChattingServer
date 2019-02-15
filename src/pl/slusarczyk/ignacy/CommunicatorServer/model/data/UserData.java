package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;
import java.util.HashSet;

/**
 * ����� ��Ű�� Ŭ����
 */
public class UserData implements Serializable {
	private static final long serialVersionUID = 1L;
	/** userIdData */
	private final UserIdData userIdData;
	/** ������� ��Ű�� �޽��� ��Ʈ */
	private final HashSet<MessageData> usersMessages;
	/** ����ڰ� Ȱ�� �������� ���θ� ��Ÿ���� �÷��� */
	private final boolean isActive;

	public UserData(final UserIdData userIdData, final HashSet<MessageData> userMessages, final boolean isActive) {
		this.userIdData = userIdData;
		this.usersMessages = userMessages;
		this.isActive = isActive;
	}

	/**
	 * ������� userID�� ��ȯ
	 * 
	 * @return userId ID
	 */
	public UserIdData getUserIdData() {
		return userIdData;
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
	public boolean isUserActive() {
		return isActive;
	}
}
