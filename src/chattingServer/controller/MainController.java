package chattingServer.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import chattingClient.serverHandleEvent.CreateNewRoomEvent;
import chattingClient.serverHandleEvent.JoinExistingRoomEvent;
import chattingClient.serverHandleEvent.QuitChattingEvent;
import chattingClient.serverHandleEvent.SendMessageEvent;
import chattingClient.serverHandleEvent.ServerHandledEvent;
import chattingServer.clientHandleEvent.AlertToClientEvent;
import chattingServer.connection.MainConnectionHandler;
import chattingServer.model.UserActionProcessor;

/**
 * Ŭ���̾�Ʈ�� ���� ���� ������ ����� ����ϴ� ��Ʈ�ѷ� Ŭ����.
 */
public class MainController {
	/** �̺�Ʈ ť */
	private final BlockingQueue<ServerHandledEvent> eventQueue;
	/** �� */
	private final UserActionProcessor userActionProcessor;
	/** �̺�Ʈ ó�� ���� �� */
	private final Map<Class<? extends ServerHandledEvent>, ClientEventStrategy> strategyMap;
	/** ������ ���� ���� */
	private final MainConnectionHandler mainConnectionHandler;

	/**
	 * ������ �Ű� ������ ������� ��Ʈ���� ����� ������
	 * 
	 * @param eventQueue
	 * @param userActionProcessor
	 * @param mainConnectionHandler ����ڰ� ���� ��Ʈ���� ���� ������ �����ϰ� �޽����� �����ϴ� ������ ������ Ŭ����
	 */
	public MainController(final BlockingQueue<ServerHandledEvent> eventQueue, final UserActionProcessor userActionProcessor,
			final MainConnectionHandler mainConnectionHandler) {
		this.eventQueue = eventQueue;
		this.userActionProcessor = userActionProcessor;
		this.mainConnectionHandler = mainConnectionHandler;

		// �̺�Ʈ ó�� ��å �� �ۼ�
		strategyMap = new HashMap<Class<? extends ServerHandledEvent>, ClientEventStrategy>();
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
				ServerHandledEvent serverHandledEvent = eventQueue.take();
				ClientEventStrategy clientEventStrategy = strategyMap.get(serverHandledEvent.getClass());
				clientEventStrategy.execute(serverHandledEvent);
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
		abstract void execute(final ServerHandledEvent applicationEvent);
	}

	/**
	 * �� ���� ����� ����ڸ� ��ϴ� ������ �����ϴ� ���� Ŭ�����Դϴ�.
	 */
	class CreateNewRoomStrategy extends ClientEventStrategy {
		void execute(final ServerHandledEvent serverHandledEvent) {
			CreateNewRoomEvent createNewRoomEvent = (CreateNewRoomEvent) serverHandledEvent;
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
		void execute(final ServerHandledEvent serverHandledEvent) {
			JoinExistingRoomEvent joinExistingRoomEvent = (JoinExistingRoomEvent) serverHandledEvent;
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
		void execute(final ServerHandledEvent serverHandledEvent) {
			SendMessageEvent newMessageInformation = (SendMessageEvent) serverHandledEvent;
			userActionProcessor.addMessageOfUser(newMessageInformation);
			mainConnectionHandler.sendMessageToAll(userActionProcessor.getRoomData(newMessageInformation));
		}
	}

	/**
	 * ���� ������� ��Ż ������ �����ϴ� ���� Ŭ�����Դϴ�.
	 */
	class ClientLeftRoomStrategy extends ClientEventStrategy {
		void execute(final ServerHandledEvent serverHandledEvent) {
			QuitChattingEvent clientLeftRoomInformation = (QuitChattingEvent) serverHandledEvent;
			userActionProcessor.setUserToInactive(clientLeftRoomInformation);
		}
	}
}
