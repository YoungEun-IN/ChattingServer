package pl.slusarczyk.ignacy.CommunicatorServer.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.NewMessage;

/**
 * User 클래스는 특정 사용자에 대한 모든 정보를 포함합니다. 이 정보는 userId와 그에 의해 보내지는 모든 메시지를 나타내는 메시지
 * 유형 세트로 구성됩니다.
 */

class User implements Serializable {
	private static final long serialVersionUID = 1L;
	/** userID */
	private final UserId userID;
	/** 사용자가 보낸 일련의 메시지 */
	private final HashSet<Message> messageHistory;
	/** 사용자가 활성 상태인지 여부를 나타내는 플래그 */
	private boolean isActive;

	/**
	 * 지정된 이름을 가지는 사용자를 작성하는 생성자
	 * 
	 * @param userId
	 */
	public User(final UserId userId) {
		this.userID = userId;
		messageHistory = new HashSet<Message>();
		this.isActive = true;
	}

	/**
	 * 사용자의 ID를 반환
	 * 
	 * @return userID
	 */
	public UserId getUserID() {
		return userID;
	}

	/**
	 * 주어진 사용자가 보낸 메시지 집합을 반환
	 * 
	 * @return messageHistory
	 */
	public HashSet<Message> getUserMessageHistory() {
		return messageHistory;
	}

	/**
	 * 주어진 사용자의 메시지 집합에 메시지를 추가
	 * 
	 * @param textMessage
	 * @param timestamp
	 */
	public void addMessage(final NewMessage newMessage, final Date timestamp) {
		messageHistory.add(new Message(newMessage.getMessage(), timestamp));
	}

	/**
	 * 사용자가 아직 채팅을 사용하고 있는지 여부에 대한 정보를 반환
	 * 
	 * @return isActive
	 */
	public boolean getUserStatus() {
		return isActive;
	}

	/**
	 * 주어진 사용자를 비활성으로 표시하는 메소드
	 */
	public void setUserToInactive() {
		isActive = false;
	}
}