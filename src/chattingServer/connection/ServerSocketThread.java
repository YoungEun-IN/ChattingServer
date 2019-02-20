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
 * 클라이언트의 요청소켓을 받아 각 소켓마다 ConnectionThread를 파생시킨다.
 */
class ServerSocketThread extends Thread {
	/** Socket 서버 */
	private final ServerSocket serverSocket;
	/** 블로킹 큐 */
	private final BlockingQueue<ClientSideEvent> eventQueue;
	/** 해시맵 */
	private final HashMap<UserId, ObjectOutputStream> oosMap;

	/**
	 * 새로운 연결 링크를 생성하는 생성자
	 * 
	 * @param serverSocket
	 * @param eventQueue
	 * @param oosMap
	 */
	public ServerSocketThread(final ServerSocket serverSocket, final BlockingQueue<ClientSideEvent> eventQueue,
			final HashMap<UserId, ObjectOutputStream> oosMap) {
		this.serverSocket = serverSocket;
		this.eventQueue = eventQueue;
		this.oosMap = oosMap;
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
				ConnectionThread connectionThread = new ConnectionThread(socket, eventQueue, oosMap);
				connectionThread.start();
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
}
