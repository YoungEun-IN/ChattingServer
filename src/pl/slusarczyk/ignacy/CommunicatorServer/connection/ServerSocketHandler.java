package pl.slusarczyk.ignacy.CommunicatorServer.connection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.UserId;

/**
 * Klasa odpowiedzialna za nasłuchiwanie przez serwer żądań nowych połączeń od klientów
 * 
 * @author Ignacy Ślusarczyk
 */
class ServerSocketHandler extends Thread
{
	/**Socket servera*/
	private final ServerSocket serverSocket;
	/**Kolejka blokująca zdarzeń*/
	private final BlockingQueue<ServerHandledEvent> eventQueue;
	/**Mapa użytkowników i ich output streamów*/
	private final HashMap <UserId,ObjectOutputStream> userOutputStreams;
	
	/**
	 * Konstruktor tworzący wątek nasłuchujący nowych połączeń
	 * 
	 * @param serverSocket socket serwera
	 * @param eventQueue kolejka zdarzeń
	 * @param userOutputStreams mapa strumieni wyjściowych użytkowników
	 */
	public ServerSocketHandler(final ServerSocket serverSocket, final BlockingQueue<ServerHandledEvent> eventQueue,final HashMap <UserId,ObjectOutputStream> userOutputStreams)
	{
		this.serverSocket = serverSocket;
		this.eventQueue = eventQueue;
		this.userOutputStreams = userOutputStreams;
	}
	
	/**
	 * Główna pętla klasy, w której nasłuchuje nowych połączeń od klientów
	 */
	public void run()
	{
		System.out.println("Serwer zaczął nasłuchiwanie nowych połączeń na porcie: " + serverSocket.getLocalPort());
		while(true)
		{
			try
			{
				Socket userSocket = serverSocket.accept();
				UserConnectionHandler newConnection = new UserConnectionHandler (userSocket, eventQueue, userOutputStreams);
				newConnection.start();	
			}
			catch (IOException ex)
			{
				System.err.println(ex);
			}
		}
	}
}
