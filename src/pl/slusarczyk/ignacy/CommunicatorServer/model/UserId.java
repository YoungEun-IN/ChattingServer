package pl.slusarczyk.ignacy.CommunicatorServer.model;

import java.io.Serializable;

/**
 * equals 메서드와 hashCode 메서드를 포함한 사용자의 이름을 래핑하는 클래스
 */
public class UserId implements Serializable {
	private static final long serialVersionUID = 1L;
	/** userName */
	private final String userName;

	/**
	 * 지정된 파라미터에 근거 해 오브젝트를 생성하는 생성자
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
	 * ID로 사용자 이름을 반환하는 메서드
	 * 
	 * @return user name
	 */
	public String getUserName() {
		return userName;
	}
}
