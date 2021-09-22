# T extends 클래스의 의미, 개념, 필요성

## 참고자료

이번달, 다음달에는 아무래도 Java의 기본적인 내용 + 디자인 패턴 들을 다시한번 정리하게 될것 같다. 일단 정리를 할 때 참고할 자료들의 리스트를 리스트텁해봤다. <br>

지료를 검색하다 보니, Effective Java 3/E를 반정도 읽다가 멈췄었는데, 알고보니 Effective Java에서 다루고 있는 제너릭 관련 내용이었다.<br>

일단은 웹 자료 기준으로 정리를 시작하고, 본격적으로 책을 읽으며 내용을 덧대는 것은 2022년 이후가 되지 않을까 싶다.<br>

- Effective Java 의 내용과 함께 정리된 곳 : [https://medium.com/@joongwon/java-java%EC%9D%98-generics-604b562530b3](https://medium.com/@joongwon/java-java의-generics-604b562530b3) 
- T extends Class 와 ? extends class 에 대해 정리 된 곳 : https://pathas.tistory.com/160 
- 간단예제로 정리된 곳 : [https://velog.io/@ldevlog/07.-T-extends-%ED%81%B4%EB%9E%98%EC%8A%A4](https://velog.io/@ldevlog/07.-T-extends-클래스)
- [altongmon.tistory.com - T extends 타입, 와일드카드 타입, 제너릭 타입 상속](https://altongmon.tistory.com/241)

<br>

아무래도 코드를 리팩토링하는 과정까지 거치고, 데이터 처리까지 거치는 부분이 있고, 외부 데이터 정책이 바뀌어서 필드 매핑을 새로 다시하다보니, 예전에 배웠던 디자인 패턴을 다시 꺼내들기 시작했었다. 스프링 위주의 채용공고 속에서 살아남기 위해 스프링, JPA를 공부했었는데, 어느 정도 선에서의 공부가 이루어진 다음이라면, 오히려 신입 개발자 시절 공부했던 디자인 패턴과 Effective Java 책 스터디가 오히려 업무에 더 큰 도움이 된다는 느낌을 받은 것 같다.<br>

어떻게든 재사용성이 높고 유지보수가 쉬운 코드를 만들어야 하기에...<br>







