package pl.slusarczyk.ignacy.CommunicatorServer.model.data;

import java.io.Serializable;

import pl.slusarczyk.ignacy.CommunicatorServer.model.UserId;

/**
 * Klasa opakowująca nazwę użytkownika, wysyłana do klienta
 * 
 * @author Ignacy Ślusarczyk
 */
public class UserIdData implements Serializable
{
	private static final long serialVersionUID = 1L;
	/**Nazwa użytkownika*/
	private final String userNameToDisplay;
	
	/**
	 * Konstruktor tworzący obiekt na podstawei zadanego parametru
	 * 
	 * @param userName Nazwa użytownika
	 */
	public UserIdData(final UserId userId)
	{
		this.userNameToDisplay = userId.getUserName();
	}
	
	@Override
	public boolean equals(Object other) 
	{
		if(other == null)
		{
			return false;
		}
		
		if(!(other instanceof UserIdData))
		{
			return false;
		}
		
		UserIdData otherUserId = (UserIdData) other;
		String otherUserName = otherUserId.getUserName();
		return this.userNameToDisplay.equals(otherUserName);
	}
	
	/**
	 * Metoda zwracająca nazwę użytkownika w celu dopasowania ich do wysłanych przez nich wiadomości
	 * 
	 * @return user name
	 */
	public String getUserName()
	{
		return userNameToDisplay;
	}
}

