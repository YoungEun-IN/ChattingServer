package chattingServer.model.data;

import java.io.Serializable;
import java.util.HashSet;

/**
 * �� ������ ��� �ִ�.
 */
public class RoomData implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**userSet*/
	private  final HashSet<UserData> userSet;
	
	/**
	 * �־��� �Ű� ������ ������� ��ü�� �����ϴ� ������
	 * 
	 * @param userSet
	 */
	public RoomData(final HashSet<UserData> userSet)
	{
		this.userSet = userSet;
	}
	
	/**
	 * ������� ������ ��ȯ
	 * 
	 * @return userSet
	 */
	public HashSet<UserData> getUserSet()
	{
		return userSet;
	}
}
