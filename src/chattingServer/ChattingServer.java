package chattingServer;

import chattingServer.controller.Controller;

/**
 * Controller ��ü�� �̱������� �����Ѵ�.
 */
public class ChattingServer {
	/**
	 * ��, �̺�Ʈ ť �� ��Ʈ�ѷ��� ����
	 * @param args 
	 */
	public static void main(String args[]) {
		
		Controller controller = Controller.createInst();
		controller.work();
	}
}
