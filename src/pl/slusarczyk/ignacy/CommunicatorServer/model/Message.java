package pl.slusarczyk.ignacy.CommunicatorServer.model;

import java.io.Serializable;
import java.util.Date;

/** 
 * ������� ��ü ��ȭ�� ������ �޽����� ���� �κ��� ��Ÿ���� Ŭ����
 */
class Message implements Comparable<Message>,  Serializable
{
	private static final long serialVersionUID = 1L;
	/**message*/
	private final String message;
	/**timeStamp*/
	private final Date timeStamp;

	/**
	 * ����� Ÿ�� �������� ������� �޽����� �����ϴ� ������
	 * 
	 * @param message
	 * @param timestamp
	 */
	public Message (final String message,final Date timestamp)
	{
		this.message = message;
		this.timeStamp = timestamp;
	}
	
	/**
	 * Ư�� �޽����� ������ ��ȯ
	 * 
	 * @return message
	 */
	public String getMessage()
	{
		return message;
	}
	
	/**
	 * Ÿ�� �������� ����
	 * 
	 * @return timeStamp
	 */
	public Date getDate ()
	{
		return timeStamp;
	}
	
	/** 
	 * �޽����� ���� �� ���ִ� �޼ҵ�
	 */
	public int compareTo(Message o) 
	{
	  return getDate().compareTo(o.getDate());
	}
}
