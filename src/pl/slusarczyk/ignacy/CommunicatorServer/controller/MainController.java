package pl.slusarczyk.ignacy.CommunicatorServer.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ClientLeftRoomEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.JoinExistingRoomEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.SendMessageEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.CreateNewRoomEvent;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.InfoServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.connection.MainConnectionHandler;
import pl.slusarczyk.ignacy.CommunicatorServer.model.UserActionProcessor;

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
	public MainController(final BlockingQueue<ServerHandledEvent> eventQueue, final UserActionProcessor userActionProcessor, final MainConnectionHandler mainConnectionHandler) {
		this.eventQueue = eventQueue;
		this.userActionProcessor = userActionProcessor;
		this.mainConnectionHandler = mainConnectionHandler;

		// �̺�Ʈ ó�� ��å �� �ۼ�
		strategyMap = new HashMap<Class<? extends ServerHandledEvent>, ClientEventStrategy>();
		strategyMap.put(CreateNewRoomEvent.class, new CreateNewRoomStrategy());
		strategyMap.put(JoinExistingRoomEvent.class, new JoinExistingRoomStrategy());
		strategyMap.put(SendMessageEvent.class, new NewMessageStrategy());
		strategyMap.put(ClientLeftRoomEvent.class, new ClientLeftRoomStrategy());
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
				mainConnectionHandler.assertConnectionEstablished(createNewRoomEvent.getUserIdData(), true, createNewRoomEvent.getRoomName());
			} else {
				mainConnectionHandler.sendMessage(new InfoServerEvent("\r\n" + "�־��� �̸��� ���� �̹� �ֽ��ϴ�.", createNewRoomEvent.getUserIdData()));
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
				mainConnectionHandler.assertConnectionEstablished(joinExistingRoomEvent.getUserIdData(), true, joinExistingRoomEvent.getRoomName());
			} else {
				mainConnectionHandler.sendMessage(new InfoServerEvent("�����Ϸ��� ���� �������� �ʽ��ϴ�.", joinExistingRoomEvent.getUserIdData()));
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
			ClientLeftRoomEvent clientLeftRoomInformation = (ClientLeftRoomEvent) serverHandledEvent;
			userActionProcessor.setUserToInactive(clientLeftRoomInformation);
		}
	}
}
