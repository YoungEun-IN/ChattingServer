package chattingServer.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import chattingClient.clientSideEvent.ClientSideEvent;
import chattingClient.clientSideEvent.CreateNewRoomEvent;
import chattingClient.clientSideEvent.JoinExistingRoomEvent;
import chattingClient.clientSideEvent.QuitChattingEvent;
import chattingClient.clientSideEvent.SendMessageEvent;
import chattingServer.model.UserId;
import chattingServer.serverSideEvent.AlertToClientEvent;

/**
 * 클라이언트의 소켓을 분석해 채팅방별로 관리해 적절하게 메시지를 뿌려준다. 유저가 방에 접속할때마다 이 쓰레드가 하나씩 생긴다.
 */
public class ConnectionThread extends Thread {
	/** socket */
	private final Socket socket;
	/** 입력 스트림 */
	private ObjectInputStream ois;
	/** 출력 스트림 */
	private ObjectOutputStream oos;
	/** 사용자별 출력 스트림 맵 */
	private final HashMap<UserId, ObjectOutputStream> oosMap;
	/** 블로킹 큐 */
	private final BlockingQueue<ClientSideEvent> eventQueue;
	/** 작업중 식별 플래그 */
	private boolean isRunning;

	/**
	 * 주어진 사용자로부터 연결에 대한 새로운 링크를 생성하는 생성자
	 * 
	 * @param socket
	 * @param eventQueue 한 방에 있는 사용자의 큐
	 * @param oosMap
	 */
	public ConnectionThread(final Socket socket, final BlockingQueue<ClientSideEvent> eventQueue, final HashMap<UserId, ObjectOutputStream> oosMap) {
		this.socket = socket;
		this.eventQueue = eventQueue;
		this.oosMap = oosMap;
		this.isRunning = true;

		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException ex) {
			System.err.println("스트림을 생성 할 때 예외 발생. " + ex);
			return;
		}
	}

	/**
	 * 클라이언트에서 이벤트를 수신하여 블로킹 큐에 추가하는 클래스의 기본 루프
	 */
	public void run() {
		ClientSideEvent clientSideEvent;
		while (isRunning) {
			try {
				clientSideEvent = (ClientSideEvent) ois.readObject();

				/** 클라이언트가 방에 들어 오거나 방을 만들 때, 출력 스트림을 작성 */
				if (clientSideEvent instanceof CreateNewRoomEvent) {
					CreateNewRoomEvent createNewRoomEvent = (CreateNewRoomEvent) clientSideEvent;

					/** 맵에 추가하기 전에 주어진 사용자가 이미 존재하는지 확인해야합니다. */
					if (oosMap.get(new UserId(createNewRoomEvent.getUserName())) != null) {
						oos.writeObject(new AlertToClientEvent("주어진 이름의 사용자가 이미 있습니다.", createNewRoomEvent.getUserName()));
					}
					/** 존재하지 않으면 맵에 추가합니다. */
					else {
						oosMap.put(new UserId(createNewRoomEvent.getUserName()), oos);
						eventQueue.add(clientSideEvent);
					}
				} else if (clientSideEvent instanceof JoinExistingRoomEvent) {
					JoinExistingRoomEvent joinNewRoomInformation = (JoinExistingRoomEvent) clientSideEvent;

					/** 맵에 추가하기 전에 주어진 사용자가 이미 존재하는지 확인해야합니다. */
					if (oosMap.get(new UserId(joinNewRoomInformation.getUserName())) != null) {
						oos.writeObject(new AlertToClientEvent("주어진 이름의 사용자가 이미 있습니다.", joinNewRoomInformation.getUserName()));
					} else {
						/** 존재하지 않으면 맵에 추가합니다. */
						oosMap.put(new UserId(joinNewRoomInformation.getUserName()), oos);
						eventQueue.add(clientSideEvent);
					}
				}
				/**
				 * 채팅에서 QuitChattingEvent를 얻는다면 우리는 멈추고 컨트롤러에 이벤트를 보내야합니다.
				 */
				else if (clientSideEvent instanceof QuitChattingEvent) {
					eventQueue.add(clientSideEvent);
					socket.close();
					isRunning = false;
				} else if (clientSideEvent instanceof SendMessageEvent) {
					eventQueue.add(clientSideEvent);
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
