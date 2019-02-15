package pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * ���ο� ������ ������ ���� ������ ��Ÿ���� Ŭ�����μ�, Ŭ���̾�Ʈ ���� ���α׷����� �⺻ ��ȭâ�� �� ���ɼ��� �˸��ϴ�.
 */
public class ConnectionEstablishedServerEvent extends ClientHandledEvent implements Serializable
{
	 private static final long serialVersionUID = 1L;
	/**���� ������ ���� ǥ��*/
	 private  final boolean isEstablished;
	 /**������ ����� �̸�*/
	 private final UserIdData userIDData;
	 /**������ ���� �̸�*/
	 private final String roomName;
	 
	/**
	 * ������ �Ű� ������ ������� �̺�Ʈ�� ����� ������
	 * 
	 * @param isEstablished ������ �����Ǿ����� ����
	 * @param userIdData
	 * @param roomName
	 */
	public ConnectionEstablishedServerEvent(final boolean isEstablished, final UserIdData userIdData, final String roomName)
	{
		this.isEstablished = isEstablished;
		this.userIDData = userIdData;
		this.roomName = roomName;
	}
	
	/**
	 * ���� ������ ���� ������ ��ȯ�ϴ� �޼���
	 * 
	 * @return isEstablished
	 */
	public boolean isEstablished()
	{
		return this.isEstablished;
	}
	
	/**
	 * userIDData�� ��ȯ
	 * 
	 * @return userIDData
	 */
	public UserIdData getUserIDData()
	{
		return userIDData;
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
}
