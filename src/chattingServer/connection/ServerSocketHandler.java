package chattingServer.connection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import chattingClient.serverHandleEvent.ServerHandledEvent;
import chattingServer.model.UserId;

/**
 * 우리에게 책임지는 클래스는 서버가 클라이언트와 새로운 연결을 할 수있게합니다.
 */
class ServerSocketHandler extends Thread {
	/** Socket 서버 */
	private final ServerSocket serverSocket;
	/** 블로킹 큐 */
	private final BlockingQueue<ServerHandledEvent> eventQueue;
	/** 출력 스트림을 사용하여 맵 작성 */
	private final HashMap<UserId, ObjectOutputStream> userOutputStreams;

	/**
	 * 새로운 연결 링크를 생성하는 생성자
	 * 
	 * @param serverSocket
	 * @param eventQueue
	 * @param userOutputStreams
	 */
	public ServerSocketHandler(final ServerSocket serverSocket, final BlockingQueue<ServerHandledEvent> eventQueue,
			final HashMap<UserId, ObjectOutputStream> userOutputStreams) {
		this.serverSocket = serverSocket;
		this.eventQueue = eventQueue;
		this.userOutputStreams = userOutputStreams;
	}

	/**
	 * 우리가 새로운 연결을 가지고있는 클래스의 주 루프는 클라이언트의 것입니다.
	 */
	public void run() {
		System.out.println("서버가 포트에 새 연결을 연결하기 시작했습니다.: " + serverSocket.getLocalPort());
		while (true) {
			try {
				Socket userSocket = serverSocket.accept();
				
				InetSocketAddress remoteSocketAddress = (InetSocketAddress) userSocket.getRemoteSocketAddress();
				String remoteHostName = remoteSocketAddress.getAddress().getHostAddress();
				int remoteHostPort = remoteSocketAddress.getPort();
				
				System.out.println("서버에 클라이언트 연결됨. connected socket address:" + remoteHostName + "port:" + remoteHostPort);
				ConnectionHandler userConnection = new ConnectionHandler(userSocket, eventQueue, userOutputStreams);
				userConnection.start();
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
}
