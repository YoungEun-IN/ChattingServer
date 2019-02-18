package pl.slusarczyk.ignacy.CommunicatorServer.connection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandleEvent.AfterConnectionServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandleEvent.ConversationServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandleEvent.MessageServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.UserId;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.RoomData;
import pl.slusarczyk.ignacy.CommunicatorServer.model.data.UserData;

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
				sendDirectMessage(userData.getUserName(), roomData);
			}
		}
	}

	/**
	 * �־��� �г����� ���� ����ڿ��� ���� �޽����� ������ �޼ҵ�
	 * 
	 * @param userName
	 * @param roomData
	 */
	private void sendDirectMessage(final String userName, final RoomData roomData) {
		try {
			ConversationServerEvent conversationServerEvent = new ConversationServerEvent(roomData);
			userOutputStreams.get(new UserId(userName)).writeObject(conversationServerEvent);
		} catch (IOException ex) {
			System.err.println("���� �޽��� ���� �� �ͼ��� �߻�. " + ex);
		}
	}

	/**
	 * ���� �ùٸ��� ����ų� �߰��ϱ� ���� Ŭ���̾�Ʈ���� ������ ������ �޼ҵ�
	 * 
	 * @param userName
	 * @param roomName
	 */
	public void sendMainChatViewInfo(final String userName, final String roomName) {
		try {
			userOutputStreams.get(new UserId(userName)).writeObject(new AfterConnectionServerEvent(userName, roomName));
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * ����ڿ��� �޽����� ������ �� ���Ǵ� �޼ҵ�
	 * 
	 * @param messageObject
	 */
	public void sendMessage(MessageServerEvent messageObject) {
		try {
			userOutputStreams.get(new UserId(messageObject.getUserName())).writeObject(messageObject);
			userOutputStreams.remove(new UserId(messageObject.getUserName()));
		} catch (IOException ex) {
			System.err.println("�ͼ��� �߻�. " + ex);
		}
	}
}
