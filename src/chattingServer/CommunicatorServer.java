package chattingServer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import chattingClient.serverHandleEvent.ServerHandledEvent;
import chattingServer.connection.MainConnectionHandler;
import chattingServer.controller.MainController;
import chattingServer.model.UserActionProcessor;

/**
 * ��� ���� ����� ������ �ʱ�ȭ�� ���
 */
public class CommunicatorServer {
	/**
	 * ��, �̺�Ʈ ť �� ��Ʈ�ѷ��� ����
	 * @param args 
	 */
	public static void main(String args[]) {
		BlockingQueue<ServerHandledEvent> eventQueue = new LinkedBlockingQueue<ServerHandledEvent>();
		MainConnectionHandler connectionHandler = new MainConnectionHandler(5000, eventQueue);
		UserActionProcessor userActionProcessor = new UserActionProcessor();
		MainController controller = new MainController(eventQueue, userActionProcessor, connectionHandler);
		controller.work();
	}
}
