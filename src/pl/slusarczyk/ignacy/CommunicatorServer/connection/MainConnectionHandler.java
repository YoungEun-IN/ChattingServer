package pl.slusarczyk.ignacy.CommunicatorServer.connection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.ConnectionEstablishedServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.ConversationServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.MessageServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.UserId;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.RoomData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * Klasa główna serwera przechowująca informacje o strumieniach wyjściowych użytkowników i rozsyłająca wiadomości
 * 
 * @author Ignacy Ślusarczyk
 */
public class MainConnectionHandler 
{
	/** Mapa przetrzymująca strumienie wyjściowe użytkowników, w której kluczem jest userId */
	private final HashMap <UserId, ObjectOutputStream> userOutputStreams;
	/**Socket servera*/
	private ServerSocket serverSocket;
	/**Port na którym serwer nasłuchuje*/
	private final int portNumber;
	/**Kolejka bloująca zdarzeń*/
	private final BlockingQueue<ServerHandledEvent> eventQueue; 
	
	/**
	 * Konstruktor włączający serwer na podanym porcie, który tworzy oddzielny wątek do nasłuchiwania nowych połączeń
	 * 
	 * @param portNumber numer portu
	 * @param eventQueue kolejka blokująca zdarzeń
	 */
	public MainConnectionHandler (final int portNumber, final BlockingQueue<ServerHandledEvent> eventQueueObject)
	{
		this.eventQueue = eventQueueObject;
		this.portNumber = portNumber;
		this.userOutputStreams = new HashMap <UserId, ObjectOutputStream>();
		
		createServerSocket();
		ServerSocketHandler serverSocketHandler = new ServerSocketHandler(serverSocket, eventQueue, userOutputStreams);
		serverSocketHandler.start();
	}
	
	/**
	 * Metoda tworząca server socket na zadanym porcie
	 */
	public void createServerSocket()
	{
		try
		{
			this.serverSocket = new ServerSocket(portNumber);
		}
		catch(IOException ex)
		{
			System.err.println("Nastąpił błąd podczas tworzenia połączenia " + ex);
			System.exit(1);	
		}
	}
	
	/**
	 * Metoda wysyłająca bezpośrednio wiadomość do użytkownika o zadanym nicku
	 * 
	 * @param userName nazwa użytkownika
	 * @param usersConversation rozmowa pomiędzy użytkownikami
	 * @param listOfUsers lista użytkowników aktualnie będących na chacie
	 */
	public void sendDirectMessage (final UserIdData userIdData,final RoomData roomData)
	{
		try
		{
			ConversationServerEvent userConversationToSend = new ConversationServerEvent (roomData);
			userOutputStreams.get(new UserId(userIdData.getUserName())).writeObject(userConversationToSend);
		}
		catch(IOException ex)
		{
			System.err.println(ex);
		}
	}
	
	/**
	 * Metoda wysyłająca rozmowę do wszystkich użytkowników znajdujących się w tym samym pokoju
	 * 
	 * @param roomData Opakowany obiekt pokój, w którym znajduje się konwersacja do wyświetlenia oraz lista użytkowników
	 */
	public void sendMessageToAll (final RoomData roomData)
	{
		for (UserData userData: roomData.getUserSet())
		{
			if(userData.isUserActive())
			{
				sendDirectMessage(userData.getUserIdData(), roomData);
			}
		}
	}
	
	/**
	 * Metoda wysyłająca do klienta potwierdzenie poprawnego utworzenia lub dodania do pokoju
	 * 
	 * @param userId
	 * @param connectionEstablished
	 */
	public void connectionEstablished(final UserIdData userIdData,final boolean connectionEstablished,final String roomName)
	{
		try 
		{
			userOutputStreams.get(new UserId(userIdData.getUserName())).writeObject(new ConnectionEstablishedServerEvent(connectionEstablished,userIdData,roomName));
		} 
		catch (IOException e) 
		{
			System.err.println(e);
		}
	}	
	
	/**
	 * Metoda służąca do wysłania wiadomości informacyjnej do użytkownika
	 * 
	 * @param messageObject Obiekt zawierający w sobie wiadomość do wyświetlenia oraz nick użytkownika do którego wysyłamy wiadomość informacyjną
	 */
	public void sendMessage(MessageServerEvent messageObject)
	{
		try
		{
			userOutputStreams.get(new UserId(messageObject.getUserIDData().getUserName())).writeObject(messageObject);
			userOutputStreams.remove(new UserId(messageObject.getUserIDData().getUserName()));
		}
		catch(IOException ex)
		{
			System.err.println(ex);
		}
	}
}
