package pl.slusarczyk.ignacy.CommunicatorServer.clientHandleEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserName;

/**
 * 클라이언트 응용 프로그램에서 기본 대화창을 열 정보를 제공
 */
public class AfterConnectionServerEvent extends ClientHandleEvent implements Serializable {
	private static final long serialVersionUID = 1L;
	/** 감싸인 사용자 이름 */
	private final UserName userName;
	/** 가입한 방의 이름 */
	private final String roomName;

	/**
	 * 지정된 매개 변수를 기반으로 이벤트를 만드는 생성자
	 * 
	 * @param userName
	 * @param roomName
	 */
	public AfterConnectionServerEvent(final UserName userName, final String roomName) {
		this.userName = userName;
		this.roomName = roomName;
	}

	/**
	 * userName를 반환
	 * 
	 * @return userName
	 */
	public UserName getUserName() {
		return userName;
	}

	/**
	 * 방 이름을 반환
	 * 
	 * @return roomName
	 */
	public String getRoomName() {
		return roomName;
	}
}
