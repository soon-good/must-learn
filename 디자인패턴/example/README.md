# 디자인 패턴 개념 정리 예제 디렉터리

- 빌더패턴<br>
- 싱글턴패턴<br>
- 팩토리 메서드<br>

<br>

## 참고자료

- 빌더패턴<br>
  - [Adopting Builder Patter with Abstract class](https://cindystlai.wordpress.com/2017/04/20/adopting-builder-pattern-with-abstract-class/)<br>
    - Enum 을 조합하는 것이 아닌 Dto 를 빌드하는 빌더패턴을 구현할 것이기 때문에 이 자료가 가장 도움이 되었다.<br>
  - [백기선님의 스터디 자료](https://github.com/keesun/study/blob/master/effective-java/item2.md)를 보면서 아이디어를 얻었고 실제 자료 검색 후 [여기 ->](https://cindystlai.wordpress.com/2017/04/20/adopting-builder-pattern-with-abstract-class/)의 자료가 더 내 예제에 잘 맞는 케이스였던것 같다.<br>
  - [altongmon.tistory.com - T extends 타입, 와일드카드 타입, 제너릭 타입 상속](https://altongmon.tistory.com/241)<br>
  
  - [HttpStatus 상태코드 (SpringFramework)](https://github.com/spring-projects/spring-framework/blob/main/spring-web/src/main/java/org/springframework/http/HttpStatus.java) <br>
      - enum 기반의 HttpStatus 상태코드를 참고해서, 예제에 필요한 부분들만을 추려내서 구현<br>
      - 참고 : [github/spring-projects/spring-framework/.../HttpStatus.java](https://github.com/spring-projects/spring-framework/blob/main/spring-web/src/main/java/org/springframework/http/HttpStatus.java)<br>
- 싱글턴패턴<br>
  - [JournalDev - Java Singleton Design Pattern](https://www.journaldev.com/1377/java-singleton-design-pattern-best-practices-examples#bill-pugh-singleton)<br>
    - 가장 유효하게 찾아본 자료이다. 이 자료를 기반으로 정리해볼 예정이다.<br>
  - [Inner class, static inner class-lazy loading](https://www.programmersought.com/article/35794331711/)<br>
    - 나는 inner class 가 로딩되는 시점을 약간은 내 마음대로 이해하고 있지 않나? 싶어서 찾아봤던 자료이다.<br>
    - 도움이 많이 되었다.<br>
  - [케빈 tv 방송 - 패턴이야기 - 싱글톤 패턴 (1부)](https://www.youtube.com/watch?v=Ba7iYO7_BPc)<br>
  - [케빈 tv 방송 - 패턴이야기 - 싱글톤 패턴 (2부)](https://www.youtube.com/watch?v=ZrF8r5LUadc)<br>
    - 순진한 코더 1년차 일때 싱글톤 패턴에 관심을 가지게 해준 방송 ㅋㅋ<br>
  - [백기선님 effective java](https://github.com/keesun/study/blob/master/effective-java/item3.md)<br>
  - [yaboong.github.io - thread-safe-singleton-patterns/](https://yaboong.github.io/design-pattern/2018/09/28/thread-safe-singleton-patterns/)<br>

<br>

## 테스트 환경설정 (JUnit5)

- [참고 1 - github/junit-team/junit5-sample](https://github.com/junit-team/junit5-samples/blob/r5.7.1/junit5-jupiter-starter-gradle/build.gradle)<br>
- [참고 2 - JUnit5 User Guide](https://junit.org/junit5/docs/current/user-guide/)<br>

<br>

아래의 메이븐 의존성 추가<br>

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.7.0</version>
    <scope>test</scope>
</dependency>
```
<br>

java 11 을 사용할 경우 부가적으로 아래의 내용을 추가해주어야 한다.([참고](https://www.inflearn.com/questions/19302))<br>

```xml
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <configuration>
          <source>11</source>
          <target>11</target>
        </configuration>
      </plugin>
    </plugins>
  </build>
```
<br>

이 외에 파라미터 테스트(Repeat, Parameteraized, etc) 를 사용하려면 아래의 의존성도 추가해주어야 한다.<br>

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-params</artifactId>
    <version>5.7.0</version>
    <scope>test</scope>
</dependency>
```