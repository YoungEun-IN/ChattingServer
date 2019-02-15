package pl.slusarczyk.ignacy.CommunicatorServer.model;

import java.io.Serializable;
import java.util.Date;

/** Klasa reprezentująca pojedyńczą wiadomość które składają się na całą rozmowę użytkownika 
 * 
 * @author Ignacy ŚLusarczyk
 *
 */
class Message implements Comparable<Message>,  Serializable
{
	private static final long serialVersionUID = 1L;
	/**Treść konkretnej wiadomości*/
	private final String message;
	/**Znacznik czasowy utworzenia danej wiadomości*/
	private final Date createdOn;

	/**
	 * Konstruktor tworzący wiadomość na podstawie jej treści oraz znacznika czasowego
	 * 
	 * @param message Treść wiadomości
	 * @param timestamp Znacznik czasowy
	 */
	public Message (final String message,final Date timestamp)
	{
		this.message = message;
		this.createdOn = timestamp;
	}
	
	/**
	 * Metoda zwracająca treść konkretnej wiadomości
	 * 
	 * @return Treść wiadomości
	 */
	public String getMessage()
	{
		return message;
	}
	
	/**
	 * Metoda zwracająca datę utworzenia wiadomości 
	 * 
	 * @return Data utworzenia wiadomości
	 */
	public Date getDate ()
	{
		return createdOn;
	}
	
	/** 
	 * Metoda która umożliwia posortowanie wiadomości wg. czasu ich utworzenia
	 */
	public int compareTo(Message o) 
	{
	  return getDate().compareTo(o.getDate());
	}
}
