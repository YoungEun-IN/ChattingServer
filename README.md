# Chatting server

<a href="#"><img src="https://img.shields.io/github/last-commit/1ilsang/java-mvc-chatting.svg?style=flat" /></a>
<a href="#"><img src="https://img.shields.io/github/languages/top/1ilsang/java-mvc-chatting.svg?colorB=yellow&style=flat" /></a>
<a href="#"><img src="https://img.shields.io/badge/license-MIT-green.svg" /></a>

## � ������Ʈ�ΰ���?
- ��Ƽ ä�� ���� ������Ʈ.
- �ٽ� ��ǥ: ������, MVC, ������, ������ ¤���.
- ���� �Ⱓ: �� 10��
- Keyword: MVC, Thread, Socket, Polymorphism, Serializable, Swing, Singleton, Strategy Pattern


##���� �� ���� ����
1. MVC ������ ����Ͽ� Ŭ���� �� ����� ��Ȯ�ϵ��� �����Ѵ�.
2. ��Ʈ�ѷ� Ŭ������ �̱����� �ǵ��� �Ͽ� ���ʿ��� ��ü ������ ���´�.
3. ������ Ŭ���̾�Ʈ �� ���޵Ǵ� ��ü�� ���� ������ ����Ͽ� ĸ��ȭ�ϰ�, BlockingQueue�� Ȱ���Ͽ� �̺�Ʈ�� ������� ����ǵ��� �Ѵ�.
4. final Ű����� private Ű���带 ������ ����Ͽ� ������ ���ɼ��� �ּ�ȭ�Ѵ�.
5. ����� ��� ����ȭ�� ��ü�� ����Ѵ�.

##���� ȭ��
#####��ȭ�� ���� ȭ��

<img src="img/welcome.png"  />

#####ä��ȭ��

<img src="img/chatting.png" />

#####���� ���� �ַܼα�

<img src="img/server.png" />

#####Ŭ���̾�Ʈ ���� �ַܼα�

<img src="img/client.png" />

<br/>

##�Ѵ��� ���� ��ü ����

<img src="img/welcome.png"  />

<hr/>

<br/>

##### Server:
  - ������ ����� ���ÿ� `ServerSocketThread` �� �Ļ���Ű��, `ServerSocketThread`�� �ٽ� ä�ù溰�� `ConnectionThread`�� �Ļ���Ų��.
  - Ŭ���̾�Ʈ ��û�� ���� ������ �����ֱ� ���� `Blocking Queue`�� ��� ���������� �����Ѵ�.
  - `ConnectionThread` ��ü�� Ŭ���̾�Ʈ�� ������ �м��� ä�ù溰�� ������ �����ϰ� �޽����� �ѷ��ش�.
<br/>


##### Client: 
  - `Connection`���� ������ ����ϴ� ������ ������. ����� ��� `����ȭ`�� ��ü�� ����Ѵ�.
  - ������ `ListenThread` ������ ��ü�� �Ļ��� �����Ѵ�.
  - ������ ȭ�鰴ü�� ã�Ҵٸ�  `View` ��ü�� ���� �������� ȭ���� �����ش�.

<br/>


##Ŭ���� ������ �˾ƺ���
#### Server
- ChattingServer : main �޼ҵ� �ȿ��� Controller ��ü�� �̱������� �����Ѵ�.

#####Connection
- ConnectionHandler : ä���� ó���� ��������� �����Ѵ�.
- ConnectionThread : Ŭ���̾�Ʈ�� ������ �м��� ä�ù溰�� ������ �����ϰ� �޽����� �ѷ��ش�. ������ �濡 �����Ҷ����� �� �����尡 �ϳ��� �����.
- ServerSocketThread : Ŭ���̾�Ʈ�� ��û������ �޾� �� ���ϸ��� ConnectionThread�� �Ļ���Ų��.
 
#####controller
- Controller : Ŭ���̾�Ʈ�� ������ �ؼ��ϴ� �������� �����ϰ� �б��Ѵ�.
- StrategyProcessor : Ŭ���̾�Ʈ�� ������ �ؼ��Ͽ� �����Ѵ�.

#####serverSideEvent
- ServerSideEvent : ���� �̺�Ʈ�� �߻�Ŭ����
- AlertToClientEvent : ����ڿ��� �˸��� �� �� �߻��ϴ� �̺�Ʈ
- ChatRoomViewBuildEvent : ä��â�� ������ �� �߻��ϴ� �̺�Ʈ
- ConversationBuildEvent : ��ȭ�� ������ �� �߻��ϴ� �̺�Ʈ
- 

#####model.data
- MessageData : �޽��� ������ ��� �ִ�.
- RoomData : �� ������ ��� �ִ�.
- UserData : ����� ������ ��� �ִ�.

#### Cient
- chattingClient : Connection ��ü�� �̱������� �����Ѵ�.

#####Connection
- Connection : Socekt �� ���� ������ ȣ��Ʈ���� ��θ� �����. ��ü�� ��ȯ�ϹǷ� ObjectIn(Out)putStream �� �����ش�.

#####view
- ViewController : ������ ������ �ؼ��ϴ� �������� �����ϰ� �б��Ѵ�.
- ChatRoomView : ��ȭâ ȭ���� �׷��ش�.
- CreateOrJoinRoomView : ���α׷� ����� ���� ���� ���̴� ȭ���� �׷��ش�.
  
#####clientSideEvent 
- clientSideEvent : Ŭ���̾�Ʈ �̺�Ʈ�� �߻�Ŭ����
- CreateNewRoomEvent : ���� ���� ������ �� �߻��ϴ� �̺�Ʈ
- JoinExistingRoomEvent : �̹� �����ϴ� �濡 �����Ҷ� �߻��ϴ� �̺�Ʈ
- QuitChattingEvent : ����ڰ� ���� ������ �� �߻��ϴ� �̺�Ʈ
- SendMessageEvent : ����ڰ� �޽����� �Է��ϰ� ������ �� �߻��ϴ� �̺�Ʈ

###��� �����ϳ���?
##### 1. ���ϴ� ���丮 ��ġ���� �Ʒ� �� ��ɾ� ����
```
$ git clone https://github.com/YoungEun-IN/CommunicatorServer
```
```
$ git clone https://github.com/YoungEun-IN/CommunicatorServer
```
##### 2. cmd â���� ������Ʈ ��ġ�� �̵�
##### 3. �Ʒ� ��ɾ� ���������� ����
```
$ java /bin/ChattingServer/ChattingServer

```
```
$ java /bin/ChattingCient/ChattingCient

```
- ���� : ä�� ���α׷��� 5000 ��Ʈ�� ����մϴ�.

### ������
- �ڵ��� ������� ����ȭ�� �� �������.
- �ʹ� ���谡 ���� �������. ���� �и��� Ư�� �������.

##### TODOLIST
  - [ ] MSA ȯ������ ���׷��̵�
  - [ ] �α��� ��� �߰�
  - [ ] ���� log�� �����ϴ� ��� �߰�
  - [ ] ä�ù� ���� ����
  - [ ] ��� ��� �ʵ� �ܺ����Ϸ� ���ų� Ŭ����ȭ
- ����ϰ�ʹ�.

### License
This is released under the MIT license. See [LICENSE](LICENSE) for details.



