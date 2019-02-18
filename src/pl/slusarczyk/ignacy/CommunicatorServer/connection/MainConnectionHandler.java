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
 * 사용자가 종료 스트림에 대한 정보를 저장하고 메시지를 배포하는 서버의 마스터 클래스
 */
public class MainConnectionHandler {
	/** userId가 키인 출력 스트림을 사용자가 보유하고있는 맵 */
	private final HashMap<UserId, ObjectOutputStream> userOutputStreams;
	/** 서버소켓 */
	private ServerSocket serverSocket;
	/** 포트 넘버 */
	private final int portNumber;
	/** 이벤트 큐 */
	private final BlockingQueue<ServerHandledEvent> eventQueue;

	/**
	 * 지정된 포트에 서버를 연결하는 생성자. 새 연결을 연결하기 위해 별도의 링크를 만듭니다.
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
				sendDirectMessage(userData.getUserIdData(), roomData);
			}
		}
	}

	/**
	 * 주어진 닉네임을 가진 사용자에게 직접 메시지를 보내는 메소드
	 * 
	 * @param userName
	 * @param usersConversation 사용자 간의 대화
	 * @param listOfUsers       현재 채팅중인 사용자 목록
	 */
	public void sendDirectMessage(final UserIdData userIdData, final RoomData roomData) {
		try {
			ConversationServerEvent userConversationToSend = new ConversationServerEvent(roomData);
			userOutputStreams.get(new UserId(userIdData.getUserName())).writeObject(userConversationToSend);
		} catch (IOException ex) {
			System.err.println("익셉션 발생. " + ex);
		}
	}

	/**
	 * 방을 올바르게 만들거나 추가하기 위해 클라이언트에게 확인을 보내는 메소드
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
	 * 이 메서드는 사용자에게 메시지를 보내는 데 사용됩니다.
	 * 
	 * @param messageObject 표시 할 메시지와 정보 메시지를 보내는 사용자의 닉네임 포함
	 */
	public void sendMessage(InfoServerEvent messageObject) {
		try {
			userOutputStreams.get(new UserId(messageObject.getUserIDData().getUserName())).writeObject(messageObject);
			userOutputStreams.remove(new UserId(messageObject.getUserIDData().getUserName()));
		} catch (IOException ex) {
			System.err.println("익셉션 발생. " + ex);
		}
	}
}
