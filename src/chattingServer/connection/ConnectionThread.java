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
 * Ŭ���̾�Ʈ�� ������ �м��� ä�ù溰�� ������ �����ϰ� �޽����� �ѷ��ش�. ������ �濡 �����Ҷ����� �� �����尡 �ϳ��� �����.
 */
public class ConnectionThread extends Thread {
	/** socket */
	private final Socket socket;
	/** �Է� ��Ʈ�� */
	private ObjectInputStream ois;
	/** ��� ��Ʈ�� */
	private ObjectOutputStream oos;
	/** ����ں� ��� ��Ʈ�� �� */
	private final HashMap<UserId, ObjectOutputStream> oosMap;
	/** ���ŷ ť */
	private final BlockingQueue<ClientSideEvent> eventQueue;
	/** �۾��� �ĺ� �÷��� */
	private boolean isRunning;

	/**
	 * �־��� ����ڷκ��� ���ῡ ���� ���ο� ��ũ�� �����ϴ� ������
	 * 
	 * @param socket
	 * @param eventQueue �� �濡 �ִ� ������� ť
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
			System.err.println("��Ʈ���� ���� �� �� ���� �߻�. " + ex);
			return;
		}
	}

	/**
	 * Ŭ���̾�Ʈ���� �̺�Ʈ�� �����Ͽ� ���ŷ ť�� �߰��ϴ� Ŭ������ �⺻ ����
	 */
	public void run() {
		ClientSideEvent clientSideEvent;
		while (isRunning) {
			try {
				clientSideEvent = (ClientSideEvent) ois.readObject();

				/** Ŭ���̾�Ʈ�� �濡 ��� ���ų� ���� ���� ��, ��� ��Ʈ���� �ۼ� */
				if (clientSideEvent instanceof CreateNewRoomEvent) {
					CreateNewRoomEvent createNewRoomEvent = (CreateNewRoomEvent) clientSideEvent;

					/** �ʿ� �߰��ϱ� ���� �־��� ����ڰ� �̹� �����ϴ��� Ȯ���ؾ��մϴ�. */
					if (oosMap.get(new UserId(createNewRoomEvent.getUserName())) != null) {
						oos.writeObject(new AlertToClientEvent("�־��� �̸��� ����ڰ� �̹� �ֽ��ϴ�.", createNewRoomEvent.getUserName()));
					}
					/** �������� ������ �ʿ� �߰��մϴ�. */
					else {
						oosMap.put(new UserId(createNewRoomEvent.getUserName()), oos);
						eventQueue.add(clientSideEvent);
					}
				} else if (clientSideEvent instanceof JoinExistingRoomEvent) {
					JoinExistingRoomEvent joinNewRoomInformation = (JoinExistingRoomEvent) clientSideEvent;

					/** �ʿ� �߰��ϱ� ���� �־��� ����ڰ� �̹� �����ϴ��� Ȯ���ؾ��մϴ�. */
					if (oosMap.get(new UserId(joinNewRoomInformation.getUserName())) != null) {
						oos.writeObject(new AlertToClientEvent("�־��� �̸��� ����ڰ� �̹� �ֽ��ϴ�.", joinNewRoomInformation.getUserName()));
					} else {
						/** �������� ������ �ʿ� �߰��մϴ�. */
						oosMap.put(new UserId(joinNewRoomInformation.getUserName()), oos);
						eventQueue.add(clientSideEvent);
					}
				}
				/**
				 * ä�ÿ��� QuitChattingEvent�� ��´ٸ� �츮�� ���߰� ��Ʈ�ѷ��� �̺�Ʈ�� �������մϴ�.
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
