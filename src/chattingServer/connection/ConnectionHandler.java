package chattingServer.connection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import chattingClient.clientSideEvent.ClientSideEvent;
import chattingServer.model.UserId;
import chattingServer.model.data.RoomData;
import chattingServer.model.data.UserData;
import chattingServer.serverSideEvent.AlertToClientEvent;
import chattingServer.serverSideEvent.ChatRoomViewBuildEvent;
import chattingServer.serverSideEvent.ConversationBuildEvent;

/**
 * ����ڰ� ���� ��Ʈ���� ���� ������ �����ϰ� �޽����� �����ϴ� ������ ������ Ŭ����
 */
public class ConnectionHandler {
	/** userId�� Ű�� ��� ��Ʈ���� ����ڰ� �����ϰ��ִ� �� */
	private final HashMap<UserId, ObjectOutputStream> userOutputStreams;
	/** �������� */
	private ServerSocket serverSocket;
	/** ��Ʈ �ѹ� */
	private final int portNumber;
	/** �̺�Ʈ ť */
	private final BlockingQueue<ClientSideEvent> eventQueue;

	/**
	 * ������ ��Ʈ�� ������ �����ϴ� ������. �� ������ �����ϱ� ���� ������ ��ũ�� ����ϴ�.
	 * 
	 * @param portNumber
	 * @param eventQueue
	 */
	public ConnectionHandler(final int portNumber, final BlockingQueue<ClientSideEvent> eventQueueObject) {
		this.portNumber = portNumber;
		this.eventQueue = eventQueueObject;
		this.userOutputStreams = new HashMap<UserId, ObjectOutputStream>();

		createServerSocket();
		ServerSocketThread serverSocketThread = new ServerSocketThread(serverSocket, eventQueue, userOutputStreams);
		serverSocketThread.start();
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
			ConversationBuildEvent conversationBuildEvent = new ConversationBuildEvent(roomData);
			userOutputStreams.get(new UserId(userName)).writeObject(conversationBuildEvent);
		} catch (IOException ex) {
			System.err.println("sendDirectMessage �ͼ��� �߻�. " + ex);
		}
	}

	/**
	 * ���� �ùٸ��� ����ų� �߰��ϱ� ���� Ŭ���̾�Ʈ���� ������ ������ �޼ҵ�
	 * 
	 * @param userName
	 * @param roomName
	 */
	public void buildChatRoomView(final String userName, final String roomName) {
		try {
			userOutputStreams.get(new UserId(userName)).writeObject(new ChatRoomViewBuildEvent(userName, roomName));
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	/**
	 * ����ڿ��� �ȳ��� ������ �� ���Ǵ� �޼ҵ�
	 * 
	 * @param messageObject
	 */
	public void alert(AlertToClientEvent messageObject) {
		try {
			userOutputStreams.get(new UserId(messageObject.getUserName())).writeObject(messageObject);
			userOutputStreams.remove(new UserId(messageObject.getUserName()));
		} catch (IOException ex) {
			System.err.println("alert �ͼ��� �߻�. " + ex);
		}
	}
}
