package pl.slusarczyk.ignacy.CommunicatorServer.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ClientLeftRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.JoinExistingRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.NewMessage;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.CreateNewRoom;
import pl.slusarczyk.ignacy.CommunicatorClient.serverHandledEvent.ServerHandledEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.clientHandledEvent.InfoServerEvent;
import pl.slusarczyk.ignacy.CommunicatorServer.connection.MainConnectionHandler;
import pl.slusarczyk.ignacy.CommunicatorServer.model.Model;

/**
 * 클라이언트와 서버 간의 적절한 통신을 담당하는 컨트롤러 클래스. 클라이언트의 고객 서비스 전략에 해당하는 클래스 포함
 */
public class Controller {
	/** 이벤트 큐 */
	private final BlockingQueue<ServerHandledEvent> eventQueue;
	/** 모델 */
	private final Model model;
	/** 이벤트 처리 전략 맵 */
	private final Map<Class<? extends ServerHandledEvent>, ClientEventStrategy> strategyMap;
	/** 서버에 대한 참조 */
	private final MainConnectionHandler mainConnectionHandler;

	/**
	 * 지정된 매개 변수를 기반으로 컨트롤을 만드는 생성자
	 * 
	 * @param eventQueue
	 * @param model
	 * @param mainConnectionHandler 사용자가 종료 스트림에 대한 정보를 저장하고 메시지를 배포하는 서버의 마스터 클래스
	 */
	public Controller(final BlockingQueue<ServerHandledEvent> eventQueue, final Model model, final MainConnectionHandler mainConnectionHandler) {
		this.eventQueue = eventQueue;
		this.model = model;
		this.mainConnectionHandler = mainConnectionHandler;

		// 이벤트 처리 정책 맵 작성
		strategyMap = new HashMap<Class<? extends ServerHandledEvent>, ClientEventStrategy>();
		strategyMap.put(CreateNewRoom.class, new CreateNewRoomStrategy());
		strategyMap.put(JoinExistingRoom.class, new JoinExistingRoomStrategy());
		strategyMap.put(NewMessage.class, new NewMessageStrategy());
		strategyMap.put(ClientLeftRoom.class, new ClientLeftRoomStrategy());
	}

	/**
	 * 컨트롤러의 주요 메소드는 이벤트를 기다린 다음 올바르게 처리합니다.
	 */
	public void work() {
		while (true) {
			try {
				ServerHandledEvent serverHandledEvent = eventQueue.take();
				ClientEventStrategy clientEventStrategy = strategyMap.get(serverHandledEvent.getClass());
				clientEventStrategy.execute(serverHandledEvent);
			} catch (InterruptedException e) {
				// 컨트롤러가 이벤트가 나타날 때까지 일시 중지해야하므로 아무 것도하지 않습니다.
			}
		}
	}

	/**
	 * 이벤트를 처리하는 전략 클래스의 추상 기본 클래스입니다
	 */
	abstract class ClientEventStrategy {
		/**
		 * 주어진 이벤트의 서비스를 기술하는 추상 메소드.
		 * 
		 * @param applicationEvent 지원되어야하는 응용 프로그램 이벤트
		 */
		abstract void execute(final ServerHandledEvent applicationEvent);
	}

	/**
	 * 새 방을 만들어 사용자를 운영하는 전략을 설명하는 내부 클래스입니다.
	 */
	class CreateNewRoomStrategy extends ClientEventStrategy {
		void execute(final ServerHandledEvent applicationEventObject) {
			CreateNewRoom newRoom = (CreateNewRoom) applicationEventObject;
			if (model.createNewRoom(newRoom)) {
				mainConnectionHandler.assertConnectionEstablished(newRoom.getUserIdData(), true, newRoom.getRoomName());
			} else {
				mainConnectionHandler.sendMessage(new InfoServerEvent("\r\n" + "주어진 이름의 방이 이미 있습니다.", newRoom.getUserIdData()));
			}
		}
	}

	/**
	 * 기존 방에 합류하기위한 사용자 지원 전략을 설명하는 내부 클래스입니다.
	 */
	class JoinExistingRoomStrategy extends ClientEventStrategy {
		void execute(final ServerHandledEvent applicationEventObject) {
			JoinExistingRoom joinExistingRoomInformation = (JoinExistingRoom) applicationEventObject;
			if (model.addUserToSpecificRoom(joinExistingRoomInformation) == true) {
				mainConnectionHandler.assertConnectionEstablished(joinExistingRoomInformation.getUserIdData(), true, joinExistingRoomInformation.getRoomName());
			} else {
				mainConnectionHandler.sendMessage(new InfoServerEvent("가입하려는 방은 존재하지 않습니다.", joinExistingRoomInformation.getUserIdData()));
			}
		}
	}

	/**
	 * 사용자가 새 메시지를 보내는 서비스 전략을 설명하는 내부 클래스
	 */
	class NewMessageStrategy extends ClientEventStrategy {
		void execute(final ServerHandledEvent applicationEventObject) {
			NewMessage newMessageInformation = (NewMessage) applicationEventObject;
			model.addMessageOfUser(newMessageInformation);
			mainConnectionHandler.sendMessageToAll(model.getRoomDataFromRoom(newMessageInformation));
		}
	}

	/**
	 * 방의 사용자의 이탈 전략을 설명하는 내부 클래스입니다.
	 */
	class ClientLeftRoomStrategy extends ClientEventStrategy {
		void execute(final ServerHandledEvent applicationEventObject) {
			ClientLeftRoom clientLeftRoomInformation = (ClientLeftRoom) applicationEventObject;
			model.setUserToInactive(clientLeftRoomInformation);
		}
	}
}
