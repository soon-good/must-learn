# RabbitMQ의 전송 모델들

계속 급하게 정리하느라 두서없고, 정신없기는 한데, 저번에 정리했던 내용([1-RabbitMQ-익스체인지-라우팅키-바인딩.md](https://github.com/gosgjung/must-learn/blob/develop/MQ/RabbitMQ/1-RabbitMQ-%EC%9D%B5%EC%8A%A4%EC%B2%B4%EC%9D%B8%EC%A7%80-%EB%9D%BC%EC%9A%B0%ED%8C%85%ED%82%A4-%EB%B0%94%EC%9D%B8%EB%94%A9.md))는 익스체인지의 종류였다.(중간에 정리와 공부를 함께 하다보니 개념이 헷갈려서 익스체인지의 종류가 전송 모델이라고 착각했었다.)<br>

<br>

RabbitMQ의 전송모델은 익스체인지와 큐를 어떻게 배치하느냐에 따라 메시지를 어떤 컨슈머로 분배할지가 결정된다. 처음 코드만 따라치는 방식으로만 하다가는, 미로 속에서 길을 잃은 것처럼 같은 코드를 여러번 고쳐가면서 똑같은 개념을 수정한 것인데, 다른 것이라고 착각하게 된다.<br>

<br>

따라서 처음부터 설계 단계에서 메시지를 전송하는 구조를 잘 선택해서 그림을 그려보고 코드를 쳐봐야 한다. 왜냐하면 MQ같은 메시지 전송 모델은 어찌보면 이벤트지향적(Event Driven)이기 때문에 의외로 코드는 간단하더라도, 그 시나리오를 파악하기 어려워지기 때문이다.<br>

<br>

## 참고자료

- [RabbitMQ Tutorials](https://www.rabbitmq.com/getstarted.html)

<br>

## 1. Work Queues

> https://www.rabbitmq.com/tutorials/tutorial-two-spring-amqp.html

생산자와 컨슈머가 큐에 바로 연결되어 있는 형태이다. 보통은 생산자 사이에서 익스체인지가 중재를 해주어야 하는데, 생산자가 직접 큐에 접근하는 구조이다. (태스크를 worker 들에게 분배해주는 구조이다. 참고 : [competing consumers pattern](https://www.enterpriseintegrationpatterns.com/patterns/messaging/CompetingConsumers.html) )

![이미지](https://www.rabbitmq.com/img/tutorials/python-two.png)

<br>

## 2. Publish, Subscribe

> https://www.rabbitmq.com/tutorials/tutorial-three-spring-amqp.html

다수의 소비자들에게 한번에 즉시 메시지를 보내는 구조이다.<br>

`direct` , `topic` , `headers` , `fanout`  타입의 익스체인지를 사용해서 Publish, Subscribe 하는 것이 가능하다.<br>

direct 역시 여러 컨슈머에 메시지를 직통으로 전달할 수 있기에 Publish, Subscribe 에 포함될 수 있다.<br>

![이미지](https://www.rabbitmq.com/img/tutorials/python-three-overall.png)

<br>

## 3. Routing

> https://www.rabbitmq.com/tutorials/tutorial-four-spring-amqp.html

메시지를 선택해서 받는 것이 가능하다. (Receiving messages selectively)<br>

주로 `direct` 익스체인지를 이용한다.<br>

direct 익스체인지 뒤의 라우팅 알고리즘은 단순하다. 메세지의 라우팅 키와 정확히 일치하는 바인딩 키를 가진 큐에 메시지가 전달되는 것을 의미한다. 즉, 큐에는 바인딩키가 있고, 메시지에는 라우팅 키가 있는데, 이것이 정확히 일치하는 곳으로 매칭된다는 의미이다. 뒤에서 살펴보게될 토픽과는 조금 다르다. 라우팅은 정확히 문자열이 일치해야 매칭된다.<br>

<br>

아래와 같은 형태로 매칭되는 것이 가능하다.<br>

![이미지](https://www.rabbitmq.com/img/tutorials/direct-exchange.png)

또는 아래와 같이 여러개의 큐에 같은 바인딩 키로 매칭하는 것이 가능하다.<br>

![이미지](https://www.rabbitmq.com/img/tutorials/direct-exchange-multiple.png)

또는 아래 그림과 같은 형식처럼 여러 종류의 바인딩 키가 하나의 큐에 연결되어 있고 여러 종류의 라우팅 키를 하나의 바인딩 키에 매칭하는 것 역시 가능하다.<br>

![이미지](https://www.rabbitmq.com/img/tutorials/python-four.png)

<br>

## 4. Topics

> https://www.rabbitmq.com/tutorials/tutorial-five-spring-amqp.html

패턴에 기반해서 메시지들을 받는 것이 가능하다. 예를 들면 와일드 카드 ( `*` , `#`  )로 큐를 지정하는 것이 가능하다.<br>

1,2,3 까지는 fanout, direct 를 사용했었다. `Topics` 모델은 topic 이라는 익스체인지를 사용한다.<br>

<br>

![이미지](https://www.rabbitmq.com/img/tutorials/python-five.png)

<br>

## 5. RPC

> https://www.rabbitmq.com/tutorials/tutorial-six-spring-amqp.html

[RPC](https://namu.wiki/w/RPC) 는 [Request/reply pattern](http://www.enterpriseintegrationpatterns.com/patterns/messaging/RequestReply.html) 을 추상화한 모델이다. 원격 프로시저 호출이라고 하는데, 쉽게 설명하면 원격지의 메서드를 실행하도록 하는 모델이다.<br>

<br>

![이미지](https://www.rabbitmq.com/img/tutorials/python-six.png)

<br>

