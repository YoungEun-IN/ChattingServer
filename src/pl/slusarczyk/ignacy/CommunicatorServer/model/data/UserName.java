package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.UserId;

/**
 * ������ ���� ����� �̸��� �����ϴ� Ŭ����
 */
public class UserName implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** userName */
	private final String userName;

	/**
	 * ������ �Ķ���Ϳ� �ٰ��� ������Ʈ�� �����ϴ� ������
	 * 
	 * @param userId
	 */
	public UserName(final UserId userId) {
		this.userName = userId.getUserName();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}

		if (!(other instanceof UserName)) {
			return false;
		}

		UserName otherUserId = (UserName) other;
		String otherUserName = otherUserId.getUserName();
		return this.userName.equals(otherUserName);
	}

	/**
	 * ����ڰ� ���� �޽����� ��ġ�ϵ��� ������� �̸��� ��ȯ
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}
}
