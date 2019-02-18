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
 * �츮���� å������ Ŭ������ ������ Ŭ���̾�Ʈ�� ���ο� ������ �� ���ְ��մϴ�.
 */
class ServerSocketHandler extends Thread {
	/** Socket ���� */
	private final ServerSocket serverSocket;
	/** ���ŷ ť */
	private final BlockingQueue<ServerHandledEvent> eventQueue;
	/** ��� ��Ʈ���� ����Ͽ� �� �ۼ� */
	private final HashMap<UserId, ObjectOutputStream> userOutputStreams;

	/**
	 * ���ο� ���� ��ũ�� �����ϴ� ������
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
	 * �츮�� ���ο� ������ �������ִ� Ŭ������ �� ������ Ŭ���̾�Ʈ�� ���Դϴ�.
	 */
	public void run() {
		System.out.println("������ ��Ʈ�� �� ������ �����ϱ� �����߽��ϴ�.: " + serverSocket.getLocalPort());
		while (true) {
			try {
				Socket userSocket = serverSocket.accept();
				
				InetSocketAddress remoteSocketAddress = (InetSocketAddress) userSocket.getRemoteSocketAddress();
				String remoteHostName = remoteSocketAddress.getAddress().getHostAddress();
				int remoteHostPort = remoteSocketAddress.getPort();
				
				System.out.println("������ Ŭ���̾�Ʈ �����. connected socket address:" + remoteHostName + "port:" + remoteHostPort);
				ConnectionHandler userConnection = new ConnectionHandler(userSocket, eventQueue, userOutputStreams);
				userConnection.start();
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
}
