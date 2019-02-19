package chattingServer.model;

import java.util.Calendar;
import java.util.HashSet;

import chattingClient.clientEvent.CreateNewRoomEvent;
import chattingClient.clientEvent.JoinExistingRoomEvent;
import chattingClient.clientEvent.QuitChattingEvent;
import chattingClient.clientEvent.SendMessageEvent;
import chattingServer.model.data.MessageData;
import chattingServer.model.data.RoomData;
import chattingServer.model.data.UserData;

/**
 * ��ü �� �������̽��� �����ϴ� Ŭ����
 */
public class UserActionProcessor {
	/** Ȱ��ȭ�� �� ����� ���� �� ��Ʈ */
	private final HashSet<Room> roomSet;

	/**
	 * ������
	 */
	public UserActionProcessor() {
		this.roomSet = new HashSet<Room>();
	}

	/**
	 * ���� �̸��� ���� �����ϴ��� Ȯ���ϰ� �� ���� �����ϴ� �޼ҵ�
	 * 
	 * @param createNewRoomEvent
	 */
	public boolean createNewRoom(final CreateNewRoomEvent createNewRoomEvent) {
		for (Room room : roomSet) {
			if (room.getRoomName().equals(createNewRoomEvent.getRoomName())) {
				return false;
			}
		}

		roomSet.add(new Room(createNewRoomEvent.getRoomName(), new UserId(createNewRoomEvent.getUserName())));
		return true;
	}

	/**
	 * �־��� �̸��� ���� �濡 �־��� �г����� ���� ����ڰ� �ִ��� Ȯ���ϰ� ����ڸ� �߰��ϴ� �޼ҵ�
	 * 
	 * @param joinExistingRoomEvent
	 */
	public boolean addUserToSpecificRoom(final JoinExistingRoomEvent joinExistingRoomEvent) {
		for (Room room : roomSet) {
			if (joinExistingRoomEvent.getRoomName().equals(room.getRoomName())) {
				room.addUser(new UserId(joinExistingRoomEvent.getUserName()));
				return true;
			}
		}
		return false;
	}

	/**
	 * ������� �޽��� ��Ͽ� �޽����� �߰��ϴ� �޼ҵ�
	 * 
	 * @param sendMessageEvent
	 */
	public void addMessageOfUser(final SendMessageEvent sendMessageEvent) {
		for (Room room : roomSet) {
			if (sendMessageEvent.getRoomName().equals(room.getRoomName())) {
				for (User user : room.getUserSet()) {
					if (new UserId(sendMessageEvent.getUserName()).equals(user.getUserID())) {
						user.addMessage(sendMessageEvent, Calendar.getInstance().getTime());
					}
				}
			}
		}
	}

	/**
	 * ���ο� �޽����� �� ���� ���� ������ �����ϴ� �޼ҵ�
	 * 
	 * @param sendMessageEvent
	 * 
	 * @return RoomData
	 */
	public RoomData getRoomData(final SendMessageEvent sendMessageEvent) {
		HashSet<UserData> userSet = new HashSet<UserData>();

		for (Room room : roomSet) {
			if (sendMessageEvent.getRoomName().equals(room.getRoomName())) {
				for (User user : room.getUserSet()) {
					HashSet<MessageData> messagesOfUser = new HashSet<MessageData>();
					for (Message message : user.getUserMessageHistory()) {
						messagesOfUser.add(new MessageData(message.getMessage(), message.getDate()));
					}
					UserData userData = new UserData(user.getUserID().getUserName(), messagesOfUser, user.isActive());
					userSet.add(userData);
				}
				return new RoomData(userSet);
			}
		}
		return null;
	}

	/**
	 * ����ڸ� ��Ȱ������ ��Ÿ���� �޼ҵ�
	 * 
	 * @param quitChattingEvent
	 */
	public void setUserToInactive(QuitChattingEvent quitChattingEvent) {
		for (Room room : roomSet) {
			if (room.getRoomName().equals(quitChattingEvent.getRoomName())) {
				for (User user : room.getUserSet()) {
					if (user.getUserID().equals(new UserId(quitChattingEvent.getUserName()))) {
						user.setUserToInactive();
					}
				}
			}
		}
	}
}
