package pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * ����ڰ� �濡 �����ϱ� ���� ��ư�� ������ �̺�Ʈ�� �����ϴ� Ŭ����
 */
public class JoinExistingRoom extends ServerHandledEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**roomName*/
	private final String roomName;
	/**userIDData*/
	private final UserIdData userIDData;

	/**
	 * ������ �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param roomName 
	 * @param userId ID
	 */
	public JoinExistingRoom (final String roomName,final UserIdData userIdData)
	{
		this.roomName = roomName;
		this.userIDData = userIdData;
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
	 * @return userIDData
	 */
	public UserIdData getUserIdData()
	{
		return userIDData;
	}
}


