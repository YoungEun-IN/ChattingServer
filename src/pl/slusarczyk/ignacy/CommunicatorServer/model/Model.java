package pl.slusarczyk.ignacy.CommunicatorServer.model;

import java.util.Calendar;
import java.util.HashSet;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ClientLeftRoomEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.CreateNewRoomEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.JoinExistingRoomEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.SendMessageEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.MessageData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.RoomData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * ��ü �� �������̽��� �����ϴ� Ŭ����
 */
public class Model {
	/** ��Ƽ�� �� ����� ���� �� ��Ʈ */
	private final HashSet<Room> roomList;

	/**
	 * ������
	 */
	public Model() {
		this.roomList = new HashSet<Room>();
	}

	/**
	 * ���� �̸��� ���� �����ϴ��� Ȯ���ϰ� �� ���� �����ϴ� �޼ҵ�
	 * 
	 * @param createNewRoom
	 */
	public boolean createNewRoom(final CreateNewRoomEvent createNewRoom) {
		for (Room room : roomList) {
			if (room.getRoomName().equals(createNewRoom.getRoomName())) {
				return false;
			}
		}

		roomList.add(new Room(createNewRoom.getRoomName(), new UserId(createNewRoom.getUserIdData().getUserName())));
		return true;
	}

	/**
	 * �־��� �̸��� ���� �濡 �־��� �г����� ���� ����ڰ� �ִ��� Ȯ���ϰ� ����ڸ� �߰��ϴ� �޼ҵ�
	 * 
	 * @param joinExistingRoom
	 */
	public boolean addUserToSpecificRoom(final JoinExistingRoomEvent joinExistingRoom) {
		for (Room room : roomList) {
			if (joinExistingRoom.getRoomName().equals(room.getRoomName())) {
				room.addUser(new UserId(joinExistingRoom.getUserIdData().getUserName()));
				return true;
			}
		}
		return false;
	}

	/**
	 * ������� �޽��� ��Ͽ� �޽����� �߰��ϴ� å�������� �޼ҵ�
	 * 
	 * @param neMessage
	 */
	public void addMessageOfUser(final SendMessageEvent newMessage) {
		for (Room room : roomList) {
			if (newMessage.getRoomName().equals(room.getRoomName())) {
				for (User user : room.getUserList()) {
					if (new UserId(newMessage.getUserIdData().getUserName()).equals(user.getUserID())) {
						user.addMessage(newMessage, Calendar.getInstance().getTime());
					}
				}
			}
		}
	}

	/**
	 * ���ο� �޽����� �� ���� ���� ������ �������ϴ� �޼ҵ�
	 * 
	 * @param newMessage
	 * 
	 * @return RoomData Object
	 */
	public RoomData getRoomDataFromRoom(final SendMessageEvent newMessageObject) {
		HashSet<UserData> userSet = new HashSet<UserData>();

		for (Room room : roomList) {
			if (newMessageObject.getRoomName().equals(room.getRoomName())) {
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
	 * @param clientLeftRoom
	 */
	public void setUserToInactive(ClientLeftRoomEvent clientLeftRoom) {
		for (Room room : roomList) {
			if (room.getRoomName().equals(clientLeftRoom.getRoomName())) {
				for (User user : room.getUserList()) {
					if (user.getUserID().equals(new UserId(clientLeftRoom.getUserIDData().getUserName()))) {
						user.setUserToInactive();
					}
				}
			}
		}
	}
}
