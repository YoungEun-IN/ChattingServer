package pl.slusarczyk.ignacy.CommunicatorServer.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ClientLeftRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.CreateNewRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.JoinExistingRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.NewMessage;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandeledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.MessageServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.connection.MainConnectionHandler;
import pl.slusarczyk.ignacy.CommunicatorServer.model.Model;

/**
 * Klasa kontrolera odpowiadajaca za odpowiednią komunikację pomiędzy klientem a serwerem, zawierająca w sobie klasy
 * odpowiadające strategiom obsługi zdarzeń pochodzących od klienta
 * 
 * @author Ignacy Ślusarczyk
 */
public class Controller 
{
	/**Kolejka blokująca*/
	private final BlockingQueue<ServerHandeledEvent> eventQueue;
	/**Referencja do modelu*/
	private final Model model;
	/**Mapa strategii obsługi zdarzeń*/
	private final Map<Class<? extends ServerHandeledEvent>, clientEventStrategy> strategyMap;
	/**Referencja do Servera*/
	private final MainConnectionHandler mainConnectionHandler;
	
	/**
	 * Konstruktor tworzący controler na podstawie zadanych parametrów
	 * 
	 * @param eventQueue kolejka blokująca
	 * @param model model
	 * @param mainConnectionHandler serwer
	 */
	public Controller(final BlockingQueue<ServerHandeledEvent>  eventQueue, final Model model, final MainConnectionHandler mainConnectionHandler)
	{
		this.eventQueue = eventQueue;
		this.model = model;
		this.mainConnectionHandler = mainConnectionHandler;
		
		//Tworzenie mapy strategii obsługi zdarzeń
		strategyMap = new HashMap<Class<? extends ServerHandeledEvent>, clientEventStrategy>();
		strategyMap.put(CreateNewRoom.class, new CreateNewRoomStrategy());
		strategyMap.put(JoinExistingRoom.class, new JoinExistingRoomStrategy());
		strategyMap.put(NewMessage.class, new NewMessageStrategy());	
		strategyMap.put(ClientLeftRoom.class,new ClientLeftRoomStrategy());
	}
			
		/**
		 * Główna metoda kontrolera, czeka on w niej na zdarzenia, a następnie odpowiednio je obsługuje.
		 */
		public void work()
		{
			while (true)
			{
				try
				{
					ServerHandeledEvent serverHandeledEvent = eventQueue.take();
					clientEventStrategy applicationEventStrategy = strategyMap.get(serverHandeledEvent.getClass());
					applicationEventStrategy.execute(serverHandeledEvent);
				}
				catch(InterruptedException e)
				{
					//Nic nie robimy, ponieważ kontroler ma być zawieszony dopóki nie pojawi się zdarzenie
				}
			}
		}
		
		/**
		 * Abstrakcyjna klasa bazowa dla klas strategii obsługujących zdarzenia.
		 * 
		 * @author Ignacy Ślusarczyk
		 */
		abstract class clientEventStrategy
		{
			/**
			 * Abstrakcyjna metoda opisująca obsługę danego zdarzenia.
			 * 
			 * @param applicationEvent zdarzenie aplikacji które musi zostać obsłużone
			 */
			abstract void execute(final ServerHandeledEvent applicationEvent);
		}
		
		/**
		 * Klasa wewnętrzna opisująca strategię obsługi żądania przez użytkownika utworzenia nowego pokoju
		 *
		 * @author Ignacy Ślusarczyk
		 */
		class CreateNewRoomStrategy extends clientEventStrategy
		{
			void execute(final ServerHandeledEvent applicationEventObject)
			{
				CreateNewRoom createNewRoom = (CreateNewRoom) applicationEventObject;
				if (model.createNewRoom(createNewRoom))
				{
					mainConnectionHandler.connectionEstablished(createNewRoom.getUserIdData(), true, createNewRoom.getRoomName());
				}
				else 
				{
					mainConnectionHandler.sendMessage(new MessageServerEvent("Pokoj o zadanej nazwie juz istnieje",createNewRoom.getUserIdData()));
				}
			}
		}
		
		/**
		 * Klasa wewnętrzna opisująca strategię obsługi żądania przez użytkownika dołączenia do istniejącego pokoju
		 *
		 * @author Ignacy Ślusarczyk
		 */
		class JoinExistingRoomStrategy extends clientEventStrategy
		{
			void execute(final ServerHandeledEvent applicationEventObject)
			{
				JoinExistingRoom joinExistingRoom = (JoinExistingRoom) applicationEventObject;	
				if(model.addUserToSpecificRoom(joinExistingRoom) == true)
				{
					mainConnectionHandler.connectionEstablished(joinExistingRoom.getUserIdData(), true,joinExistingRoom.getRoomName());
				}
				else 
				{
					mainConnectionHandler.sendMessage(new MessageServerEvent("Pokoj do ktorego chcesz dolaczyc nie istnieje",joinExistingRoom.getUserIdData()));
				}
			}
		}
	
		/**
		 * Klasa wewnętrzna opisująca strategię obsługi wysłania przez użytkownika nowej wiadomości
		 * 
		 * @author Ignacy Ślusarczyk
		 */
		class NewMessageStrategy extends clientEventStrategy
		{
			void execute(final ServerHandeledEvent applicationEventObject)
			{
				NewMessage newMessage = (NewMessage) applicationEventObject;
				model.addMessageOfUser(newMessage);
				mainConnectionHandler.sendMessageToAll(model.getRoomDataFromRoom(newMessage));	
			}
		}
		
		/**
		 * Klasa wewnętrzna opisująca strategię wyjscia użytkownika z pokoju rozmów
		 * 
		 * @author Ignacy Ślusarc
		 */
		class ClientLeftRoomStrategy extends clientEventStrategy
		{
			void execute(final ServerHandeledEvent applicationEventObject) 
			{
				ClientLeftRoom clientLeftRoom = (ClientLeftRoom) applicationEventObject;
				model.setUserToInactive(clientLeftRoom);
			}
		}
}
