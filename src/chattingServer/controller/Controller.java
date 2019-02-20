package chattingServer.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import chattingClient.clientSideEvent.ClientSideEvent;
import chattingClient.clientSideEvent.CreateNewRoomEvent;
import chattingClient.clientSideEvent.JoinExistingRoomEvent;
import chattingClient.clientSideEvent.QuitChattingEvent;
import chattingClient.clientSideEvent.SendMessageEvent;
import chattingServer.connection.ConnectionHandler;
import chattingServer.serverSideEvent.AlertToClientEvent;

/**
 * Ŭ���̾�Ʈ�� ������ �ؼ��ϴ� �������� �����ϰ� �б��Ѵ�.
 */
public class Controller {
	/** eventQueue */
	private final BlockingQueue<ClientSideEvent> eventQueue = new LinkedBlockingQueue<ClientSideEvent>();
	/** connectionHandler */
	private final ConnectionHandler connectionHandler = new ConnectionHandler(5000, eventQueue);
	/** userActionProcessor */
	private final StrategyProcessor strategyProcessor = new StrategyProcessor();
	/** �̺�Ʈ ó�� ���� �� */
	private final Map<Class<? extends ClientSideEvent>, ClientSideStrategy> strategyMap;

	static Controller inst = null;
	
	public static Controller createInst() {
		if (inst == null)
			inst = new Controller();

		return inst;
	}

	/**
	 * ������ �Ű� ������ ������� ��Ʈ���� ����� ������
	 */
	private Controller() {
		// �̺�Ʈ ó�� ��å �� �ۼ�
		strategyMap = new HashMap<Class<? extends ClientSideEvent>, ClientSideStrategy>();
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
				ClientSideStrategy clientSideStrategy = strategyMap.get(clientdEvent.getClass());
				clientSideStrategy.execute(clientdEvent);
			} catch (InterruptedException e) {
				// ��Ʈ�ѷ��� �̺�Ʈ�� ��Ÿ�� ������ �Ͻ� �����ؾ��ϹǷ� �ƹ� �͵����� �ʽ��ϴ�.
			}
		}
	}

	/**
	 * �̺�Ʈ�� ó���ϴ� ���� Ŭ������ �߻� �⺻ Ŭ�����Դϴ�
	 */
	abstract class ClientSideStrategy {
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
	class CreateNewRoomStrategy extends ClientSideStrategy {
		void execute(final ClientSideEvent clientSideEvent) {
			CreateNewRoomEvent createNewRoomEvent = (CreateNewRoomEvent) clientSideEvent;
			if (strategyProcessor.createNewRoom(createNewRoomEvent)) {
				connectionHandler.buildChatRoomView(createNewRoomEvent.getUserName(), createNewRoomEvent.getRoomName());
			} else {
				connectionHandler.alert(new AlertToClientEvent("\r\n" + "�־��� �̸��� ���� �̹� �ֽ��ϴ�.", createNewRoomEvent.getUserName()));
			}
		}
	}

	/**
	 * ���� �濡 �շ��ϱ����� ����� ���� ������ �����ϴ� ���� Ŭ����
	 */
	class JoinExistingRoomStrategy extends ClientSideStrategy {
		void execute(final ClientSideEvent clientSideEvent) {
			JoinExistingRoomEvent joinExistingRoomEvent = (JoinExistingRoomEvent) clientSideEvent;
			if (strategyProcessor.addUserToSpecificRoom(joinExistingRoomEvent)) {
				connectionHandler.buildChatRoomView(joinExistingRoomEvent.getUserName(), joinExistingRoomEvent.getRoomName());
			} else {
				connectionHandler.alert(new AlertToClientEvent("�����Ϸ��� ���� �������� �ʽ��ϴ�.", joinExistingRoomEvent.getUserName()));
			}
		}
	}

	/**
	 * ����ڰ� �� �޽����� ������ ���� ������ �����ϴ� ���� Ŭ����
	 */
	class SendMessageStrategy extends ClientSideStrategy {
		void execute(final ClientSideEvent clientSideEvent) {
			SendMessageEvent sendMessageEvent = (SendMessageEvent) clientSideEvent;
			strategyProcessor.addMessageOfUser(sendMessageEvent);
			connectionHandler.sendMessageToAll(strategyProcessor.getRoomData(sendMessageEvent));
		}
	}

	/**
	 * ���� ������� ��Ż ������ �����ϴ� ���� Ŭ�����Դϴ�.
	 */
	class QuitChattingStrategy extends ClientSideStrategy {
		void execute(final ClientSideEvent clientSideEvent) {
			QuitChattingEvent quitChattingEvent = (QuitChattingEvent) clientSideEvent;
			strategyProcessor.setUserToInactive(quitChattingEvent);
		}
	}
}
