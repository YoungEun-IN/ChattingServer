package pl.slusarczyk.ignacy.CommunicatorServer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.connection.MainConnectionHandler;
import pl.slusarczyk.ignacy.CommunicatorServer.controller.Controller;
import pl.slusarczyk.ignacy.CommunicatorServer.model.Model;

/**
 * �⺻ ���� ���α׷� Ŭ������ ��� ���� ����� ������ �ʱ�ȭ�� ����մϴ�.
 */
public class CommunicatorServer {
	/**
	 * ���ο� ���ø����̼� �޼ҵ�� ��, �̺�Ʈ ť �� ��Ʈ�ѷ��� �����մϴ�.
	 */
	public static void main(String args[]) {
		BlockingQueue<ServerHandledEvent> eventQueue = new LinkedBlockingQueue<ServerHandledEvent>();
		Model model = new Model();
		MainConnectionHandler mainConnectionHandler = new MainConnectionHandler(5000, eventQueue);
		Controller controller = new Controller(eventQueue, model, mainConnectionHandler);
		controller.work();
	}
}
