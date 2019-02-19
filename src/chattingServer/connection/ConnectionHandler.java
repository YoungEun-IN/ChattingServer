package chattingServer.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import chattingClient.clientEvent.CreateNewRoomEvent;
import chattingClient.clientEvent.JoinExistingRoomEvent;
import chattingClient.clientEvent.QuitChattingEvent;
import chattingClient.clientEvent.ClientdEvent;
import chattingServer.serverEvent.AlertToClientEvent;
import chattingServer.model.UserId;

/**
 * Ŭ���̾�Ʈ�κ��� �̺�Ʈ�� �����Ͽ� ť�� �߰��ϴ� ������ ����ϴ� Ŭ����
 */
public class ConnectionHandler extends Thread {
	/** userSocket */
	private final Socket userSocket;
	/** �Է� ��Ʈ�� */
	private ObjectInputStream inputStream;
	/** ��� ��Ʈ�� */
	private ObjectOutputStream outputStream;
	/** ����ں� ��� ��Ʈ�� �� */
	private final HashMap<UserId, ObjectOutputStream> userOutputStreams;
	/** ����ŷ ť */
	private final BlockingQueue<ClientdEvent> eventQueue;
	/** �۾��� �ĺ� �÷��� */
	private boolean isRunning;

	/**
	 * �־��� ����ڷκ��� ���ῡ ���� ���ο� ��ũ�� �����ϴ� ������
	 * 
	 * @param userSocket        
	 * @param eventQueue        
	 * @param userOutputStreams
	 */
	public ConnectionHandler(final Socket userSocket, final BlockingQueue<ClientdEvent> eventQueue, final HashMap<UserId, ObjectOutputStream> userOutputStreams) {
		this.userSocket = userSocket;
		this.eventQueue = eventQueue;
		this.userOutputStreams = userOutputStreams;
		this.isRunning = true;

		try {
			outputStream = new ObjectOutputStream(userSocket.getOutputStream());
			inputStream = new ObjectInputStream(userSocket.getInputStream());
		} catch (IOException ex) {
			System.err.println("Ŭ���̾�Ʈ ��Ʈ���� ���� �� �� ���� �߻�. " + ex);
			return;
		}
	}

	/**
	 * Ŭ���̾�Ʈ���� �̺�Ʈ�� �����Ͽ� ����ŷ ť�� �߰��ϴ� Ŭ������ �⺻ ����
	 */
	public void run() {
		ClientdEvent appEvent;
		while (isRunning) {
			try {
				appEvent = (ClientdEvent) inputStream.readObject();

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
				 * ä�ÿ��� ����� �ⱸ�� ��Ÿ���� ��ü�� ��´ٸ� �츮�� ���߰� ��Ʈ�ѷ��� �̺�Ʈ�� �������մϴ�.
				 */
				else if (appEvent instanceof QuitChattingEvent) {
					eventQueue.add(appEvent);
					userSocket.close();
					isRunning = false;
				} else {
					eventQueue.add(appEvent);
				}
			} catch (IOException ex) {
				try {
					userSocket.close();
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