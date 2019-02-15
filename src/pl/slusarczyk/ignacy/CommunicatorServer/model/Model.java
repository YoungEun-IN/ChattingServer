package pl.slusarczyk.ignacy.CommunicatorServer.model;

import java.util.Calendar;
import java.util.HashSet;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ClientLeftRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.CreateNewRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.JoinExistingRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.NewMessage;
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
	 * ���� �̸��� ���� �����ϴ��� Ȯ���ϴ� �޼ҵ�
	 * 
	 * @param createNewRoom
	 */
	public boolean createNewRoom(final CreateNewRoom createNewRoom) {
		for (Room room : roomList) {
			if (room.getRoomName().equals(createNewRoom.getRoomName())) {
				return false;
			}
		}

		roomList.add(new Room(createNewRoom.getRoomName(), new UserId(createNewRoom.getUserIdData().getUserName())));
		return true;
	}

	/**
	 * �־��� �̸��� ���� �濡 �־��� �г����� ���� ����ڸ� �߰��ϴ� �޼ҵ�
	 * 
	 * @param joinExistingRoom
	 */
	public boolean addUserToSpecificRoom(final JoinExistingRoom joinExistingRoom) {
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
	public void addMessageOfUser(final NewMessage newMessage) {
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
	public RoomData getRoomDataFromRoom(final NewMessage newMessageObject) {
		HashSet<UserData> userSet = new HashSet<UserData>();

		for (Room room : roomList) {
			if (newMessageObject.getRoomName().equals(room.getRoomName())) {
				for (User user : room.getUserList()) {
					HashSet<MessageData> messagesOfUser = new HashSet<MessageData>();
					for (Message message : user.getUserMessageHistory()) {
						messagesOfUser.add(new MessageData(message.getMessage(), message.getDate()));
					}
					UserData userData = new UserData(new UserIdData(user.getUserID()), messagesOfUser, user.getUserStatus());
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
	public void setUserToInactive(ClientLeftRoom clientLeftRoom) {
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
