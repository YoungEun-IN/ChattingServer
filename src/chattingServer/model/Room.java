package chattingServer.model;

import java.util.HashSet;

/**
 * �� ������ ��� �ִ�.
 */
public class Room {
	/** roomName */
	private String roomName;
	/** listOfUsers */
	private HashSet<User> listOfUsers;

	/**
	 * ���� �̸����� ���� ���� ������� �̸��� ������� �� ���� ����� ������
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
	 * ������� ����Ʈ�� ��ȯ
	 * 
	 * @return listOfUsers
	 */
	public HashSet<User> getUserSet() {
		return listOfUsers;
	}

	/**
	 * �� �̸��� ��ȯ
	 * 
	 * @return roomName
	 */
	public String getRoomName() {
		return roomName;
	}

	/**
	 * ������ �̸��� ����ڸ� �濡 �߰�
	 * 
	 * @param userName
	 */
	public void addUser(final UserId userId) {
		User newUser = new User(userId);
		listOfUsers.add(newUser);
	}
}