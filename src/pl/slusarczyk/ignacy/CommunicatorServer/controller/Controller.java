package pl.slusarczyk.ignacy.CommunicatorServer.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ClientLeftRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.JoinExistingRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.NewMessage;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.CreateNewRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.InfoServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.connection.MainConnectionHandler;
import pl.slusarczyk.ignacy.CommunicatorServer.model.Model;

/**
 * Ŭ���̾�Ʈ�� ���� ���� ������ ����� ����ϴ� ��Ʈ�ѷ� Ŭ����. Ŭ���̾�Ʈ�� �� ���� ������ �ش��ϴ� Ŭ���� ����
 */
public class Controller {
	/** �̺�Ʈ ť */
	private final BlockingQueue<ServerHandledEvent> eventQueue;
	/** �� */
	private final Model model;
	/** �̺�Ʈ ó�� ���� �� */
	private final Map<Class<? extends ServerHandledEvent>, ClientEventStrategy> strategyMap;
	/** ������ ���� ���� */
	private final MainConnectionHandler mainConnectionHandler;

	/**
	 * ������ �Ű� ������ ������� ��Ʈ���� ����� ������
	 * 
	 * @param eventQueue
	 * @param model
	 * @param mainConnectionHandler ����ڰ� ���� ��Ʈ���� ���� ������ �����ϰ� �޽����� �����ϴ� ������ ������ Ŭ����
	 */
	public Controller(final BlockingQueue<ServerHandledEvent> eventQueue, final Model model, final MainConnectionHandler mainConnectionHandler) {
		this.eventQueue = eventQueue;
		this.model = model;
		this.mainConnectionHandler = mainConnectionHandler;

		// �̺�Ʈ ó�� ��å �� �ۼ�
		strategyMap = new HashMap<Class<? extends ServerHandledEvent>, ClientEventStrategy>();
		strategyMap.put(CreateNewRoom.class, new CreateNewRoomStrategy());
		strategyMap.put(JoinExistingRoom.class, new JoinExistingRoomStrategy());
		strategyMap.put(NewMessage.class, new NewMessageStrategy());
		strategyMap.put(ClientLeftRoom.class, new ClientLeftRoomStrategy());
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
		void execute(final ServerHandledEvent applicationEventObject) {
			CreateNewRoom newRoom = (CreateNewRoom) applicationEventObject;
			if (model.createNewRoom(newRoom)) {
				mainConnectionHandler.assertConnectionEstablished(newRoom.getUserIdData(), true, newRoom.getRoomName());
			} else {
				mainConnectionHandler.sendMessage(new InfoServerEvent("\r\n" + "�־��� �̸��� ���� �̹� �ֽ��ϴ�.", newRoom.getUserIdData()));
			}
		}
	}

	/**
	 * ���� �濡 �շ��ϱ����� ����� ���� ������ �����ϴ� ���� Ŭ�����Դϴ�.
	 */
	class JoinExistingRoomStrategy extends ClientEventStrategy {
		void execute(final ServerHandledEvent applicationEventObject) {
			JoinExistingRoom joinExistingRoomInformation = (JoinExistingRoom) applicationEventObject;
			if (model.addUserToSpecificRoom(joinExistingRoomInformation) == true) {
				mainConnectionHandler.assertConnectionEstablished(joinExistingRoomInformation.getUserIdData(), true, joinExistingRoomInformation.getRoomName());
			} else {
				mainConnectionHandler.sendMessage(new InfoServerEvent("�����Ϸ��� ���� �������� �ʽ��ϴ�.", joinExistingRoomInformation.getUserIdData()));
			}
		}
	}

	/**
	 * ����ڰ� �� �޽����� ������ ���� ������ �����ϴ� ���� Ŭ����
	 */
	class NewMessageStrategy extends ClientEventStrategy {
		void execute(final ServerHandledEvent applicationEventObject) {
			NewMessage newMessageInformation = (NewMessage) applicationEventObject;
			model.addMessageOfUser(newMessageInformation);
			mainConnectionHandler.sendMessageToAll(model.getRoomDataFromRoom(newMessageInformation));
		}
	}

	/**
	 * ���� ������� ��Ż ������ �����ϴ� ���� Ŭ�����Դϴ�.
	 */
	class ClientLeftRoomStrategy extends ClientEventStrategy {
		void execute(final ServerHandledEvent applicationEventObject) {
			ClientLeftRoom clientLeftRoomInformation = (ClientLeftRoom) applicationEventObject;
			model.setUserToInactive(clientLeftRoomInformation);
		}
	}
}
