package chattingServer.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import chattingClient.clientSideEvent.CreateNewRoomEvent;
import chattingClient.clientSideEvent.JoinExistingRoomEvent;
import chattingClient.clientSideEvent.QuitChattingEvent;
import chattingClient.clientSideEvent.ClientSideEvent;
import chattingServer.serverSideEvent.AlertToClientEvent;
import chattingServer.model.UserId;

/**
 * 클라이언트로부터 이벤트를 수신하여 큐에 추가하는 역할을 담당하는 클래스
 */
public class ConnectionThread extends Thread {
	/** socket */
	private final Socket socket;
	/** 입력 스트림 */
	private ObjectInputStream inputStream;
	/** 출력 스트림 */
	private ObjectOutputStream outputStream;
	/** 사용자별 출력 스트림 맵 */
	private final HashMap<UserId, ObjectOutputStream> userOutputStreams;
	/** 블로킹 큐 */
	private final BlockingQueue<ClientSideEvent> eventQueue;
	/** 작업중 식별 플래그 */
	private boolean isRunning;

	/**
	 * 주어진 사용자로부터 연결에 대한 새로운 링크를 생성하는 생성자
	 * 
	 * @param socket        
	 * @param eventQueue        
	 * @param userOutputStreams
	 */
	public ConnectionThread(final Socket socket, final BlockingQueue<ClientSideEvent> eventQueue, final HashMap<UserId, ObjectOutputStream> userOutputStreams) {
		this.socket = socket;
		this.eventQueue = eventQueue;
		this.userOutputStreams = userOutputStreams;
		this.isRunning = true;

		try {
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			inputStream = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			System.err.println("클라이언트 스트림을 생성 할 때 예외 발생. " + ex);
			return;
		}
	}

	/**
	 * 클라이언트에서 이벤트를 수신하여 블로킹 큐에 추가하는 클래스의 기본 루프
	 */
	public void run() {
		ClientSideEvent appEvent;
		while (isRunning) {
			try {
				appEvent = (ClientSideEvent) inputStream.readObject();

				/** 클라이언트가 방에 들어 오거나 방을 만들 때, 출력 스트림을 작성 */
				if (appEvent instanceof CreateNewRoomEvent) {
					CreateNewRoomEvent createNewRoomEvent = (CreateNewRoomEvent) appEvent;

					/** 맵에 추가하기 전에 주어진 사용자가 이미 존재하는지 확인해야합니다. */
					if (userOutputStreams.get(new UserId(createNewRoomEvent.getUserName())) != null) {
						outputStream.writeObject(new AlertToClientEvent("주어진 이름의 사용자가 이미 있습니다.", createNewRoomEvent.getUserName()));
					}
					/** 존재하지 않으면 맵에 추가합니다. */
					else {
						userOutputStreams.put(new UserId(createNewRoomEvent.getUserName()), outputStream);
						eventQueue.add(appEvent);
					}
				} else if (appEvent instanceof JoinExistingRoomEvent) {
					JoinExistingRoomEvent joinNewRoomInformation = (JoinExistingRoomEvent) appEvent;

					/** 맵에 추가하기 전에 주어진 사용자가 이미 존재하는지 확인해야합니다. */
					if (userOutputStreams.get(new UserId(joinNewRoomInformation.getUserName())) != null) {
						outputStream.writeObject(new AlertToClientEvent("주어진 이름의 사용자가 이미 있습니다.", joinNewRoomInformation.getUserName()));
					} else {
						/** 존재하지 않으면 맵에 추가합니다. */
						userOutputStreams.put(new UserId(joinNewRoomInformation.getUserName()), outputStream);
						eventQueue.add(appEvent);
					}
				}
				/**
				 * 채팅에서 QuitChattingEvent를 얻는다면 우리는 멈추고 컨트롤러에 이벤트를 보내야합니다.
				 */
				else if (appEvent instanceof QuitChattingEvent) {
					eventQueue.add(appEvent);
					socket.close();
					isRunning = false;
				} else {
					eventQueue.add(appEvent);
				}
			} catch (IOException ex) {
				try {
					socket.close();
					isRunning = false;
				} catch (IOException e) {
					System.err.println(e);
				}
			} catch (ClassNotFoundException ex) {
				System.err.println("ClassNotFoundException" + ex);
			} catch (NullPointerException ex) {
				System.err.println("NullPointerException" + ex);
			} catch (ClassCastException ex) {
				System.err.println(ex);
			}
		}
	}
}
