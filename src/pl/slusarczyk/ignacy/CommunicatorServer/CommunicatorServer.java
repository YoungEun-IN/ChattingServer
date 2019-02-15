package pl.slusarczyk.ignacy.CommunicatorServer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.connection.MainConnectionHandler;
import pl.slusarczyk.ignacy.CommunicatorServer.controller.Controller;
import pl.slusarczyk.ignacy.CommunicatorServer.model.Model;

/**
 * Główna klasa applikacji odpowiada za odpowiednie zainicjalizowanie wszystkich komponentów
 * 
 * @author Ignacy Ślusarczyk
 */
public class CommunicatorServer 
{
	/**
	 * Głowna metoda aplikacji,tworzy model, kolejkę zdarzeń oraz kontroler.
	 * 
	 * @param args argumenty wywołania programu
	 */
	public static void main(String args[])
	{
		BlockingQueue<ServerHandledEvent> eventQueue = new LinkedBlockingQueue<ServerHandledEvent>();
		MainConnectionHandler mainConnectionHandler = new MainConnectionHandler(5000,eventQueue);
		Model model = new Model();
		Controller controller = new Controller(eventQueue, model, mainConnectionHandler);
		controller.work();
	}	
}
