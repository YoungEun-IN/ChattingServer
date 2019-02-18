package pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent;

import java.io.Serializable;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * ������ �޼����� �۽��ϱ� ���ؼ� ��ư�� ������ �̺�Ʈ�� ����ϴ� Ŭ����
 */
public class SendMessageEvent extends ServerHandledEvent implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**���� �̸�*/
	private final String roomName;
	/**������ ����� �̸�*/
	private final UserIdData userIDData;
	/**����ڰ� �������� �޽���*/
	private final String message;
	
	/**
	 * �־��� �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param roomName
	 * @param userID 
	 * @param message
	 */
	public SendMessageEvent (final String roomName,final UserIdData userIdData,final String message)
	{
		this.roomName = roomName;
		this.userIDData = userIdData;
		this.message = message;
	}
	
	/**
	 * ������ ����ڰ��ִ� ���� �̸��� ��ȯ 
	 * 
	 * @return roomName
	 */
	public String getRoomName()
	{
		return roomName;
	}
	
	/**
	 * �޽����� ���� ������� ID�� ��ȯ
	 * 
	 * @return userIDData
	 */
	public UserIdData getUserIdData ()
	{
		return userIDData;
	}
	
	/**
	 * ����ڰ� ���� �޽����� ��ȯ
	 * 
	 * @return message
	 */
	public String getMessage ()
	{
		return message;
	}
}
