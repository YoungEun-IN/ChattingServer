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
 * Ŭ���̾�Ʈ�� ��û������ �޾� �� ���ϸ��� ConnectionThread�� �Ļ���Ų��.
 */
class ServerSocketThread extends Thread {
	/** Socket ���� */
	private final ServerSocket serverSocket;
	/** ���ŷ ť */
	private final BlockingQueue<ClientSideEvent> eventQueue;
	/** �ؽø� */
	private final HashMap<UserId, ObjectOutputStream> oosMap;

	/**
	 * ���ο� ���� ��ũ�� �����ϴ� ������
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
	 * run�޼ҵ�
	 */
	public void run() {
		System.out.println("������ ��Ʈ�� �� ������ ����. ��Ʈ��ȣ : " + serverSocket.getLocalPort());
		while (true) {
			try {
				Socket socket = serverSocket.accept();

				InetSocketAddress remoteSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
				String remoteHostName = remoteSocketAddress.getAddress().getHostAddress();
				int remoteHostPort = remoteSocketAddress.getPort();

				System.out.println("������ Ŭ���̾�Ʈ �����. connected socket address:  " + remoteHostName + " port : " + remoteHostPort);
				ConnectionThread connectionThread = new ConnectionThread(socket, eventQueue, oosMap);
				connectionThread.start();
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
}
