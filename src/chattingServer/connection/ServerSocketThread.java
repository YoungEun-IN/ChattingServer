package chattingServer.connection;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import chattingClient.clientSideEvent.ClientSideEvent;
import chattingServer.model.UserId;

/**
 * ServerSocket을 관리하는 클래스
 */
class ServerSocketThread extends Thread {
	/** Socket 서버 */
	private final ServerSocket serverSocket;
	/** 블로킹 큐 */
	private final BlockingQueue<ClientSideEvent> eventQueue;
	/** 해시맵 */
	private final HashMap<UserId, ObjectOutputStream> userOutputStreams;

	/**
	 * 새로운 연결 링크를 생성하는 생성자
	 * 
	 * @param serverSocket
	 * @param eventQueue
	 * @param userOutputStreams
	 */
	public ServerSocketThread(final ServerSocket serverSocket, final BlockingQueue<ClientSideEvent> eventQueue,
			final HashMap<UserId, ObjectOutputStream> userOutputStreams) {
		this.serverSocket = serverSocket;
		this.eventQueue = eventQueue;
		this.userOutputStreams = userOutputStreams;
	}

	/**
	 * run메소드
	 */
	public void run() {
		System.out.println("서버가 포트에 새 연결을 시작. 포트번호 : " + serverSocket.getLocalPort());
		while (true) {
			try {
				Socket socket = serverSocket.accept();

				InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
				String remoteHostName = remoteSocketAddress.getAddress().getHostAddress();
				int remoteHostPort = remoteSocketAddress.getPort();

				System.out.println("서버에 클라이언트 연결됨. connected socket address:  " + remoteHostName + " port : " + remoteHostPort);
				ConnectionThread connectionThread = new ConnectionThread(socket, eventQueue, userOutputStreams);
				connectionThread.start();
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
}
