package pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserName;

/**
 * ����ڰ� �濡 �����ϱ� ���� ��ư�� ������ �̺�Ʈ�� �����ϴ� Ŭ����
 */
public class JoinExistingRoomEvent extends ServerHandledEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**roomName*/
	private final String roomName;
	/**userName*/
	private final UserName userName;

	/**
	 * ������ �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param roomName 
	 * @param userId ID
	 */
	public JoinExistingRoomEvent (final String roomName,final UserName userName)
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
	public UserName getUserName()
	{
		return userName;
	}
}


