package pl.slusarczyk.ignacy.CommunicatorServer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.connection.MainConnectionHandler;
import pl.slusarczyk.ignacy.CommunicatorServer.controller.Controller;
import pl.slusarczyk.ignacy.CommunicatorServer.model.Model;

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
		Model model = new Model();
		MainConnectionHandler mainConnectionHandler = new MainConnectionHandler(5000, eventQueue);
		Controller controller = new Controller(eventQueue, model, mainConnectionHandler);
		controller.work();
	}
}
