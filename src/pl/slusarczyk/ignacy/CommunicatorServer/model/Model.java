package pl.slusarczyk.ignacy.CommunicatorServer.model;

import java.util.Calendar;
import java.util.HashSet;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ClientLeftRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.CreateNewRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.JoinExistingRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.NewMessage;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.MessageData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.RoomData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**Klasa, która udostępnia cały interfejs modelu 
 * 
 * @author Ignacy Ślusarczyk
 *
 */
public class Model
{
	/**Zbiór zawierający listę aktywnych pokoi*/
	private final HashSet<Room> roomList;
	
	/**
	 * Konstruktor tworzy pusty zbiór pokoi
	 */
	public Model()
	{
		this.roomList = new HashSet<Room>();
	}
	
	/** 
	 * Metoda tworząca nowy pokój oraz dodająca pierwszego użytkownika, który ten pokój utworzył
	 * 
	 * @param createNewRoom opakowane informacje potrzebne do założenia nowego pokoju
	 */
	public boolean createNewRoom(final CreateNewRoom createNewRoom)
	{
		for(Room room : roomList)
		{
			if (room.getRoomName().equals(createNewRoom.getRoomName()))
			{
				return false;
			}
		}
	
		roomList.add(new Room(createNewRoom.getRoomName(), new UserId(createNewRoom.getUserIdData().getUserName())));
		return true;
	}

	/**
	 * Metoda dodająca użytkownika o zadanym nicku do pokoju o zadanej nazwie 
	 * 
	 * @param joinExistingRoom opakowane informacje potrzebne do dołączenia użytkownika do danego pokoju
	 */
	public boolean addUserToSpecificRoom (final JoinExistingRoom joinExistingRoom)
	{
		for (Room room : roomList)
		{
			if (joinExistingRoom.getRoomName().equals(room.getRoomName()))
			{
				room.addUser(new UserId(joinExistingRoom.getUserIdData().getUserName()));
				return true;
			}
		}
		return false;
	}
		
	/**
	 * Metoda odpowiedzialna za dodanie wiadomości od użytkownika do jego historii wiadomości
	 * 
	 * @param neMessageInformation opakowane informacje potrzebne do dodania nowej wiaodmości użytownika
	 */
	public void addMessageOfUser (final NewMessage newMessage)
	{
		for (Room room : roomList)
		{
			if (newMessage.getRoomName().equals(room.getRoomName()))
			{
				for (User user: room.getUserList())
				{		
					if (new UserId(newMessage.getUserIdData().getUserName()).equals(user.getUserID()))
					{
						user.addMessage(newMessage, Calendar.getInstance().getTime());
					}
				}
			}
		}	
	}
	
	/**
	 * Metoda opakowująca dane pokoju, do którego przyszła nowa wiadomość 
	 * 
	 * @param newMessageInformation opakowana wiadomość wysłana do pokoju, z którego dane chcemy pobrać
	 * 
	 * @return opakowane informacje o pokoju
	 */
	public RoomData getRoomDataFromRoom (final NewMessage newMessageObject)
	{
		HashSet<UserData> userSet= new HashSet<UserData>();
	
		for (Room room : roomList)
		{
			if (newMessageObject.getRoomName().equals(room.getRoomName()))
			{
				for(User user: room.getUserList())
				{
					HashSet<MessageData> messagesOfUser= new HashSet<MessageData>();
					for(Message message: user.getUserMessageHistory())
					{
						messagesOfUser.add(new MessageData(message.getMessage(),message.getDate()));
					}
					UserData userData = new UserData(new UserIdData(user.getUserID()), messagesOfUser, user.getUserStatus());
					userSet.add(userData);
				}
				return new RoomData(userSet);
			}
		}
		return null;
	}
	
	/**
	 * Metoda oznaczająca danego użytkownika jako nieaktywnego
	 * 
	 * @param clientLeftRoom informacje o użytkowniku, który wyszedł z chatu
	 */
	public void setUserToInactive (ClientLeftRoom clientLeftRoom)
	{
		for (Room room: roomList)
		{
			if(room.getRoomName().equals(clientLeftRoom.getRoomName()))
			{
				for (User user:room.getUserList())
				{
					if(user.getUserID().equals(new UserId(clientLeftRoom.getUserIDData().getUserName())))
					{
						user.setUserToInactive();
					}
				}
			}
		}
	}
}
