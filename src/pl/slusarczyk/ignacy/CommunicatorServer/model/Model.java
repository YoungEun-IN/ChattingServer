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
 * 전체 모델 인터페이스를 제공하는 클래스
 */
public class Model {
	/** 액티브 룸 목록이 포함 된 세트 */
	private final HashSet<Room> roomList;

	/**
	 * 생성자
	 */
	public Model() {
		this.roomList = new HashSet<Room>();
	}

	/**
	 * 같은 이름의 방이 존재하는지 확인하는 메소드
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
	 * 주어진 이름을 가진 방에 주어진 닉네임을 가진 사용자를 추가하는 메소드
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
	 * 사용자의 메시지 기록에 메시지를 추가하는 책임을지는 메소드
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
	 * 새로운 메시지가 온 방의 세부 사항을 마무리하는 메소드
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
	 * 사용자를 비활성으로 나타내는 메소드
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
