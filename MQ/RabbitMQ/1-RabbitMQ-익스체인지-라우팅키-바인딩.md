# RabbitMQ의 익스체인지, 라우팅키, 바인딩



## 참고자료

- [Part4 - RabbitMQ Exchanges, routing keys and bindings](https://www.cloudamqp.com/blog/part4-rabbitmq-for-beginners-exchanges-routing-keys-bindings.html)
- [RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)
- [RabbitMQ 삽질](https://shortstories.gitbook.io/studybook/message_queue_c815_b9ac/rabbitmq-c0bd-c9c8)
  - RabbitMQ의 메시지 전송 방식들을 빠르게 파악하는데에 도움이 되었던 자료이다.

<br>

## 바인딩, 라우팅키, 익스체인지

### 바인딩 (Binding)

Queue와 Exchange를 서로 바인드하기 위한 link 같은 개념이다. (실제로 URL처럼 생기기도 했다.)<br>

<br>

### 라우팅키 (Routing Key)

라우팅 키는 메시지 내의 속성(attribute)중 하나이다. (즉, 메시지내의 필드 중 하나라는 이야기). <br>

메시지가 어떤 큐로 라우팅 될지를 결정한다. 바인딩은 라우팅 키를 기준으로 익스체인지가 어떤 큐로 바인딩할 지를 결정한다.<br>

<br>

### 익스체인지 (Exchange)

익스체인지는 생산자로부터의 메시지를 큐와 연결해주는 중간계층의 요소이다.<br>

RabbitMQ에는 아래와 같이 여러개의 Exchange 타입들이 존재할 수 있다. 익스체인지의 종류에 따라 메시지의 전달방식이 각각 다르다. 아래 그림에서는 Direct, Topic, Fanout 타입의 익스체인지의 메시지 전달방식 및 큐에 바인딩하는 방식들을 정리하고 있다.

![이미지](https://www.cloudamqp.com/img/blog/exchanges-topic-fanout-direct.png)

<br>

## 여러가지 익스체인지들

### Fanout 익스체인지

참고 : https://www.rabbitmq.com/tutorials/tutorial-three-spring-amqp.html 

(Publisher, Subscriber 모델을 채택할 경우 `direct`, `topic` , `headers` , `fanout` 방식의 익스체인지를 선택할 수 있다. 공식문서에서는 Publisher, Subscriber 모델의 한 예로 fanout 을 예로 들어 설명하고 있다.)

>  There are a few exchange types available: direct, topic, headers and fanout. We'll focus on the last one -- the fanout. <br>

<br>

- 익스체인지 중 Fanout 이라는 익스체인지가 있는데, 이 것을 Fanout 익스체인지라고 부른다.

- 브로드캐스팅 같은 전송방식이다.

- Fanout 익스체인지를 사용하면, Publisher 가 어떤 메시지를 Exchange(거래소)로 전달했을 때 Exchange(거래소)는 binding된 모든 큐로 메시지를 쏘아 보낸다. 따라서 Fanout 방식은 라우팅 키(routing key)가 아무의미 없다.

<br>

![이미지](https://www.rabbitmq.com/img/tutorials/bindings.png)

<br>

### Direct 익스체인지

### Topic 익스체인지

### Default 익스체인지



