package chattingServer.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import chattingClient.clientSideEvent.CreateNewRoomEvent;
import chattingClient.clientSideEvent.JoinExistingRoomEvent;
import chattingClient.clientSideEvent.QuitChattingEvent;
import chattingClient.clientSideEvent.SendMessageEvent;
import chattingClient.clientSideEvent.ClientSideEvent;
import chattingServer.serverSideEvent.AlertToClientEvent;
import chattingServer.connection.ConnectionHandler;
import chattingServer.model.UserActionProcessor;

/**
 * Ŭ���̾�Ʈ�� ���� ���� ������ ����� ����ϴ� ��Ʈ�ѷ� Ŭ����.
 */
public class Controller {
	/** �̺�Ʈ ť */
	private final BlockingQueue<ClientSideEvent> eventQueue;
	/** �� */
	private final UserActionProcessor userActionProcessor;
	/** �̺�Ʈ ó�� ���� �� */
	private final Map<Class<? extends ClientSideEvent>, ClientSideEventStrategy> strategyMap;
	/** ������ ���� ���� */
	private final ConnectionHandler connectionHandler;

	/**
	 * ������ �Ű� ������ ������� ��Ʈ���� ����� ������
	 * 
	 * @param eventQueue
	 * @param userActionProcessor
	 * @param connectionHandler   ����ڰ� ���� ��Ʈ���� ���� ������ �����ϰ� �޽����� �����ϴ� ������ ������ Ŭ����
	 */
	public Controller(final BlockingQueue<ClientSideEvent> eventQueue, final UserActionProcessor userActionProcessor,
			final ConnectionHandler connectionHandler) {
		
		this.eventQueue = eventQueue;
		this.userActionProcessor = userActionProcessor;
		this.connectionHandler = connectionHandler;

		// �̺�Ʈ ó�� ��å �� �ۼ�
		strategyMap = new HashMap<Class<? extends ClientSideEvent>, ClientSideEventStrategy>();
		strategyMap.put(CreateNewRoomEvent.class, new CreateNewRoomStrategy());
		strategyMap.put(JoinExistingRoomEvent.class, new JoinExistingRoomStrategy());
		strategyMap.put(SendMessageEvent.class, new SendMessageStrategy());
		strategyMap.put(QuitChattingEvent.class, new QuitChattingStrategy());
	}

	/**
	 * ��Ʈ�ѷ��� �ֿ� �޼ҵ�� �̺�Ʈ�� ��ٸ� ���� �ùٸ��� ó���մϴ�.
	 */
	public void work() {
		while (true) {
			try {
				ClientSideEvent clientdEvent = eventQueue.take();
				ClientSideEventStrategy clientSideEventStrategy = strategyMap.get(clientdEvent.getClass());
				clientSideEventStrategy.execute(clientdEvent);
			} catch (InterruptedException e) {
				// ��Ʈ�ѷ��� �̺�Ʈ�� ��Ÿ�� ������ �Ͻ� �����ؾ��ϹǷ� �ƹ� �͵����� �ʽ��ϴ�.
			}
		}
	}

	/**
	 * �̺�Ʈ�� ó���ϴ� ���� Ŭ������ �߻� �⺻ Ŭ�����Դϴ�
	 */
	abstract class ClientSideEventStrategy {
		/**
		 * �־��� �̺�Ʈ�� ���񽺸� ����ϴ� �߻� �޼ҵ�.
		 * 
		 * @param applicationEvent �����Ǿ���ϴ� ���� ���α׷� �̺�Ʈ
		 */
		abstract void execute(final ClientSideEvent clientSideEvent);
	}

	/**
	 * �� ���� ����� ����ڸ� ��ϴ� ������ �����ϴ� ���� Ŭ����
	 */
	class CreateNewRoomStrategy extends ClientSideEventStrategy {
		void execute(final ClientSideEvent clientSideEvent) {
			CreateNewRoomEvent createNewRoomEvent = (CreateNewRoomEvent) clientSideEvent;
			if (userActionProcessor.createNewRoom(createNewRoomEvent)) {
				connectionHandler.buildChatRoomView(createNewRoomEvent.getUserName(), createNewRoomEvent.getRoomName());
			} else {
				connectionHandler.alert(new AlertToClientEvent("\r\n" + "�־��� �̸��� ���� �̹� �ֽ��ϴ�.", createNewRoomEvent.getUserName()));
			}
		}
	}

	/**
	 * ���� �濡 �շ��ϱ����� ����� ���� ������ �����ϴ� ���� Ŭ����
	 */
	class JoinExistingRoomStrategy extends ClientSideEventStrategy {
		void execute(final ClientSideEvent clientSideEvent) {
			JoinExistingRoomEvent joinExistingRoomEvent = (JoinExistingRoomEvent) clientSideEvent;
			if (userActionProcessor.addUserToSpecificRoom(joinExistingRoomEvent)) {
				connectionHandler.buildChatRoomView(joinExistingRoomEvent.getUserName(), joinExistingRoomEvent.getRoomName());
			} else {
				connectionHandler.alert(new AlertToClientEvent("�����Ϸ��� ���� �������� �ʽ��ϴ�.", joinExistingRoomEvent.getUserName()));
			}
		}
	}

	/**
	 * ����ڰ� �� �޽����� ������ ���� ������ �����ϴ� ���� Ŭ����
	 */
	class SendMessageStrategy extends ClientSideEventStrategy {
		void execute(final ClientSideEvent clientSideEvent) {
			SendMessageEvent sendMessageEvent = (SendMessageEvent) clientSideEvent;
			userActionProcessor.addMessageOfUser(sendMessageEvent);
			connectionHandler.sendMessageToAll(userActionProcessor.getRoomData(sendMessageEvent));
		}
	}

	/**
	 * ���� ������� ��Ż ������ �����ϴ� ���� Ŭ�����Դϴ�.
	 */
	class QuitChattingStrategy extends ClientSideEventStrategy {
		void execute(final ClientSideEvent clientSideEvent) {
			QuitChattingEvent quitChattingEvent = (QuitChattingEvent) clientSideEvent;
			userActionProcessor.setUserToInactive(quitChattingEvent);
		}
	}
}
