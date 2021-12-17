# Java 설치 - 서버 설치 버전

Java를 서버에 설치하는 것에 대해 정리해보려고 한다. 설치해두고 나서 심볼릭 링크를 생성해둔 다음에 alias 를 잘 등록해두면, 꽤 편리해질 수 있다.<br>

전전 회사, 전 회사, 첫 회사를 통틀어서 서버설치, Java 설치는 모두 개발자들이 관리했었다. 현재 회사는 입사한지 한달이 된 후에 갑자기 DevOps 엔지니어가 설치를 해서 강제로 내보낸다. 심지어 개발자에게 현재 사용하고 있는 JVM이 뭔지도 물어보지도 않고 Java를 설치해서 내보낸다... 조금 황당하기는 했었는데 그러려니 하고 넘어갔었다. 그냥 넘어가지는 않았고... JVM을 따로 다운로드 후에 애플리케이션 구동 스크립트에서 별도의 JVM을 사용하도록 별도 지정을 했었다.<br>

큰 회사에서는 보통 Java 유료화 이슈나 보안 이슈에 대해 담당하는 개발자나 협업 모임이 있다. 몇 개월에 걸쳐서 다른 개발 부서와 협의해 나가는 과정을 거치는 것으로 보인다. 실제로 본적은 없지만 [이 글의 하단부](https://kakaoentertainrecruit.tistory.com/16)를 보면 플랫폼 개발을 담당하는 개발자 분께서 무료 OpenJDK를 선정하게 된 것에 대한 이야기가 다뤄지고 있다.<br>

예전 회사에서는 AdoptOpenJDK를 사용했다. 오늘은 AdoptOpenJDK를 설치하는 과정에 대해 정리할 예정이다.<br>

<br>

## 참고자료

- [Ubuntu/CentOs 에서 Java 버전을 빠르게 바꾸는 방법](https://wgtech.github.io/posts/2019/07/14/Change-the-JDK-using-update-alternatives/)
- [update-alternatives : symbolic link 관리 명령어](https://donghwi-kim.github.io/jekyll/update/2015/04/17/update-alternatives.html) 

<br>

## AdoptOpenJDK 다운로드

```bash
$ cd ~/env/java 
$ mkdir download 
$ wget https://github.com/AdoptOpenJDK/openjdk16-binaries/releases/download/jdk-16.0.1%2B9/OpenJDK16U-jdk_x64_linux_hotspot_16.0.1_9.tar.gz
$ wget https://github.com/AdoptOpenJDK/openjdk16-binaries/releases/download/jdk-16.0.1%2B9/OpenJDK16U-jre_x64_linux_hotspot_16.0.1_9.tar.gz
```

<br>

## JDK, JRE 압축풀기, bash_profile 등록

```bash
$ tar xvzf OpenJDK16U-jdk_x64_linux_hotspot_16.0.1_9.tar.gz
$ tar xvzf OpenJDK16U-jre_x64_linux_hotspot_16.0.1_9.tar.gz

$ mv jdk-16.0.1+9 jdk16
$ mv jdk-16.0.1+9-jre jre16

# jdk16 디렉터리를 ~/env/java/java16 로 변경
$ mv jdk16 ~/env/java/java16
# jre 디렉터리를 ~/env/java/java16/jre 로 이동
$ mv jre ~/env/java/java16/jre

# 옮겨놓은 디렉터리로 이동
$ cd ~/env/java/java16
```

<br>

**bash_profile 수정**<br>

조금 뒤에 조금 더 추상화할 예정이다. 지금은 java16 버전의 jdk, jre 디렉터리를 명시적으로 지정하고 있다.

```bash
$ vim ~/.bash_profile
JAVA_HOME=/home/ec2-user/env/java/java16
JRE_HOME=/home/ec2-user/env/java/java16/jre
CLASSPATH=/home/ec2-user/env/java/java16/lib/tools.jar

PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin:$CLASSPATH:$HOME/.local/bin:$HOME/bin

export PATH
```

<br>

## update-alternatives

update-alternatives 명령어의 각 옵션에 대해서는 코드 하단부에 정리해두었다. 자세한 내용은 [update-alternatives : symbolic link 관리 명령어](https://donghwi-kim.github.io/jekyll/update/2015/04/17/update-alternatives.html) 을 참고하자.

```bash
$ sudo update-alternatives --install /usr/bin/java java ~/env/java/java16/bin/java 1

$ sudo update-alternatives --config java

3 개의 프로그램이 'java'를 제공합니다.

  선택    명령
-----------------------------------------------
*  1           /home/ec2-user/env/java/java11/bin/java
 + 2           /home/ec2-user/env/java/java16/jdk16/bin/java
   3           /home/ec2-user/env/java/java16/bin/java
```

<br>

- `update-alternatives` 
  - 리눅스에서 제공하는 명령어인데, 심볼릭 링크를 관리하는 명령이다.
- `--install` 
  - update-alternatives 명령어를 이용해 새로운 심볼릭 링크를 등록하려고 할 때 사용하는 옵션
- `/usr/bin/java` 
  - 생성할 심볼릭 링크의 이름. 위의 예제에서는 `/usr/bin/java` 로 지정하고 있다.
  - `/usr/bin/java` 는 `which java` 를 수행할 때 나타나는 심볼릭 링크의 위치다.
- `java`
  - 등록되는 심볼릭 링크가 사용될 명령어의 이름
- `~/env/java/java16/bin/java`
  - 실제 java16 버전의 바이너리 파일이 지정된 위치
  - 뒤에서 살펴볼 예제에서는 이 위치를 `/home/ec2-user/java/current` 라는 심볼릭 링크를 지정한다.
  - 즉 심볼릭 링크 `/usr/bin/java` 가 가리키는 java 파일의 위치는 `/home/ec2-user/java/current` 이다.
    - `/usr/bin/java` -> `/home/ec2-user/java/current`
- 1
  - 우선순위를 1로 지정했는데, 숫자가 낮을 수록 우선순위는 높다.

<br>

## 커스터마이징 1) 심볼릭 링크 생성

> 아래 내용은 [Ubuntu/CentOs 에서 Java 버전을 빠르게 바꾸는 방법](https://wgtech.github.io/posts/2019/07/14/Change-the-JDK-using-update-alternatives/) 에서 정리한 내용을 정리했다. 조금 기발한 방법이라 놀랬다. 뒤에 나오는 커스터마이증 2에서의 내용은 더 기발하다.

요약하자면 이렇다. <br>

- `/home/ec2-user/env/java/java16` 과 같은 특정 버전에 대한 java 버전을 심볼릭 링크인 `/home/ec2-user/java/current` 라는 이름으로 생성한다.
- 이것을 update-alternatives 명령어로 `--install` 하거나 `--config` 하는 등의 작업을 수행한다.

<br>

```bash
$ ln -s /home/ec2-user/env/java/java16 /home/ec2-user/env/java/current


$ ls -al /home/ec2-user/env/java

합계 0

lrwxrwxrwx  1 ec2-user ec2-user  30  9월 19 14:11 current -> /home/ec2-user/env/java/java16

drwxrwxr-x  2 ec2-user ec2-user 118  9월 19 14:02 download

drwxr-xr-x 10 ec2-user ec2-user 120  5월 20 13:55 java11

drwxr-xr-x 10 ec2-user ec2-user 120  9월 19 14:02 java16



# 심볼릭 링크를 update-alternatives 로 등록하기

$ sudo update-alternatives --install /usr/bin/java java /home/ec2-user/env/java/current/bin/java 1

$ sudo update-alternatives --config java
```

<br>

## 커스터마이징 2) alias 로 커스텀 명령어 만들기

> 이번 내용 역시 [Ubuntu/CentOs 에서 Java 버전을 빠르게 바꾸는 방법](https://wgtech.github.io/posts/2019/07/14/Change-the-JDK-using-update-alternatives/) 의 내용을 참고했다.<br>

**요약**

- `JAVA_HOME` 을 방금전 생성해줬던 `/home/ec2-user/java/current` 로 등록해준다. 
  - 이렇게 하면 심볼릭 링크이기에 자바 버전을 바꿀때는 심볼릭링크  `/home/ec2-user/java/current` 가 가리키는 실제 java 버전을 바꿔주기만 하면 `JAVA_HOME` 이 업데이트 되기에 조금은 편리해진 방법이다.
- `change-to-jdk16` 이라는 이름의 커스텀 명령어를 생성했다.
  - 명령어가 수행하는 동작은 이렇다.
  - 심볼릭 링크 `/home/ec2-user/env/java/current` 를 삭제한다. 
  - 새로운 자바 버전인 jdk16에 맞는 jdk 디렉터리 경로인 `/home/ec2-user/env/java/java16`  으로 새로운 심볼릭 링크를 생성한다.
- `change-to-jdk11` 이라는 이름의 커스텀 명령어는 그냥 만들어봤다. 

<br>

```bash
$ vim ~/.bash_profile

JAVA_HOME=/home/ec2-user/env/java/current
JRE_HOME=$JAVA_HOME/jre
CLASS_PATH=$JAVA_HOME/lib/tools.jar

PATH=$PATH:$JAVA_HOME/bin:$JRE_HOME/bin:$CLASSPATH:$HOME/.local/bin:$HOME/bin

export PATH


alias change-to-jdk16="rm /home/ec2-user/env/java/current && ln -s /home/ec2-user/env/java/java16 /home/ec2-user/env/java/current"
alias change-to-jdk11="rm /home/ec2-user/env/java/current && ln -s /home/ec2-user/env/java/java11 /home/ec2-user/env/java/current"


:wq
```

<br>

## 확인해보자

**JDK 11**<br>

```bash
$ change-to-jdk11

$ java -version
openjdk version "11.0.8" 2020-07-14
OpenJDK Runtime Environment AdoptOpenJDK (build 11.0.8+10)
OpenJDK 64-Bit Server VM AdoptOpenJDK (build 11.0.8+10, mixed mode)
```

<br>

**JDK 16**<br>

```bash
$ change-to-jdk16

$ java -version
openjdk version "16.0.1" 2021-04-20
OpenJDK Runtime Environment AdoptOpenJDK-16.0.1+9 (build 16.0.1+9)
OpenJDK 64-Bit Server VM AdoptOpenJDK-16.0.1+9 (build 16.0.1+9, mixed mode, sharing)
```

<br>

## ASDF ㅁㄴㅇㄹ

프로덕션 서버의 JDK 를 바꾸는 일은 흔하지 않다. 하지만 개발 용도 VM을 관리하는 상황이라면 JDK를 자주 바꿀일이 많다. 예를 들면, ElasticSearch를 사용하는 경우 Java 버전에 따라서 거지같은 에러를 자주 접해서ㅋㅋ Java 버전을 여러번 바꿔가며 설치하는 경우가 있다. 이렇게 초기 개발 단계 VM에서 JDK를 여러번 바꿔가면서 확인해야 할 경우 이렇게 alias와 심볼릭 링크를 만들어 둔다면, 좋은 선택이 되는 것 같다.<br>

<br>

