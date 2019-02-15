package pl.slusarczyk.ignacy.CommunicatorServer.model;

import java.io.Serializable;
import java.util.HashSet;

/**
 * 방에있는 사용자 집합과 그 이름을 포함하는 하나의 방을 나타내는 클래스
 */
class Room implements Serializable {
	private static final long serialVersionUID = 1L;
	/** roomName */
	private String roomName;
	/** listOfUsers */
	private HashSet<User> listOfUsers;

	/**
	 * 방의 이름과이 방을 가진 사용자의 이름을 기반으로 새 방을 만드는 생성자
	 * 
	 * @param roomName     
	 * @param userId
	 */
	public Room(final String roomName, final UserId userId) {
		this.roomName = roomName;
		listOfUsers = new HashSet<User>();
		listOfUsers.add(new User(userId));
	}

	/**
	 * 사용자의 리스트를 반환
	 * 
	 * @return listOfUsers
	 */
	public HashSet<User> getUserList() {
		return listOfUsers;
	}

	/**
	 * 방 이름을 반환
	 * 
	 * @return roomName
	 */
	public String getRoomName() {
		return roomName;
	}

	/**
	 * 지정된 이름의 사용자를 방에 추가
	 * 
	 * @param userName
	 */
	public void addUser(final UserId userId) {
		User newUser = new User(userId);
		listOfUsers.add(newUser);
	}
}