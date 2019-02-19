package chattingServer.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import chattingClient.clientSideEvent.CreateNewRoomEvent;
import chattingClient.clientSideEvent.JoinExistingRoomEvent;
import chattingClient.clientSideEvent.QuitChattingEvent;
import chattingClient.clientSideEvent.SendMessageEvent;
import chattingClient.clientSideEvent.ClientSideEvent;
import chattingServer.serverSideEvent.AlertToClientEvent;
import chattingServer.connection.ConnectionHandler;
import chattingServer.model.UserActionProcessor;

/**
 * 클라이언트와 서버 간의 적절한 통신을 담당하는 컨트롤러 클래스.
 */
public class Controller {
	/** 이벤트 큐 */
	private final BlockingQueue<ClientSideEvent> eventQueue;
	/** 모델 */
	private final UserActionProcessor userActionProcessor;
	/** 이벤트 처리 전략 맵 */
	private final Map<Class<? extends ClientSideEvent>, ClientSideEventStrategy> strategyMap;
	/** 서버에 대한 참조 */
	private final ConnectionHandler connectionHandler;

	/**
	 * 지정된 매개 변수를 기반으로 컨트롤을 만드는 생성자
	 * 
	 * @param eventQueue
	 * @param userActionProcessor
	 * @param connectionHandler   사용자가 종료 스트림에 대한 정보를 저장하고 메시지를 배포하는 서버의 마스터 클래스
	 */
	public Controller(final BlockingQueue<ClientSideEvent> eventQueue, final UserActionProcessor userActionProcessor,
			final ConnectionHandler connectionHandler) {
		
		this.eventQueue = eventQueue;
		this.userActionProcessor = userActionProcessor;
		this.connectionHandler = connectionHandler;

		// 이벤트 처리 정책 맵 작성
		strategyMap = new HashMap<Class<? extends ClientSideEvent>, ClientSideEventStrategy>();
		strategyMap.put(CreateNewRoomEvent.class, new CreateNewRoomStrategy());
		strategyMap.put(JoinExistingRoomEvent.class, new JoinExistingRoomStrategy());
		strategyMap.put(SendMessageEvent.class, new SendMessageStrategy());
		strategyMap.put(QuitChattingEvent.class, new QuitChattingStrategy());
	}

	/**
	 * 컨트롤러의 주요 메소드는 이벤트를 기다린 다음 올바르게 처리합니다.
	 */
	public void work() {
		while (true) {
			try {
				ClientSideEvent clientdEvent = eventQueue.take();
				ClientSideEventStrategy clientSideEventStrategy = strategyMap.get(clientdEvent.getClass());
				clientSideEventStrategy.execute(clientdEvent);
			} catch (InterruptedException e) {
				// 컨트롤러가 이벤트가 나타날 때까지 일시 중지해야하므로 아무 것도하지 않습니다.
			}
		}
	}

	/**
	 * 이벤트를 처리하는 전략 클래스의 추상 기본 클래스입니다
	 */
	abstract class ClientSideEventStrategy {
		/**
		 * 주어진 이벤트의 서비스를 기술하는 추상 메소드.
		 * 
		 * @param applicationEvent 지원되어야하는 응용 프로그램 이벤트
		 */
		abstract void execute(final ClientSideEvent clientSideEvent);
	}

	/**
	 * 새 방을 만들어 사용자를 운영하는 전략을 설명하는 내부 클래스
	 */
	class CreateNewRoomStrategy extends ClientSideEventStrategy {
		void execute(final ClientSideEvent clientSideEvent) {
			CreateNewRoomEvent createNewRoomEvent = (CreateNewRoomEvent) clientSideEvent;
			if (userActionProcessor.createNewRoom(createNewRoomEvent)) {
				connectionHandler.buildChatRoomView(createNewRoomEvent.getUserName(), createNewRoomEvent.getRoomName());
			} else {
				connectionHandler.alert(new AlertToClientEvent("\r\n" + "주어진 이름의 방이 이미 있습니다.", createNewRoomEvent.getUserName()));
			}
		}
	}

	/**
	 * 기존 방에 합류하기위한 사용자 지원 전략을 설명하는 내부 클래스
	 */
	class JoinExistingRoomStrategy extends ClientSideEventStrategy {
		void execute(final ClientSideEvent clientSideEvent) {
			JoinExistingRoomEvent joinExistingRoomEvent = (JoinExistingRoomEvent) clientSideEvent;
			if (userActionProcessor.addUserToSpecificRoom(joinExistingRoomEvent)) {
				connectionHandler.buildChatRoomView(joinExistingRoomEvent.getUserName(), joinExistingRoomEvent.getRoomName());
			} else {
				connectionHandler.alert(new AlertToClientEvent("가입하려는 방은 존재하지 않습니다.", joinExistingRoomEvent.getUserName()));
			}
		}
	}

	/**
	 * 사용자가 새 메시지를 보내는 서비스 전략을 설명하는 내부 클래스
	 */
	class SendMessageStrategy extends ClientSideEventStrategy {
		void execute(final ClientSideEvent clientSideEvent) {
			SendMessageEvent sendMessageEvent = (SendMessageEvent) clientSideEvent;
			userActionProcessor.addMessageOfUser(sendMessageEvent);
			connectionHandler.sendMessageToAll(userActionProcessor.getRoomData(sendMessageEvent));
		}
	}

	/**
	 * 방의 사용자의 이탈 전략을 설명하는 내부 클래스입니다.
	 */
	class QuitChattingStrategy extends ClientSideEventStrategy {
		void execute(final ClientSideEvent clientSideEvent) {
			QuitChattingEvent quitChattingEvent = (QuitChattingEvent) clientSideEvent;
			userActionProcessor.setUserToInactive(quitChattingEvent);
		}
	}
}
