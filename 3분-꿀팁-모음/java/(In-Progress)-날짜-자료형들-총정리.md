# 날짜 관련 자료형들 총정리

> 일단, Calendar 타입의 자료형들을 정리하는 것은 Pass 했다. Calendar 클래스는 Java 버전이 올라가면서 싱글턴 사용으로 인한 문제점들이 많이 보완되었지만, 서버 애플리케이션을 구현하는 데에는 적합하지 않다는 생각이 들어서이다. 실제로 소스를 열어보면 프록시를 이용해서 어느 정도는 보완했다는 생각은 들었는데, 역시나 처음 배울 때 부터 편견을 가지게 되어서 인지 선뜻 서버 애플리케이션에 사용하기에는 주저하게 되는 것 같다. <br>
>
> (10년 이상 된 레거시 코드에서는 아직도 Calendar 클래스를 사용하는 경우도 있기는 하다. 나의 경우는 전 직장에서 팀장님이 캘린더 클래스를 Mocking 하는 테스트 환경을 꼭 만들어야 한다며 1주일의 시간을 주고 이것만 일을 시키신 적이 있었는데, 다른 JIRA 이슈를 받아와서 2주일 뒤에야 해당 이슈를 보류 시키는 식으로 잘(?) 넘어간 기억이 있다.)

<br>

ZonedDateTime, LocalDateTime 에 대해서도 정리하면 좋겠지만, OffsetDateTime 으로 모든 예제를 통일해서 정리해볼 예정이다. 사실 제공되는 API 는 ZonedDateTime, LocalDateTime, OffsetDateTime 모두 거의 비슷한 기능들을 제공해주고 있고, 타임존 관련해서만 상이한 면이 있다.<br>

<br>

## DateTimeFormatter - 포맷 문자열

언뜻 보기에 쉬운 내용일수도 있겠다. 하지만, 글로벌 서비스시에는 자세히 파악해야 하는 내용이어서 정리 시작하기로 함. 최대한 각 포맷의 의미를 요약하는 식으로 정리 필요.<br>

<br>

## OffsetDateTime 타입을 long 타입으로 변환

<br>

## Long 타입을 OffsetDateTime 으로 변환

<br>

## java.util.Date 타입을 OffsetDateTime 으로 변환

<br>

## 월 단위로 날짜 타입 순회하기

<br>

## 일 단위로 날짜 타입 순회하기

<br>

## OffsetDateTime Serialization 에러

객체를 Serialization 하는 것은 Disk 에 객체의 내용을 저장해두거나 이종의 기기(원격지 서버, 원격지 클라이언트 등)에 데이터를 전달해줄 때에 필요하다. 객체 자체에 Serialization 을 했다고 해도 OffsetDateTime, LocalDateTime 관련해서는 또 별도의 작업이 필요하다. (차주 정리 시작 예정)

### 참고자료

- Stack Over Flow - [serialize / deserialize java 8 java.time with Jackson JSON mapper](https://stackoverflow.com/questions/27952472/serialize-deserialize-java-8-java-time-with-jackson-json-mapper)
- jackson Faster XML GitHub - [github.com/FasterXML/jackson-modules/java8](https://github.com/FasterXML/jackson-modules-java8)
  - jackson 모듈을 fasterXML 에서 사용하는 것에 관련자료 깃헙



**스택오버플로우 원문**

There's no need to use custom serializers/deserializers here. Use [jackson-modules-java8's datetime module](https://github.com/FasterXML/jackson-modules-java8):

Datatype module to make Jackson recognize Java 8 Date & Time API data types (JSR-310).

[This module adds support for quite a few classes:](https://fasterxml.github.io/jackson-modules-java8/javadoc/datetime/2.9/com/fasterxml/jackson/datatype/jsr310/JavaTimeModule.html)

- Duration
- Instant
- LocalDateTime
- LocalDate
- LocalTime
- MonthDay
- OffsetDateTime
- OffsetTime
- Period
- Year
- YearMonth
- ZonedDateTime
- ZoneId
- ZoneOffset



