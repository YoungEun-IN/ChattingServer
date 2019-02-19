package chattingServer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import chattingClient.clientSideEvent.ClientSideEvent;
import chattingServer.connection.MainConnectionHandler;
import chattingServer.controller.MainController;
import chattingServer.model.UserActionProcessor;

/**
 * 모든 구성 요소의 적절한 초기화를 담당
 */
public class ChattingServer {
	/**
	 * 모델, 이벤트 큐 및 컨트롤러를 생성
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
