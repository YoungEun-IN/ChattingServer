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
 * 전체 모델 인터페이스를 제공하는 클래스
 */
public class UserActionProcessor {
	/** 활성화된 룸 목록이 포함 된 세트 */
	private final HashSet<Room> roomList;

	/**
	 * 생성자
	 */
	public UserActionProcessor() {
		this.roomList = new HashSet<Room>();
	}

	/**
	 * 같은 이름의 방이 존재하는지 확인하고 새 방을 생성하는 메소드
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
	 * 주어진 이름을 가진 방에 주어진 닉네임을 가진 사용자가 있는지 확인하고 사용자를 추가하는 메소드
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
	 * 사용자의 메시지 기록에 메시지를 추가하는 메소드
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
	 * 새로운 메시지가 온 방의 세부 사항을 전달하는 메소드
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
	 * 사용자를 비활성으로 나타내는 메소드
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
