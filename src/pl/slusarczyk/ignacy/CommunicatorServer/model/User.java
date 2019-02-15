package pl.slusarczyk.ignacy.CommunicatorServer.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.NewMessage;

/**
 * User Ŭ������ Ư�� ����ڿ� ���� ��� ������ �����մϴ�. �� ������ userId�� �׿� ���� �������� ��� �޽����� ��Ÿ���� �޽���
 * ���� ��Ʈ�� �����˴ϴ�.
 */

class User implements Serializable {
	private static final long serialVersionUID = 1L;
	/** userID */
	private final UserId userID;
	/** ����ڰ� ���� �Ϸ��� �޽��� */
	private final HashSet<Message> messageHistory;
	/** ����ڰ� Ȱ�� �������� ���θ� ��Ÿ���� �÷��� */
	private boolean isActive;

	/**
	 * ������ �̸��� ������ ����ڸ� �ۼ��ϴ� ������
	 * 
	 * @param userId
	 */
	public User(final UserId userId) {
		this.userID = userId;
		messageHistory = new HashSet<Message>();
		this.isActive = true;
	}

	/**
	 * ������� ID�� ��ȯ
	 * 
	 * @return userID
	 */
	public UserId getUserID() {
		return userID;
	}

	/**
	 * �־��� ����ڰ� ���� �޽��� ������ ��ȯ
	 * 
	 * @return messageHistory
	 */
	public HashSet<Message> getUserMessageHistory() {
		return messageHistory;
	}

	/**
	 * �־��� ������� �޽��� ���տ� �޽����� �߰�
	 * 
	 * @param textMessage
	 * @param timestamp
	 */
	public void addMessage(final NewMessage newMessage, final Date timestamp) {
		messageHistory.add(new Message(newMessage.getMessage(), timestamp));
	}

	/**
	 * ����ڰ� ���� ä���� ����ϰ� �ִ��� ���ο� ���� ������ ��ȯ
	 * 
	 * @return isActive
	 */
	public boolean getUserStatus() {
		return isActive;
	}

	/**
	 * �־��� ����ڸ� ��Ȱ������ ǥ���ϴ� �޼ҵ�
	 */
	public void setUserToInactive() {
		isActive = false;
	}
}