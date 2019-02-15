package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.UserId;

/**
 * 고객에게 보낸 사용자 이름을 래핑하는 클래스
 */
public class UserIdData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/** userName */
	private final String userName;

	/**
	 * 지정된 파라미터에 근거해 오브젝트를 생성하는 생성자
	 * 
	 * @param userId
	 */
	public UserIdData(final UserId userId) {
		this.userName = userId.getUserName();
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}

		if (!(other instanceof UserIdData)) {
			return false;
		}

		UserIdData otherUserId = (UserIdData) other;
		String otherUserName = otherUserId.getUserName();
		return this.userName.equals(otherUserName);
	}

	/**
	 * 사용자가 보낸 메시지와 일치하도록 사용자의 이름을 반환
	 * 
	 * @return userName
	 */
	public String getUserName() {
		return userName;
	}
}
