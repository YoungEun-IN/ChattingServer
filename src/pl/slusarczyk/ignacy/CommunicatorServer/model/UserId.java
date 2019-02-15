package pl.slusarczyk.ignacy.CommunicatorServer.model;

import java.io.Serializable;

/**
 * Klasa opakowująca nazwę użytkownika impelmentująca metody equals oraz hashCode.
 * 
 * @author Ignacy Ślusarczyk
 */
public class UserId implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**Nazwa użytkownika*/
	private final String userName;
	
	/**
	 * Konstruktor tworzący obiekt na podstawie zadanego parametru
	 * 
	 * @param userName Nazwa użytownika
	 */
	public UserId(final String userName)
	{
		this.userName = userName;
	}
	
	@Override
	public boolean equals(Object other) 
	{
		if(other == null)
		{
			return false;
		}
		
		if(!(other instanceof UserId))
		{
			return false;
		}
		
		UserId otherUserId = (UserId) other;
		String otherUserName = otherUserId.getUserName();
		return userName.equals(otherUserName);
	}
	
	
	@Override
	public int hashCode() 
	{
		return userName.hashCode();
	}
	
	/**
	 * Metoda zwracająca nazwę użytkownika z ID, potrzebna przy opakowywniu userId w UserIdData, nie udostępniana klientowi
	 * 
	 * @return user name
	 */
	public String getUserName()
	{
		return userName;
	}
}
