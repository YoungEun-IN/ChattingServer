package chattingClient.clientSideEvent;

import java.io.Serializable;

/**
 * ����ڰ� �濡 �����ϱ� ���� ��ư�� ������ �̺�Ʈ�� �����ϴ� Ŭ����
 */
public class JoinExistingRoomEvent extends ClientSideEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**roomName*/
	private final String roomName;
	/**userName*/
	private final String userName;

	/**
	 * ������ �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param roomName 
	 * @param userId ID
	 */
	public JoinExistingRoomEvent (final String roomName,final String userName)
	{
		this.roomName = roomName;
		this.userName = userName;
	}
		
	/**
	 * �� �̸��� ��ȯ
	 * 
	 * @return roomName
	 */
	public String getRoomName()
	{
		return roomName;
	}

	/**
	 * ������� ��Ű�� �̸��� ��ȯ
	 * 
	 * @return userName
	 */
	public String getUserName()
	{
		return userName;
	}
}


