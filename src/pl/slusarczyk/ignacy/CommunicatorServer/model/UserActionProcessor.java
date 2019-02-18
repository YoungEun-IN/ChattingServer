package pl.slusarczyk.ignacy.CommunicatorServer.model;

import java.util.Calendar;
import java.util.HashSet;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent.CreateNewRoomEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent.JoinExistingRoomEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent.QuitChattingEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent.SendMessageEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.MessageData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.RoomData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * ��ü �� �������̽��� �����ϴ� Ŭ����
 */
public class UserActionProcessor {
	/** Ȱ��ȭ�� �� ����� ���� �� ��Ʈ */
	private final HashSet<Room> roomList;

	/**
	 * ������
	 */
	public UserActionProcessor() {
		this.roomList = new HashSet<Room>();
	}

	/**
	 * ���� �̸��� ���� �����ϴ��� Ȯ���ϰ� �� ���� �����ϴ� �޼ҵ�
	 * 
	 * @param createNewRoomEvent
	 */
	public boolean createNewRoom(final CreateNewRoomEvent createNewRoomEvent) {
		for (Room room : roomList) {
			if (room.getRoomName().equals(createNewRoomEvent.getRoomName())) {
				return false;
			}
		}

		roomList.add(new Room(createNewRoomEvent.getRoomName(), new UserId(createNewRoomEvent.getUserIdData().getUserName())));
		return true;
	}

	/**
	 * �־��� �̸��� ���� �濡 �־��� �г����� ���� ����ڰ� �ִ��� Ȯ���ϰ� ����ڸ� �߰��ϴ� �޼ҵ�
	 * 
	 * @param joinExistingRoomEvent
	 */
	public boolean addUserToSpecificRoom(final JoinExistingRoomEvent joinExistingRoomEvent) {
		for (Room room : roomList) {
			if (joinExistingRoomEvent.getRoomName().equals(room.getRoomName())) {
				room.addUser(new UserId(joinExistingRoomEvent.getUserIdData().getUserName()));
				return true;
			}
		}
		return false;
	}

	/**
	 * ������� �޽��� ��Ͽ� �޽����� �߰��ϴ� �޼ҵ�
	 * 
	 * @param neMessage
	 */
	public void addMessageOfUser(final SendMessageEvent sendMessageEvent) {
		for (Room room : roomList) {
			if (sendMessageEvent.getRoomName().equals(room.getRoomName())) {
				for (User user : room.getUserList()) {
					if (new UserId(sendMessageEvent.getUserIdData().getUserName()).equals(user.getUserID())) {
						user.addMessage(sendMessageEvent, Calendar.getInstance().getTime());
					}
				}
			}
		}
	}

	/**
	 * ���ο� �޽����� �� ���� ���� ������ �����ϴ� �޼ҵ�
	 * 
	 * @param newMessage
	 * 
	 * @return RoomData Object
	 */
	public RoomData getRoomData(final SendMessageEvent sendMessageEvent) {
		HashSet<UserData> userSet = new HashSet<UserData>();

		for (Room room : roomList) {
			if (sendMessageEvent.getRoomName().equals(room.getRoomName())) {
				for (User user : room.getUserList()) {
					HashSet<MessageData> messagesOfUser = new HashSet<MessageData>();
					for (Message message : user.getUserMessageHistory()) {
						messagesOfUser.add(new MessageData(message.getMessage(), message.getDate()));
					}
					UserData userData = new UserData(new UserIdData(user.getUserID()), messagesOfUser, user.isActive());
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
	 * @param clientLeftRoomEvent
	 */
	public void setUserToInactive(QuitChattingEvent clientLeftRoomEvent) {
		for (Room room : roomList) {
			if (room.getRoomName().equals(clientLeftRoomEvent.getRoomName())) {
				for (User user : room.getUserList()) {
					if (user.getUserID().equals(new UserId(clientLeftRoomEvent.getUserIDData().getUserName()))) {
						user.setUserToInactive();
					}
				}
			}
		}
	}
}
