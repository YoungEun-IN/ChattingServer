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
 * Ŭ���̾�Ʈ�κ��� �̺�Ʈ�� �����Ͽ� ť�� �߰��ϴ� ������ ����ϴ� Ŭ����
 */
public class ConnectionThread extends Thread {
	/** socket */
	private final Socket socket;
	/** �Է� ��Ʈ�� */
	private ObjectInputStream inputStream;
	/** ��� ��Ʈ�� */
	private ObjectOutputStream outputStream;
	/** ����ں� ��� ��Ʈ�� �� */
	private final HashMap<UserId, ObjectOutputStream> userOutputStreams;
	/** ���ŷ ť */
	private final BlockingQueue<ClientSideEvent> eventQueue;
	/** �۾��� �ĺ� �÷��� */
	private boolean isRunning;

	/**
	 * �־��� ����ڷκ��� ���ῡ ���� ���ο� ��ũ�� �����ϴ� ������
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
			System.err.println("Ŭ���̾�Ʈ ��Ʈ���� ���� �� �� ���� �߻�. " + ex);
			return;
		}
	}

	/**
	 * Ŭ���̾�Ʈ���� �̺�Ʈ�� �����Ͽ� ���ŷ ť�� �߰��ϴ� Ŭ������ �⺻ ����
	 */
	public void run() {
		ClientSideEvent appEvent;
		while (isRunning) {
			try {
				appEvent = (ClientSideEvent) inputStream.readObject();

				/** Ŭ���̾�Ʈ�� �濡 ��� ���ų� ���� ���� ��, ��� ��Ʈ���� �ۼ� */
				if (appEvent instanceof CreateNewRoomEvent) {
					CreateNewRoomEvent createNewRoomEvent = (CreateNewRoomEvent) appEvent;

					/** �ʿ� �߰��ϱ� ���� �־��� ����ڰ� �̹� �����ϴ��� Ȯ���ؾ��մϴ�. */
					if (userOutputStreams.get(new UserId(createNewRoomEvent.getUserName())) != null) {
						outputStream.writeObject(new AlertToClientEvent("�־��� �̸��� ����ڰ� �̹� �ֽ��ϴ�.", createNewRoomEvent.getUserName()));
					}
					/** �������� ������ �ʿ� �߰��մϴ�. */
					else {
						userOutputStreams.put(new UserId(createNewRoomEvent.getUserName()), outputStream);
						eventQueue.add(appEvent);
					}
				} else if (appEvent instanceof JoinExistingRoomEvent) {
					JoinExistingRoomEvent joinNewRoomInformation = (JoinExistingRoomEvent) appEvent;

					/** �ʿ� �߰��ϱ� ���� �־��� ����ڰ� �̹� �����ϴ��� Ȯ���ؾ��մϴ�. */
					if (userOutputStreams.get(new UserId(joinNewRoomInformation.getUserName())) != null) {
						outputStream.writeObject(new AlertToClientEvent("�־��� �̸��� ����ڰ� �̹� �ֽ��ϴ�.", joinNewRoomInformation.getUserName()));
					} else {
						/** �������� ������ �ʿ� �߰��մϴ�. */
						userOutputStreams.put(new UserId(joinNewRoomInformation.getUserName()), outputStream);
						eventQueue.add(appEvent);
					}
				}
				/**
				 * ä�ÿ��� QuitChattingEvent�� ��´ٸ� �츮�� ���߰� ��Ʈ�ѷ��� �̺�Ʈ�� �������մϴ�.
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
