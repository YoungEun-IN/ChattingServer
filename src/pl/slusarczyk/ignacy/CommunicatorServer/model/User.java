package pl.slusarczyk.ignacy.CommunicatorServer.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.NewMessage;

/**Klasa User zawiera wszystkie informacje o konkretnym użytkowniku
 * Na te informacji składa się jego userId oraz zbiór wiadomości typu Message, które reprezentują wszystkie wiadomości wysłane przez niego
 *  
 * @author Ignacy ŚLusarczyk
 */

class User implements Serializable
{
	private static final long serialVersionUID = 1L;
	/** ID danego użytkownika */
	private final UserId userID;
	/**Zbiór wysłanych przez użytkownika wiadomości */
	private final HashSet<Message> messageHistory;
	/**Flaga określająca czy użytkownik jest aktywny*/
	private boolean isActive;
	
	/**Konstruktor tworzący użytkownika o podanym imieniu
	 * 
	 * @param userId Id użytkownika którego tworzymy
	 */
	public User (final UserId userId)
	{
		this.userID = userId;
		messageHistory = new HashSet<Message>();
		this.isActive = true;
	}
	
	/**
	 * Metoda zwracająca ID użytkownika
	 * 
	 * @return userID
	 */
	public UserId getUserID ()
	{
		return userID;
	}
	
	/**
	 * Metoda zwracająca zbiór wiadomości wysłanych przez danego użytkowniak
	 * 
	 * @return zbior wiadomosci
	 */
	public HashSet<Message> getUserMessageHistory ()
	{
		return messageHistory;
	}
	
	/**Metoda dodająca wiadomość do zbioru wiadomości danego użytkownika
	 * 
	 * @param textMessage Treść dodawanej wiadomości
	 * @param timestamp Znacznik czasowy dodawanej wiadomości
	 */
	public void addMessage (final NewMessage newMessage,final Date timestamp)
	{
		messageHistory.add(new Message(newMessage.getMessage(),timestamp));
	}
	
	/**
	 * Metoda zwracająca informację czy dany użytkownik nadal korzysta z czatu
	 * 
	 * @return czy korzysta
	 */
	public boolean getUserStatus()
	{
		return isActive;
	}
	
	/**
	 * Metoda zaznaczająca danego użytkownika jako nieaktywnego
	 */
	public void setUserToInactive()
	{
		isActive = false;
	}
}