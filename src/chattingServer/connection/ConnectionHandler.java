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
 * 사용자가 종료 스트림에 대한 정보를 저장하고 메시지를 배포하는 서버의 마스터 클래스
 */
public class ConnectionHandler {
	/** userId가 키인 출력 스트림을 사용자가 보유하고있는 맵 */
	private final HashMap<UserId, ObjectOutputStream> userOutputStreams;
	/** 서버소켓 */
	private ServerSocket serverSocket;
	/** 포트 넘버 */
	private final int portNumber;
	/** 이벤트 큐 */
	private final BlockingQueue<ClientSideEvent> eventQueue;

	/**
	 * 지정된 포트에 서버를 연결하는 생성자. 새 연결을 연결하기 위해 별도의 링크를 만듭니다.
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
	 * 지정된 포트에 서버 소켓을 만드는 메소드
	 */
	public void createServerSocket() {
		try {
			this.serverSocket = new ServerSocket(portNumber);
		} catch (IOException ex) {
			System.err.println("연결을 만들 때 익셉션 발생. " + ex);
			System.exit(1);
		}
	}

	/**
	 * 같은 방에있는 모든 사용자에게 메시지 보내는 메소드
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
	 * 주어진 닉네임을 가진 사용자에게 직접 메시지를 보내는 메소드
	 * 
	 * @param userName
	 * @param roomData
	 */
	private void sendDirectMessage(final String userName, final RoomData roomData) {
		try {
			ConversationBuildEvent conversationBuildEvent = new ConversationBuildEvent(roomData);
			userOutputStreams.get(new UserId(userName)).writeObject(conversationBuildEvent);
		} catch (IOException ex) {
			System.err.println("sendDirectMessage 익셉션 발생. " + ex);
		}
	}

	/**
	 * 방을 올바르게 만들거나 추가하기 위해 클라이언트에게 정보를 보내는 메소드
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
	 * 사용자에게 안내를 보내는 데 사용되는 메소드
	 * 
	 * @param messageObject
	 */
	public void alert(AlertToClientEvent messageObject) {
		try {
			userOutputStreams.get(new UserId(messageObject.getUserName())).writeObject(messageObject);
			userOutputStreams.remove(new UserId(messageObject.getUserName()));
		} catch (IOException ex) {
			System.err.println("alert 익셉션 발생. " + ex);
		}
	}
}
