package pl.slusarczyk.ignacy.CommunicatorServer.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ClientLeftRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.CreateNewRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.JoinExistingRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandeledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.MessageServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.UserId;

/** Klasa odpowiedzialna za odbieranie zdarzeń od klienta i dodawanie ich do kolejki
 * 
 * @author Ignacy Ślusatczyk
 */
public class UserConnectionHandler extends Thread
{
	/**Socket klienta*/
	private final Socket userSocket;
	/**Strumień wejściowy*/
	private ObjectInputStream inputStream;
	/**Strumień wyjściowy*/
	private ObjectOutputStream outputStream;
	/**Mapa ID userów oraz ich strumieni wyjściowych*/
	 private final HashMap<UserId,ObjectOutputStream> userOutputStreams;
	/**Kolejka blokująca zdarzeń*/
	 private final BlockingQueue<ServerHandeledEvent> eventQueue;
	 /**Flaga określająca czy wątek pracuje*/
	 private boolean running;
	
	/**
	 * Konstruktor tworzący nowy wątek nasłuchujący połączeń od danego użytkownika
	 * 
	 * @param userSocket socket użytkownika
	 * @param eventQueue kolejka zdarzeń
	 * @param userOutputStreams mapa strumieni wyjściowych
	 */
	public UserConnectionHandler (final Socket userSocket,final BlockingQueue<ServerHandeledEvent> eventQueue,final HashMap <UserId,ObjectOutputStream> userOutputStreams)
	{
		this.userSocket = userSocket;
		this.eventQueue = eventQueue;
		this.userOutputStreams = userOutputStreams;
		this.running = true;
		
		try
		{
			outputStream = new ObjectOutputStream(userSocket.getOutputStream());
			inputStream = new ObjectInputStream(userSocket.getInputStream());
		}
		catch (IOException ex)
		{
			System.err.println("Nastapił błąd podczas tworzenia strumienia z klientem" + ex);
			return;
		}
	}
	
	/**
	 * Główna pętla klasy, w której nasłuchuje zdarzeń od klienta i dodaje je do kolejki blokującej
	 */
		public void run()
		{
			ServerHandeledEvent appEvent;
			while(running)
			{	
				try
				{
					appEvent = (ServerHandeledEvent) inputStream.readObject();
			
					/**W przypadku kiedy od klienta przychodzi żądanie dołączenia do pokoju lub jego utworzenia musimy zapisać jego strumień wyjściowy*/
					if (appEvent instanceof CreateNewRoom) 
					{
						CreateNewRoom createNewRoom = (CreateNewRoom) appEvent;	
						
						/** Przed dodaniem do mapy musimy sprawdzić czy dany użytkownik już nie istnieje*/
						if (userOutputStreams.get(new UserId(createNewRoom.getUserIdData().getUserName())) != null)
						{
							outputStream.writeObject(new MessageServerEvent("Uzytkownik o podanej nazwie juz istnieje", createNewRoom.getUserIdData()));				
						}
						/**Jeśli nie istnieje dodajemy go do mapy*/
						else
						{
							userOutputStreams.put(new UserId(createNewRoom.getUserIdData().getUserName()), outputStream);
							eventQueue.add(appEvent);
						}
					}
					else if(appEvent instanceof JoinExistingRoom)
					{
						JoinExistingRoom joinNewRoom = (JoinExistingRoom) appEvent;	
						
						/** Przed dodaniem do mapy musimy sprawdzić czy dany użytkownik już nie istnieje*/
						if (userOutputStreams.get(new UserId(joinNewRoom.getUserIdData().getUserName())) != null)
						{
							outputStream.writeObject(new MessageServerEvent("Uzytkownik o podanej nazwie juz istnieje", joinNewRoom.getUserIdData()));			
						}
						else
						{
							/**Jeśli nie istnieje dodajemy go do mapy*/
							userOutputStreams.put(new UserId(joinNewRoom.getUserIdData().getUserName()), outputStream);
							eventQueue.add(appEvent);
						}
					}
					/**Jesli dostaniemy obiekt reprezentujący wyjście człowieka z chatu musimy zatrzymać wątek i przekazać zdarzenie do kontrolera*/
					else if(appEvent instanceof ClientLeftRoom)
					{
						eventQueue.add(appEvent);
						userSocket.close();
						running = false;
					}
					else 
					{
						eventQueue.add(appEvent);
					}
				}
				catch (IOException ex)
				{
					try 
					{
						userSocket.close();
						running = false;
					}
					catch (IOException e) 
					{
						System.err.println(e);
					}
				}
				catch (ClassNotFoundException ex)
				{
					System.err.println("Błąd rzutowania przychodzącej informacji" + ex);
				}
				catch (NullPointerException ex)
				{
					System.err.println("Błąd odbierania obiektu" + ex);
				}
				catch(ClassCastException ex)
				{
					System.err.println(ex);
				}
			}
		}
}
