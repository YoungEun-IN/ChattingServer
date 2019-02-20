package chattingServer.model;

import java.util.Date;

/**
 * 메시지 정보를 담고 있다.
 */
public class Message implements Comparable<Message> {
	/** message */
	private final String message;
	/** timeStamp */
	private final Date timeStamp;

	/**
	 * 내용과 타임 스탬프를 기반으로 메시지를 생성하는 생성자
	 * 
	 * @param message
	 * @param timestamp
	 */
	public Message(final String message, final Date timestamp) {
		this.message = message;
		this.timeStamp = timestamp;
	}

	/**
	 * 특정 메시지의 내용을 반환
	 * 
	 * @return message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * 타임 스탬프를 리턴
	 * 
	 * @return timeStamp
	 */
	public Date getDate() {
		return timeStamp;
	}

	/**
	 * 메시지를 정렬 할 수있는 메소드
	 */
	public int compareTo(Message o) {
		return getDate().compareTo(o.getDate());
	}
}
