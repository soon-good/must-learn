# RabbitMQ 기본 개념들

## 참고자료

Part1. RabbitMQ for Begginers What is RabbitMQ?<br>

- https://www.cloudamqp.com/blog/part1-rabbitmq-for-beginners-what-is-rabbitmq.html<br>
- 베이스로 참고한 자료. 그림이 깔끔하게 잘 그려져 있어서 참고하게 되었다.<br>

Benchmarking Apache Kafka, Apache Pulsar, and RabbitMQ<br>

- https://www.confluent.io/blog/kafka-fastest-messaging-system/ 

<br>

## 목차

- [Producer, Broker, Consumer](#Producer--Broker--Consumer)<br>
  - [생산자 (Producer)](#생산자--Producer-)<br>
  - [래빗 MQ, 메시지 브로커 (Broker)](#래빗-MQ--메시지-브로커--Broker-)<br>
  - [소비자 (Consumer)](#소비자--Consumer-)<br>
- [예제 : PDF 생성 요청에 대한 메시지 큐 통신 절차](#예제---PDF-생성-요청에-대한-메시지-큐-통신-절차)<br>
- [Exchange 의 개념](#Exchange-의-개념)<br>
- [Exchange 의 유형들](#Exchange-의-유형들)<br>
- [간단 용어 정리](#간단-용어-정리)<br>

<br>

## Producer, Broker, Consumer

RabbitMQ의 기본적인 메시지 전달 flow 는 아래와 같다.<br>

( 이미지 출처 : https://www.cloudamqp.com/img/blog/workflow-rabbitmq.png ) <br>

![이미지](https://www.cloudamqp.com/img/blog/workflow-rabbitmq.png)

위 그림을 보면 아래와 같이 크게 3가지의 요소가 있는 것을 확인할 수 있다.<br>

- Producer (생산자)<br>
- Broker<br>
- Consumer(소비자)<br>
- 소비자 (Consumer)<br>

<br>

### 생산자 (Producer)

`생산자`는 보통 클라이언트 애플리케이션 이거나, 외부 소켓 API의 데이터를 받아오는 모듈을 예로 들 수 있다. 예를 들면, 국내 증권 거래소의 푸시 메시지를 받아서 실시간 주식 현황을 보여주려고 하는 경우를 예로 들어보자. 이때 생산자에 해당하는 부분은 증권거래소의 실시간 API와 소켓통신을 하고, 푸시받는 주식 데이터를 래빗MQ에게 브로커가 받아들일수 있는 자료구조로 변환해 토스해주는 부분이 될 수 있겠다. <br>

즉, 증권거래소의 푸시 데이터를 RabbitMQ가 알아듣는 자료구조로 변환해서 전달해주는 부분이 생산자(Producer)가 될 수  있다.<br>

<br>

### 래빗 MQ, 메시지 브로커 (Broker)

`레빗 MQ`는 `메시지 브로커`  역할을 수행한다. 생산자와 소비자 사이에서 메시지의 오고 가는 것을 중재를 해주는 역할을 수행한다. 이 메시지 브로커의 종류에는 여러가지가 있다. ActiveMQ, Pulsar, Kafka 를 예로 들 수 있다. 개인적으로 괜히 메시지 브로커... 라는 말을 써서 어려운 용어를 쓰는 듯한 느낌을 주거나, 괜히 후까시...를 넣는 듯한 느낌은 마음에 들지 않는다. 근데 또 메시지를 중재해주는 브로커라는 단어를 사용한 것은 또 가끔은 기발한 발상같기도 하다. 마피아게임 같은 느낌도 조금은 들기도 하고...<br>

>  참고 - 각 브로커 애플리케이션들의 가용성 및 여러가지 장단점들<br>
>
> - https://www.confluent.io/blog/kafka-fastest-messaging-system/ )<br>

<br>

### 소비자 (Consumer)

`소비자`는 레빗MQ의 브로커로부터 메시지를 받아 소비하는 역할을 한다. 실시간 주식 현황 프로그램을 만드는 것을 예로 들어보자. RabbitMQ 가 전달해주는 실시간 주식 데이터를 브라우저에 표현해주는 브라우저 내의 로직을 소비자라고 할 수 있겠다. (생산자는 증권거래소의 푸시데이터를 RabbitMQ가 알아듣는 자료구조로 전달해주고, MQ는 원하는 소비자에게 해당 데이터를 전달해준다.)<br>

소비자는 래빗 MQ내에 개발자들이 익스체인지와 큐로 라우팅 키 및 바인딩을 통해 연결고리를 만들어놓은 구체적인 내용에 대해 알 필요 없이 데이터만 받아서 사용하면 된다. 물론 소비자 로직 역시 개발자가 작성한다.<br>

<br>

## 예제 : PDF 생성 요청에 대한 메시지 큐 통신 절차

(이미지 출처 : https://www.cloudamqp.com/img/blog/rabbitmq-beginners-updated.png ) <br>

![이미지](https://www.cloudamqp.com/img/blog/rabbitmq-beginners-updated.png)

1. 사용자가 **PDF 파일로 다운로드** 라는 버튼을 누른 상태이다.<br>

2. PDF 생성 요청은 Producer 애플리케이션에게 리퀘스트 객체로 전달된다.<br>

3. Publisher 애플리케이션은 이것을 메시지로 만들어서 RabbitMQ 내의 Exchange(거래소)에 Publish(발송)한다.<br>

   RabbitMQ내의 Exchange(거래소)는 메시지의 라우팅 키를 보고 알맞은 큐에 바인딩 한다.<br>

4. RabbitMQ내의 Queue는 어떤 생산자에게 전달되어야 하는 메시지인지를 판단해서 알맞은 Consumer(소비자)에게 메시지를 전달해준다.<br>

<br>

## Exchange 의 개념

[스프링 인 액션](http://www.yes24.com/Product/Goods/90180239) 에서는 Exchange를 거래소 라고 번역하고 있다. 어찌 보면 정말 적절하게 번역한 것 같다는 생각을 자주 했었다. Exchange는 생산자로부터 받은 메시지를 라우팅 키를 기반으로 특정 큐에 바인딩하는 역할을 수행한다.<br>

<br>

> **참고**<br>
>
> - Work Queue 기반 AMQP<br>
>   - https://www.rabbitmq.com/tutorials/tutorial-two-spring-amqp.html <br>

<br>

Exchange 를 사용하지 않고, 생산자와 소비자를 직통으로 큐로 연결해 사용하는 것이 가능하기는 하다. 이런 방식을 `Work Queue` 방식이라고 한다. Work Queue 방식은 아래 그림 처럼 큐를 생산자와 소비자에 직접 연결해 사용한다. <br>

(이미지 출처 : https://www.rabbitmq.com/img/tutorials/prefetch-count.png) <br>

![이미지](https://www.rabbitmq.com/img/tutorials/prefetch-count.png)

하지만 보통 위와 같은 Work Queue 방식으로 메시지 교환방식을 구성하지는 않는다.<br>

아래와 같이 생산자와 소비자 사이에 Exchange를 두고, Exchange 뒤에 큐를 두어서 메시지를 교환하는 구조를 세팅하는 편이다. 메시지 교환방식은 대략 6가지 정도의 방법들이 있고, Exchange 의 종류도 여러가지가 있다. 이것에 관해서는 다음 문서에 정리해놓을 예정이다.(에버노트에 정리는 해놓았지만, 어느 정도 가다듬을 필요가 있어서 미뤄뒀다.)<br>

(이미지 출처 : https://www.cloudamqp.com/img/blog/exchanges-bidings-routing-keys.png)<br>

![이미지](https://www.cloudamqp.com/img/blog/exchanges-bidings-routing-keys.png)

보통 프로듀서는 익스체인지에게 메시지를 전달한다. 익스체인지는 메시지가 어떤 큐에 전달되어야 하는지를 라우팅 한다. 메시지를 큐에 라우팅할 때 라우팅 키를 기반으로 적절한 큐를 찾는데, 익스체인지와 큐 사이에 맺어진 관계를 바인딩이라고 하고, 바인딩을 찾을 때 사용하는 키를 라우팅 키라고 부르는 편이다.<br>

<br>

컨슈머는 주기적으로 브로커와 통신하면서, 컨슈머 자신이 연결되어 있는 Queue 에서 메시지를 꺼내간다.<br>

<br>

## Exchange 의 유형들

> 더 자세한 내용들은 [Part4 : RabbitMQ for beginners - Exchanges, routing keys and bindings](https://www.cloudamqp.com/blog/part4-rabbitmq-for-beginners-exchanges-routing-keys-bindings.html) 에 있다. 이 내용들을 이번 주 중으로 다른 문서에서 정리할 예정이다. (에버노트에 정리해두었지만, 정리해둔 내용들을 더 보완해야 한다...)<br>

<br>

(이미지 출처 : https://www.cloudamqp.com/img/blog/exchanges-topic-fanout-direct.png)<br>

![이미지](https://www.cloudamqp.com/img/blog/exchanges-topic-fanout-direct.png)

<br>

- Direct <br>
  - Direct 타입의 익스체인지(거래소)에서 메시지는 바인딩 키가 메시지의 라우팅 키에 맞는 큐에 라우팅 된다.<br>
  - 하나의 큐에 하나의 라우팅 키를 기반으로 바인딩하는 방식이다.<br>
  - 또는 하나의 라우팅 키를 여러 큐에 바인딩하는 것 역시 가능하다.<br>
- Topic<br>
  - 라우팅 키를 '.'으로 구분해서 하나의 경로처럼 구성할 수 있고, 여러가지 단계로 구성할 수 있다.<br>
  - 이때 디렉터리를 참조할 때처럼 와일드 카드를 사용해 *,# 과 같은 와일드 카드로 특정 조건에 해당하는 큐 들을 지목해서 메시지를 발송하는 것이 가능하다.<br>
- Fanout<br>
  - Fanout 방식은 Routing Key 가 아무 의미없다. 그냥 퍼블리셔에게서 RabbitMQ내의 Exchange(거래소)에 메시지를 전달 받으면, Fanout 방식의 Exchange(거래소)는 binding 된 모든 큐로 모든 메시지를 쏘아보낸다.<br>
  - 따라서 fanout 방식은 routingKey가 아무 의미 없다.<br>
- Headers<br>
  - 헤더는 라우팅에 메시지 헤더를 사용한다.<br>

<br>

## 간단 용어 정리

- **Producer:** Application that sends the messages.
- **Consumer:** Application that receives the messages.
- **Queue:** Buffer that stores messages.
- **Message:** Information that is sent from the producer to a consumer through RabbitMQ.
- **Connection:** A TCP connection between your application and the RabbitMQ broker.
- **Channel:** A virtual connection inside a connection. When publishing or consuming messages from a queue - it's all done over a channel.
- **Exchange:** Receives messages from producers and pushes them to queues depending on rules defined by the exchange type. To receive messages, a queue needs to be bound to at least one exchange.
- **Binding:** A binding is a link between a queue and an exchange.
- **Routing key:** A key that the exchange looks at to decide how to route the message to queues. Think of the routing key like an *address for the message.*
- **AMQP:** Advanced Message Queuing Protocol is the protocol used by RabbitMQ for messaging.
- **Users:** It is possible to connect to RabbitMQ with a given username and password. Every user can be assigned permissions such as rights to read, write and configure privileges within the instance. Users can also be assigned permissions for specific virtual hosts.
- **Vhost, virtual host:** Provides a way to segregate applications using the same RabbitMQ instance. Different users can have different permissions to different vhost and queues and exchanges can be created, so they only exist in one vhost.

<br>


