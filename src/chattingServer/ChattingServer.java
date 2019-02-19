package chattingServer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import chattingClient.clientSideEvent.ClientSideEvent;
import chattingServer.connection.MainConnectionHandler;
import chattingServer.controller.MainController;
import chattingServer.model.UserActionProcessor;

/**
 * ��� ���� ����� ������ �ʱ�ȭ�� ���
 */
public class ChattingServer {
	/**
	 * ��, �̺�Ʈ ť �� ��Ʈ�ѷ��� ����
	 * @param args 
	 */
	public static void main(String args[]) {
		BlockingQueue<ClientSideEvent> eventQueue = new LinkedBlockingQueue<ClientSideEvent>();
		MainConnectionHandler connectionHandler = new MainConnectionHandler(5000, eventQueue);
		UserActionProcessor userActionProcessor = new UserActionProcessor();
		MainController controller = new MainController(eventQueue, userActionProcessor, connectionHandler);
		controller.work();
	}
}
