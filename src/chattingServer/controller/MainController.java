package chattingServer.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import chattingClient.clientEvent.CreateNewRoomEvent;
import chattingClient.clientEvent.JoinExistingRoomEvent;
import chattingClient.clientEvent.QuitChattingEvent;
import chattingClient.clientEvent.SendMessageEvent;
import chattingClient.clientEvent.ClientdEvent;
import chattingServer.serverEvent.AlertToClientEvent;
import chattingServer.connection.MainConnectionHandler;
import chattingServer.model.UserActionProcessor;

/**
 * Ŭ���̾�Ʈ�� ���� ���� ������ ����� ����ϴ� ��Ʈ�ѷ� Ŭ����.
 */
public class MainController {
	/** �̺�Ʈ ť */
	private final BlockingQueue<ClientdEvent> eventQueue;
	/** �� */
	private final UserActionProcessor userActionProcessor;
	/** �̺�Ʈ ó�� ���� �� */
	private final Map<Class<? extends ClientdEvent>, ClientEventStrategy> strategyMap;
	/** ������ ���� ���� */
	private final MainConnectionHandler mainConnectionHandler;

	/**
	 * ������ �Ű� ������ ������� ��Ʈ���� ����� ������
	 * 
	 * @param eventQueue
	 * @param userActionProcessor
	 * @param mainConnectionHandler ����ڰ� ���� ��Ʈ���� ���� ������ �����ϰ� �޽����� �����ϴ� ������ ������ Ŭ����
	 */
	public MainController(final BlockingQueue<ClientdEvent> eventQueue, final UserActionProcessor userActionProcessor,
			final MainConnectionHandler mainConnectionHandler) {
		this.eventQueue = eventQueue;
		this.userActionProcessor = userActionProcessor;
		this.mainConnectionHandler = mainConnectionHandler;

		// �̺�Ʈ ó�� ��å �� �ۼ�
		strategyMap = new HashMap<Class<? extends ClientdEvent>, ClientEventStrategy>();
		strategyMap.put(CreateNewRoomEvent.class, new CreateNewRoomStrategy());
		strategyMap.put(JoinExistingRoomEvent.class, new JoinExistingRoomStrategy());
		strategyMap.put(SendMessageEvent.class, new NewMessageStrategy());
		strategyMap.put(QuitChattingEvent.class, new ClientLeftRoomStrategy());
	}

	/**
	 * ��Ʈ�ѷ��� �ֿ� �޼ҵ�� �̺�Ʈ�� ��ٸ� ���� �ùٸ��� ó���մϴ�.
	 */
	public void work() {
		while (true) {
			try {
				ClientdEvent clientdEvent = eventQueue.take();
				ClientEventStrategy clientEventStrategy = strategyMap.get(clientdEvent.getClass());
				clientEventStrategy.execute(clientdEvent);
			} catch (InterruptedException e) {
				// ��Ʈ�ѷ��� �̺�Ʈ�� ��Ÿ�� ������ �Ͻ� �����ؾ��ϹǷ� �ƹ� �͵����� �ʽ��ϴ�.
			}
		}
	}

	/**
	 * �̺�Ʈ�� ó���ϴ� ���� Ŭ������ �߻� �⺻ Ŭ�����Դϴ�
	 */
	abstract class ClientEventStrategy {
		/**
		 * �־��� �̺�Ʈ�� ���񽺸� ����ϴ� �߻� �޼ҵ�.
		 * 
		 * @param applicationEvent �����Ǿ���ϴ� ���� ���α׷� �̺�Ʈ
		 */
		abstract void execute(final ClientdEvent applicationEvent);
	}

	/**
	 * �� ���� ����� ����ڸ� ��ϴ� ������ �����ϴ� ���� Ŭ�����Դϴ�.
	 */
	class CreateNewRoomStrategy extends ClientEventStrategy {
		void execute(final ClientdEvent clientdEvent) {
			CreateNewRoomEvent createNewRoomEvent = (CreateNewRoomEvent) clientdEvent;
			if (userActionProcessor.createNewRoom(createNewRoomEvent)) {
				mainConnectionHandler.sendMainChatViewInfo(createNewRoomEvent.getUserName(), createNewRoomEvent.getRoomName());
			} else {
				mainConnectionHandler.sendMessage(new AlertToClientEvent("\r\n" + "�־��� �̸��� ���� �̹� �ֽ��ϴ�.", createNewRoomEvent.getUserName()));
			}
		}
	}

	/**
	 * ���� �濡 �շ��ϱ����� ����� ���� ������ �����ϴ� ���� Ŭ�����Դϴ�.
	 */
	class JoinExistingRoomStrategy extends ClientEventStrategy {
		void execute(final ClientdEvent clientdEvent) {
			JoinExistingRoomEvent joinExistingRoomEvent = (JoinExistingRoomEvent) clientdEvent;
			if (userActionProcessor.addUserToSpecificRoom(joinExistingRoomEvent)) {
				mainConnectionHandler.sendMainChatViewInfo(joinExistingRoomEvent.getUserName(), joinExistingRoomEvent.getRoomName());
			} else {
				mainConnectionHandler.sendMessage(new AlertToClientEvent("�����Ϸ��� ���� �������� �ʽ��ϴ�.", joinExistingRoomEvent.getUserName()));
			}
		}
	}

	/**
	 * ����ڰ� �� �޽����� ������ ���� ������ �����ϴ� ���� Ŭ����
	 */
	class NewMessageStrategy extends ClientEventStrategy {
		void execute(final ClientdEvent clientdEvent) {
			SendMessageEvent newMessageInformation = (SendMessageEvent) clientdEvent;
			userActionProcessor.addMessageOfUser(newMessageInformation);
			mainConnectionHandler.sendMessageToAll(userActionProcessor.getRoomData(newMessageInformation));
		}
	}

	/**
	 * ���� ������� ��Ż ������ �����ϴ� ���� Ŭ�����Դϴ�.
	 */
	class ClientLeftRoomStrategy extends ClientEventStrategy {
		void execute(final ClientdEvent clientdEvent) {
			QuitChattingEvent clientLeftRoomInformation = (QuitChattingEvent) clientdEvent;
			userActionProcessor.setUserToInactive(clientLeftRoomInformation);
		}
	}
}
