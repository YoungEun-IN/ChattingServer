package chattingServer.model;

/**
 * equals �޼���� hashCode �޼��带 ������ ������� �̸��� �����ϴ� Ŭ����
 */
public class UserId {
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
