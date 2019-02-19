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
 * 전체 모델 인터페이스를 제공하는 클래스
 */
public class UserActionProcessor {
	/** 활성화된 룸 목록이 포함 된 세트 */
	private final HashSet<Room> roomSet;

	/**
	 * 생성자
	 */
	public UserActionProcessor() {
		this.roomSet = new HashSet<Room>();
	}

	/**
	 * 같은 이름의 방이 존재하는지 확인하고 새 방을 생성하는 메소드
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
	 * 주어진 이름을 가진 방에 주어진 닉네임을 가진 사용자가 있는지 확인하고 사용자를 추가하는 메소드
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
	 * 사용자의 메시지 기록에 메시지를 추가하는 메소드
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
	 * 새로운 메시지가 온 방의 세부 사항을 전달하는 메소드
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
	 * 사용자를 비활성으로 나타내는 메소드
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
