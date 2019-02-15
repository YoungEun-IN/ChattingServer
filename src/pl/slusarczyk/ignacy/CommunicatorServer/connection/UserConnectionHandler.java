package pl.slusarczyk.ignacy.CommunicatorServer.connection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ClientLeftRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.JoinExistingRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.CreateNewRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.InfoServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.model.UserId;

/**
 * Ŭ���̾�Ʈ�κ��� �̺�Ʈ�� �����Ͽ� ť�� �߰��ϴ� ������ ����ϴ� Ŭ����
 */
public class UserConnectionHandler extends Thread {
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
	private boolean running;

	/**
	 * �־��� ����ڷκ��� ���ῡ ���� ���ο� ��ũ�� �����ϴ� ������
	 * 
	 * @param userSocket        
	 * @param eventQueue        
	 * @param userOutputStreams
	 */
	public UserConnectionHandler(final Socket userSocket, final BlockingQueue<ServerHandledEvent> eventQueue, final HashMap<UserId, ObjectOutputStream> userOutputStreams) {
		this.userSocket = userSocket;
		this.eventQueue = eventQueue;
		this.userOutputStreams = userOutputStreams;
		this.running = true;

		try {
			outputStream = new ObjectOutputStream(userSocket.getOutputStream());
			inputStream = new ObjectInputStream(userSocket.getInputStream());
		} catch (IOException ex) {
			System.err.println("Ŭ���̾�Ʈ�� �Բ� ��Ʈ���� ���� �� �� ���� �߻�. " + ex);
			return;
		}
	}

	/**
	 * Ŭ���̾�Ʈ���� �̺�Ʈ�� �����Ͽ� ���ŷ ť�� �߰��ϴ� Ŭ������ �⺻ ����
	 */
	public void run() {
		ServerHandledEvent appEvent;
		while (running) {
			try {
				appEvent = (ServerHandledEvent) inputStream.readObject();

				/** Ŭ���̾�Ʈ�� �濡 ��� ���ų� ���� ���� ��, ��� ��Ʈ���� �ۼ��ؾ��մϴ�. */
				if (appEvent instanceof CreateNewRoom) {
					CreateNewRoom createNewRoomInformation = (CreateNewRoom) appEvent;

					/** �ʿ� �߰��ϱ� ���� �־��� ����ڰ� �̹� �����ϴ��� Ȯ���ؾ��մϴ�. */
					if (userOutputStreams.get(new UserId(createNewRoomInformation.getUserIdData().getUserName())) != null) {
						outputStream.writeObject(new InfoServerEvent("�־��� �̸��� ����ڰ� �̹� �ֽ��ϴ�.", createNewRoomInformation.getUserIdData()));
					}
					/** �������� ������ �ʿ� �߰��մϴ�. */
					else {
						userOutputStreams.put(new UserId(createNewRoomInformation.getUserIdData().getUserName()), outputStream);
						eventQueue.add(appEvent);
					}
				} else if (appEvent instanceof JoinExistingRoom) {
					JoinExistingRoom joinNewRoomInformation = (JoinExistingRoom) appEvent;

					/** �ʿ� �߰��ϱ� ���� �־��� ����ڰ� �̹� �����ϴ��� Ȯ���ؾ��մϴ�. */
					if (userOutputStreams.get(new UserId(joinNewRoomInformation.getUserIdData().getUserName())) != null) {
						outputStream.writeObject(new InfoServerEvent("�־��� �̸��� ����ڰ� �̹� �ֽ��ϴ�.", joinNewRoomInformation.getUserIdData()));
					} else {
						/** �������� ������ �ʿ� �߰��մϴ�. */
						userOutputStreams.put(new UserId(joinNewRoomInformation.getUserIdData().getUserName()), outputStream);
						eventQueue.add(appEvent);
					}
				}
				/**
				 * ä�ÿ��� ����� �ⱸ�� ��Ÿ���� ��ü�� ��´ٸ� �츮�� ���߰� ��Ʈ�ѷ��� �̺�Ʈ�� �������մϴ�.
				 */
				else if (appEvent instanceof ClientLeftRoom) {
					eventQueue.add(appEvent);
					userSocket.close();
					running = false;
				} else {
					eventQueue.add(appEvent);
				}
			} catch (IOException ex) {
				try {
					userSocket.close();
					running = false;
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
