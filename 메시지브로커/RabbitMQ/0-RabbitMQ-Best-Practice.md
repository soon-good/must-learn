# 0. RabbitMQ Best Practice

초당 트래픽이 비교적 조금 되는 서비스의 중간 버퍼를 RabbitMQ로 두어 요청을 처리하는 작업을 시작하고 있다. 자료를 찾아보다 보면 후덜덜하다. 요즘 무슨 자료든 일단 읽다보면 이상하리만치 겁주는 말들이 많아서 자꾸 겁먹게 된다 ㅋㅋㅋ<br>

오늘 이 문서에서 정리하는 자료는 [RabbitMQ Best Practice](https://www.cloudamqp.com/blog/part1-rabbitmq-best-practice.html) 를 메인으로 참고했다. 가능한 한도내에서 의역을 하려고 했고, 이해가 안되는 부분은 어느 정도는 직역을 해놓으려 한다.<br>

<br>

## 참고자료

- [RabbitMQ Best Practice](https://www.cloudamqp.com/blog/part1-rabbitmq-best-practice.html)
- Bulk Publishing 에 대해 찾아본 자료들
  - [Publishing Messages - Reference Documentation](http://budjb.github.io/grails-rabbitmq-native/doc/manual/guide/publishing.html)
  - [Support RabbitMQ Batch Publish](https://gitmemory.com/issue/MassTransit/MassTransit/1332/727778266)
  - [Publishers - Batch Publishing](https://www.rabbitmq.com/publishers.html)
  - [Sending Messages in Bulk and Tracking Delivery Status](https://jack-vanlightly.com/blog/2017/3/11/sending-messages-in-bulk-and-tracking-delivery-status-rabbitmq-publishing-part-2)
- Lock message queue api / library for Java
  - JMS, JDBM3 등등 
  - [https://stackoverflow.com/questions/14201140/local-message-queue-api-library-for-java](https://stackoverflow.com/questions/14201140/local-message-queue-api-library-for-java)

<br>

## Keep your queue short (if possible)

대기열에 메시지가 많이 대기하도록 하지 말라는 이야기인 것 같다. <br>

대기열이 길어져서 메시지가 계속해서 많이 상주한다면, 물리적인 RAM 메모리는 한계치에 이르게 되어버린다. 이렇게 RAM 메모리가 한계치에 도달했을때 RabbitMQ는 큐 안에 있는 메시지를 디스크로 플러시 한다. 이렇게 플러시 하는 것을 페이지 아웃이라고 부른다. <br>

그런데 이렇게 메시지를 디스크로 플러시하는 것도 어느정도는 시간적인 비용이 든다. 소비자가 생산자의 데이터 공급 속도에 비해 빠르게 따라가지 못할 경우 이런 경우에 해당하는 이야기인 것 같다.<br>

주관적인 의견을 섞어서, 해석해보면 이렇다. <br>

예를 들어 주식 시세 조회 프로그램을 만든다고 해보자. 이 경우 실시간 데이터를 화면에 뿌려주는 단순 출력을 위한 큐를 구성할 때에는 소비자가 생산자의 속도를 따라갈 수 있다.<br>

하지만, 주식 시세를 DB에 저장한다고 해보자. 장 중이나 이런 시간대에는 굳이 DB의 데이터를 화면에 뿌릴 필요가 없다. 따라서 조금 느려져도 되고 지연된 쓰기를 해도 된다. 이 경우는 디스크에 어느 정도는 데이터를 저장해도 된다. 시간이 더 걸리든 상관 없다. 안정적으로 저장하는 것이 queue 의 처리 속도보다는 중요하기 때문이다.<br>

따라서 이런 경우는 아마도, 메시지를 단순 전달하는 큐 하나와 메시지를 받아서 저장하는 큐  이렇게 두가지를 분리하면 좋지 않을까 싶다.<br>

<br>

## Use Quorum Queues

RabbitMQ 3.8 부터는 Quorum Queue 라는 종류의 큐가 새롭게 추가되었다. 이 Quorum Queue 는 레플리케이션이 적용된 큐인데, 고가용성과 데이터 안정성을 제공해준다.

<br>

## Enable lazy queues to get predictable performance

lazy queue는 지연 대기열이라고 파파고에서는 번역된다. 지연 대기열은 클러스터를 더 안정적으로 만들 수 있고, 메시지가 디스크에 자동으로 저장되기 때문에 RAM 사용량이 최소화되지만 처리량은 조금 늦어진다.<br>

지연 대기열은 실시간 데이터를 보여주어야 하는 곳에서는 사용하지 않는다. 주식 시세 프로그램을 예로 들어보면, 시세를 실시간으로 단순 디스플레이하는 기능의 경우 소비자가 충분히 생산자의 속도를 따라갈 수 있다. 이런 경우는 지연 큐를 사용하지 않아도 된다. <br>하지만, 주식 시세 데이터를 저장하는 큐의 경우는 데이터를 한번에 몇만 건씩 모아서 저장하는 것이 효율적이고, DB에 데이터를 최대 1분 정도 늦게 저장한다고 해서 데이터의 시세를 보여주는 데에는 문제가 되지 않는다. 어차피 조회 페이지에서는 시세 데이터가 덮어쓰게 되기 때문이다.<br>

