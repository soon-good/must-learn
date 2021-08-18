# RabbitMQ 설치하기

현재 회사에서는 RabbitMQ 인스턴스를 직접 설치해서 운영하는 방식을 채택하고 있다. 인프라 팀에서 RabbitMQ 를 처음으로 도입해보는 것이라고 해서 Erlang 설치과정, Rabbit MQ 설치과정까지는 그대로 문서로 만들어 전달했었는데, 그 문서를 조금 더 정리해서 적어보려 한다. <br>

단순 설치법이라 방법들이 인터넷에도 널려있지만 영어라서 선비들이 영어를 읽지못하는 것일뿐, 저작권 문제는 없을듯하다. <br>

뭐 나도 파파고를 어느 정도를 이용하니깐 선비는 맞지만, 융통성있는 선비다. 진짜 고집센 선비는 지가 한걸 확인을 안하는 선비다. 지가 읽은게 맞다고 생각한다.말도 걸기 싫다.극혐쓰.<br>

<br>

## 참고자료

- [Install - Debian and Ubuntu](https://www.rabbitmq.com/install-debian.html)
- [Installing on Debian and Ubuntu](https://www.rabbitmq.com/install-debian.html#manual-installation)
- [RabbitMQ packages have unmet dependencies](https://askubuntu.com/questions/1188699/rabbitmq-packages-have-unmet-dependencies)

<br>

## apt-key 

RabbitMQ 설치시에 Erlang 언어를 설치해야 하는데 이 때 apt-key 관련해서 문제가 있다.<br>

자세한 내용은 [Install - Debian and Ubuntu](https://www.rabbitmq.com/install-debian.html) 를 참고<br>

```bash
$ sudo apt-key adv --keyserver "hkps://keys.openpgp.org" --recv-keys "0x0A9AF2115F4687BD29803A206B73A36E6026DFCA"
$ sudo apt-key adv --keyserver "keyserver.ubuntu.com" --recv-keys "F77F1EDA57EBB1CC"
$ curl -1sLf 'https://packagecloud.io/rabbitmq/rabbitmq-server/gpgkey' | sudo apt-key add -
```

<br>

## 우분투 리포지터리 추가

공식문서에서 언급하기로는 ubuntu 20 부터는 `focal` 이라는 저장소를 사용해야 Erlang 을 설치할수 있다. 아래 명령어는 ubuntu20 에서 focal을 사용해야 한다는 부분의 명령어를 요약한 내용이다.

```bash
$ sudo tee /etc/apt/sources.list.d/rabbitmq.list <<EOF
# 나타나는 콘솔창에서 아래의 명령을 차례로 입력해준다. 
# 자세한 내용은 공식문서를 참고할것 

> deb http://ppa.launchpad.net/rabbitmq/rabbitmq-erlang/ubuntu focal main
> deb-src http://ppa.launchpad.net/rabbitmq/rabbitmq-erlang/ubuntu focal main
> deb https://packagecloud.io/rabbitmq/rabbitmq-server/ubuntu/ focal main
> deb-src https://packagecloud.io/rabbitmq/rabbitmq-server/ubuntu/ focal main
> EOF

가장 마지막은 EOF 라는 문자를 타이핑해서 빠져나오기
```

<br>

## RabbitMQ, apt-transport-https 설치

```bash
$ sudo apt-get update -y
$ sudo apt-get install rabbitmq-server -y --fix-missing
$ sudo apt-get install apt-transport-https
```

<br>

## RabbitMQ 구동

```bash
$ sudo service rabbitmq-server start
$ sudo service rabbitmq-server status
$ sudo service rabbitmq-server stop
```

<br>

##  각 포트의 용도

나는 개발자라 설치말고도 해야할 일이 산더미이지만, 주의 첫날인 월요일, 인프라팀에서는 딱 위에것 까지만 하고 내비두었는데, 관리자 페이지는 애플리케이션에서 접속이 안되길래, 말해봐야 개발일정만 늦어질것 같아 주말 바로 전인 목요일에 이야기 하고 치빠했었다. <br>

혹시라도 그 사람도 이 문서를 볼수도 있는 가능성을 배제하지 않을 수는 없어서 이 부분은 8월 말에 정리해야 할 것 같다. <br>

<br>

CLI를 앱포트로 알고 있고 앱포트는 뭔 노드끼리 통신하는 포트라고 하고, 인터노드는 뭔지도 모르고, 뭐가 뭔지  잘 파악도 안하고 아는둥 마는둥 이야기 하고. 설치 완료된 후에는 포트요약도 개판이고, 문서정리는 제대로 하지도 못하고, 이상하고 한숨만 나온다... 열정만 있고 자기PR만 감상적으로 하는 신입을 아무렇게나 뽑은게 아닌가 하는 생각이 들었었다.<br>

참고로... 상용에 나가야할걸 이렇게 대충 몇 시간만에 후두려 패서 대충 싸질러서 전달해주는건... 전직장에서는 상상도 못할 개념없는 일이었다. <br>

<br>

