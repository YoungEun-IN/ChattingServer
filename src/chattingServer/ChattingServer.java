package chattingServer;

import chattingServer.controller.Controller;

/**
 * 모든 구성 요소의 적절한 초기화를 담당
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
