package chattingServer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import chattingClient.clientSideEvent.ClientSideEvent;
import chattingServer.connection.ConnectionHandler;
import chattingServer.controller.Controller;
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
		ConnectionHandler connectionHandler = new ConnectionHandler(5000, eventQueue);
		UserActionProcessor userActionProcessor = new UserActionProcessor();
		Controller controller = new Controller(eventQueue, userActionProcessor, connectionHandler);
		controller.work();
	}
}
