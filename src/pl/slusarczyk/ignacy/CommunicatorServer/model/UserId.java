package pl.slusarczyk.ignacy.CommunicatorServer.model;

import java.io.Serializable;

/**
 * equals �޼���� hashCode �޼��带 ������ ������� �̸��� �����ϴ� Ŭ����
 */
public class UserId implements Serializable {
	private static final long serialVersionUID = 1L;
	/** userName */
	private final String userName;

	/**
	 * ������ �Ķ���Ϳ� �ٰ� �� ������Ʈ�� �����ϴ� ������
	 * 
	 * @param userName
	 */
	public UserId(final String userName) {
		this.userName = userName;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}

		if (!(other instanceof UserId)) {
			return false;
		}

		UserId otherUserId = (UserId) other;
		String otherUserName = otherUserId.getUserName();
		return userName.equals(otherUserName);
	}

	@Override
	public int hashCode() {
		return userName.hashCode();
	}

	/**
	 * ID�� ����� �̸��� ��ȯ�ϴ� �޼���
	 * 
	 * @return user name
	 */
	public String getUserName() {
		return userName;
	}
}
