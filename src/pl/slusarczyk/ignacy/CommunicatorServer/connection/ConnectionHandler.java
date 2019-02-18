package pl.slusarczyk.ignacy.CommunicatorServer.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent.CreateNewRoomEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent.JoinExistingRoomEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent.QuitChattingEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandleEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandleEvent.MessageServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.UserId;

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
	/** ���ŷ ť */
	private final BlockingQueue<ServerHandledEvent> eventQueue;
	/** �۾��� �ĺ� �÷��� */
	private boolean isRunning;

	/**
	 * �־��� ����ڷκ��� ���ῡ ���� ���ο� ��ũ�� �����ϴ� ������
	 * 
	 * @param userSocket        
	 * @param eventQueue        
	 * @param userOutputStreams
	 */
	public ConnectionHandler(final Socket userSocket, final BlockingQueue<ServerHandledEvent> eventQueue, final HashMap<UserId, ObjectOutputStream> userOutputStreams) {
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
	 * Ŭ���̾�Ʈ���� �̺�Ʈ�� �����Ͽ� ���ŷ ť�� �߰��ϴ� Ŭ������ �⺻ ����
	 */
	public void run() {
		ServerHandledEvent appEvent;
		while (isRunning) {
			try {
				appEvent = (ServerHandledEvent) inputStream.readObject();

				/** Ŭ���̾�Ʈ�� �濡 ��� ���ų� ���� ���� ��, ��� ��Ʈ���� �ۼ� */
				if (appEvent instanceof CreateNewRoomEvent) {
					CreateNewRoomEvent createNewRoomEvent = (CreateNewRoomEvent) appEvent;

					/** �ʿ� �߰��ϱ� ���� �־��� ����ڰ� �̹� �����ϴ��� Ȯ���ؾ��մϴ�. */
					if (userOutputStreams.get(new UserId(createNewRoomEvent.getUserIdData().getUserName())) != null) {
						outputStream.writeObject(new MessageServerEvent("�־��� �̸��� ����ڰ� �̹� �ֽ��ϴ�.", createNewRoomEvent.getUserIdData()));
					}
					/** �������� ������ �ʿ� �߰��մϴ�. */
					else {
						userOutputStreams.put(new UserId(createNewRoomEvent.getUserIdData().getUserName()), outputStream);
						eventQueue.add(appEvent);
					}
				} else if (appEvent instanceof JoinExistingRoomEvent) {
					JoinExistingRoomEvent joinNewRoomInformation = (JoinExistingRoomEvent) appEvent;

					/** �ʿ� �߰��ϱ� ���� �־��� ����ڰ� �̹� �����ϴ��� Ȯ���ؾ��մϴ�. */
					if (userOutputStreams.get(new UserId(joinNewRoomInformation.getUserIdData().getUserName())) != null) {
						outputStream.writeObject(new MessageServerEvent("�־��� �̸��� ����ڰ� �̹� �ֽ��ϴ�.", joinNewRoomInformation.getUserIdData()));
					} else {
						/** �������� ������ �ʿ� �߰��մϴ�. */
						userOutputStreams.put(new UserId(joinNewRoomInformation.getUserIdData().getUserName()), outputStream);
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
