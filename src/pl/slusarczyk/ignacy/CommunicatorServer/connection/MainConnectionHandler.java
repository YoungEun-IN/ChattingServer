package pl.slusarczyk.ignacy.CommunicatorServer.connection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.ConnectionEstablishedServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.ConversationServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.InfoServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.UserId;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.RoomData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserIdData;

/**
 * ����ڰ� ���� ��Ʈ���� ���� ������ �����ϰ� �޽����� �����ϴ� ������ ������ Ŭ����
 */
public class MainConnectionHandler {
	/** userId�� Ű�� ��� ��Ʈ���� ����ڰ� �����ϰ��ִ� �� */
	private final HashMap<UserId, ObjectOutputStream> userOutputStreams;
	/** �������� */
	private ServerSocket serverSocket;
	/** ��Ʈ �ѹ� */
	private final int portNumber;
	/** �̺�Ʈ ť */
	private final BlockingQueue<ServerHandledEvent> eventQueue;

	/**
	 * ������ ��Ʈ�� ������ �����ϴ� ������. �� ������ �����ϱ� ���� ������ ��ũ�� ����ϴ�.
	 * 
	 * @param portNumber
	 * @param eventQueue
	 */
	public MainConnectionHandler(final int portNumber, final BlockingQueue<ServerHandledEvent> eventQueueObject) {
		this.portNumber = portNumber;
		this.eventQueue = eventQueueObject;
		this.userOutputStreams = new HashMap<UserId, ObjectOutputStream>();

		createServerSocket();
		ServerSocketHandler serverSocketHandler = new ServerSocketHandler(serverSocket, eventQueue, userOutputStreams);
		serverSocketHandler.start();
	}

	/**
	 * ������ ��Ʈ�� ���� ������ ����� �޼ҵ�
	 */
	public void createServerSocket() {
		try {
			this.serverSocket = new ServerSocket(portNumber);
		} catch (IOException ex) {
			System.err.println("������ ���� �� �ͼ��� �߻�. " + ex);
			System.exit(1);
		}
	}

	/**
	 * ���� �濡�ִ� ��� ����ڿ��� �޽��� ������ �޼ҵ�
	 * 
	 * @param roomData
	 */
	public void sendMessageToAll(final RoomData roomData) {
		for (UserData userData : roomData.getUserSet()) {
			if (userData.isActive()) {
				sendDirectMessage(userData.getUserIdData(), roomData);
			}
		}
	}

	/**
	 * �־��� �г����� ���� ����ڿ��� ���� �޽����� ������ �޼ҵ�
	 * 
	 * @param userName
	 * @param usersConversation ����� ���� ��ȭ
	 * @param listOfUsers       ���� ä������ ����� ���
	 */
	public void sendDirectMessage(final UserIdData userIdData, final RoomData roomData) {
		try {
			ConversationServerEvent userConversationToSend = new ConversationServerEvent(roomData);
			userOutputStreams.get(new UserId(userIdData.getUserName())).writeObject(userConversationToSend);
		} catch (IOException ex) {
			System.err.println("�ͼ��� �߻�. " + ex);
		}
	}

	/**
	 * ���� �ùٸ��� ����ų� �߰��ϱ� ���� Ŭ���̾�Ʈ���� Ȯ���� ������ �޼ҵ�
	 * 
	 * @param userId
	 * @param roomName
	 */
	public void assertConnectionEstablished(final UserIdData userIdData, final String roomName) {
		try {
			userOutputStreams.get(new UserId(userIdData.getUserName())).writeObject(new ConnectionEstablishedServerEvent( userIdData, roomName));
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * �� �޼���� ����ڿ��� �޽����� ������ �� ���˴ϴ�.
	 * 
	 * @param messageObject ǥ�� �� �޽����� ���� �޽����� ������ ������� �г��� ����
	 */
	public void sendMessage(InfoServerEvent messageObject) {
		try {
			userOutputStreams.get(new UserId(messageObject.getUserIDData().getUserName())).writeObject(messageObject);
			userOutputStreams.remove(new UserId(messageObject.getUserIDData().getUserName()));
		} catch (IOException ex) {
			System.err.println("�ͼ��� �߻�. " + ex);
		}
	}
}
