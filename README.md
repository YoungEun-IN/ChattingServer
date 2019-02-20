# Communicator server

Java Chatting Application
===

<a href="https://1ilsang.blog.me/"><img src="https://img.shields.io/badge/blog-1ilsang.blog.me-red.svg" /></a>
<a href="#"><img src="https://img.shields.io/github/last-commit/1ilsang/java-mvc-chatting.svg?style=flat" /></a>
<a href="#"><img src="https://img.shields.io/github/languages/top/1ilsang/java-mvc-chatting.svg?colorB=yellow&style=flat" /></a>
<a href="#"><img src="https://img.shields.io/badge/license-MIT-green.svg" /></a>

��Ƽ ä�� ���� ������Ʈ.

- �ٽ� ��ǥ: ������, MVC, ������, ������ ¤���.
- ���� �Ⱓ: �� 10��
- Keyword: MVC, Thread, Socket, Polymorphism, Serializable, Swing, Singleton, Strategy Pattern

<br/>

���� ȭ��
---
AWT�� ���ߵǾ��� ������ �����쿡���� ��Ȥ�� �並 ���ž� �մϴ�(...)

| Login View | Chat View |
|:----------------------------------------:|:-----------------------------------------:|
|<img src="markdown/img/login.gif" width=300 />|<img src="markdown/img/chat.gif" width=300 />|

- Server Log

<img src="markdown/img/serverlog.gif" width=550 />

<br/>

�Ѵ��� ���� ��ü ����
---
<img src="markdown/img/simplePackageDiagram.png" width=800 >

<hr/>

<br/>

<img src="markdown/img/serverDiagram.png" width=550>

- Server:
  - ������ ����� ���ÿ� `LogThread`, `LoginSocketThread`, `ChatRoomSocketThread` 3���� �����带 �Ļ���Ų��.
  - `LogThread`�� ������ ������ ��� ��û/�޽��� ���� �α׿� ��� �� �ؽ�Ʈ ���Ͽ� �����Ѵ�.
  - ��� �α״� resources/chatLog.txt �� �ڵ� ����ȴ�.
  - �̶� ���� ������ �����ֱ� ���� `queue`�� ��� ���������� �����Ѵ�.
  - `LoginSocketThread`�� �α��� ó���� �ϴ� *����* �������, DB�� ������� �����Ƿ� **Map �ڷᱸ��**�� ����� "�̸�":"������ü"�� �����Ѵ�.
  - �׷��Ƿ� ������ ����۵Ǹ� ���������� �������.
  - `ChatRoomSocketThread`�� Ŭ���̾�Ʈ�� ��û������ �޾� �� ���ϸ��� `ChatSocketThread`�� �Ļ���Ų��.
  - `ChatSocketThread` ��ü�� Ŭ���̾�Ʈ�� ������ �м��� ä�ù溰�� ������ �����ϰ� �޽����� �ѷ��ش�.

- [Read more!](markdown/index/Server.md)
<br/>

<img src="markdown/img/clientDiagram.png" width=1000 />
<img src="markdown/img/logic.png" width=800 />

- Client: 
  - ��� ��û�� `DispatcherController`�� ��ġ�� `HandlerMapping` ��ü�� �ش� �����Ͻ��� ������ `Controller`�� ã�� �޼��带 �����Ѵ�.
  - �� `Controller`�� ���� �����Ͻ� ������ ���� �ڽ��� `Service`�� ��û ��ü�� �ѱ��.
  - `Service`���� ������ ����ϴ� Login, Chatting ������ ������. ����� ��� `����ȭ`�� ��ü�� ����Ѵ�.
  - Chatting ������ ���Ḷ�� `ChatAcceptThread` ������ ��ü�� �Ļ��� �����Ѵ�.
  - ������ ��û�� `ModelAndView` ��ü�� ��� ���̾���(��Ʈ�ѷ�/��/���� ��)�� ��ģ��.
  - Service �� ���� ó���� �����ʹ� `return` �Ǿ� `DispatcherController`���� ���޵Ǹ� �̶� `ViewResolver`�� �������� ������ ȭ�鰴ü�� ã�´�.
  - ������ ȭ�鰴ü�� ã�Ҵٸ�  `View` ��ü�� ���� �������� ȭ���� �����ش�.
  - ��� ��Ʈ�ѷ��� ����, ��� �̱������� �����Ǿ� �ִ�.

- [Read more!](markdown/index/Client.md)

<br/>

��� �����ϳ���?
---
- ���� ȯ�濡�� �����ϱ�
  - client ������ server ������ ���� *root*�� ��� ChatTest, ChatServer Ŭ�������� main ����.
  - *�߿�* `server/resources/` ������ �ִ� `chatLog.dat` ������ `chatLog.txt`�� �������ּž� �մϴ�.
  - ����ȯ���̹Ƿ� `REMOTE_HOST`�� `127.0.0.1`���� �� Ȯ��.

- [Ŭ���� ȯ��(GCP)���� ���� �غ���](markdown/index/Gcp.md)
  - GCP Console �� ����.
  - VM �ν��Ͻ��� �����Ѵ�.
  - Server �� jar ���Ϸ� �����.
  - ssh Ű�� �����.
  - gcp �� �߰����ش�.
  - scp �� ���� VM�� jar ������ ������.
  - ä����Ʈ `7777`�� �α�����Ʈ `6666`�� �����ش�.
  - ��������. ```java -jar ���ϸ�.jar```
  - Client �� jar ���Ϸ� �����.(REMOTE_HOST ����)
  - client jar ������ �����Ѵ�.(�� �غ���: exe ���Ϸ� ��ȯ)
  - ��հ� ä���Ѵ�!

Ŭ���� ������ �˾ƺ���
---
- [Client](markdown/index/Client.md)
  - Main
  - Controller
  - DTO
  - Domain
  - View
  - Service
  - Thread
  - Util
  
- [Server](markdown/index/Server.md)
  - Main
  - Thread
  - DTO/Domain �� client �� ����.
  
������
---
- �ڵ��� ������� ����ȭ�� �� �������.
- �ʹ� ���谡 ���� �������. ���� �и��� Ư�� �������.
- TODOLIST
  - [ ] MSA ȯ������ ���׷��̵�
  - [ ] �ߺ� �α��� ����
  - [ ] ���� log ��ġó�� �� �ڵ�ȭ
  - [ ] ä�ù� ���� ����
  - [ ] ��� ��� �ʵ� �ܺ����Ϸ� ���ų� Ŭ����ȭ
  - [ ] ä�ù� ����� �±װ� ��� ���̴� ���� �ذ�
- ����ϰ�ʹ�.
- [Come to my Blog!](https://1ilsang.blog.me)

License
---
This is released under the MIT license. See [LICENSE](LICENSE) for details.


## Getting Started

```
$ git clone https://github.com/Morzan3/CommunicatorServer
```

## Running the app

We run the app as normal java application

```
$ java /src/pl/slusarczyk/ignacy/CommunicatorServer/CommunicatorServer.java

```

Server will start listening on port 5000
