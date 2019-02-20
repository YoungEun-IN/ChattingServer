package chattingServer;

import chattingServer.controller.Controller;

/**
 * Controller 객체를 싱글톤으로 생성한다.
 */
public class ChattingServer {
	/**
	 * 모델, 이벤트 큐 및 컨트롤러를 생성
	 * @param args 
	 */
	public static void main(String args[]) {
		
		Controller controller = Controller.createInst();
		controller.work();
	}
}
