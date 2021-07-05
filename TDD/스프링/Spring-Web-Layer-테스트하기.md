# 스프링 Web Layer 테스트하기

스프링의 Web 계층만을 테스트하는 몇 가지 방식들을 정리해보려 한다. 다만, 스프링부트에 한정해서 정리할 예정이다. 

<br>

## 참고자료

- Testing the Web Layer
  - [https://spring.io/guides/gs/testing-web/](https://spring.io/guides/gs/testing-web/)

<br>

## 테스트를 하는 이유

> 그냥 나의 주관적인 의견을 적어봤다. 직접 경험해보면서 이래서 필요한 것 같다 했던것을 정리해보려 한다.

<br>

**코드에 대한 확신**<br>

테스트를 작성하는 이유는 간단하다. 내가 작성한 코드를 단순히 육안으로 확인하는 것만으로는 확신이 안들때가 많다. 이런 경우에 대해 테스트 코드를 작성해둔다면 나중에 특정 테스트 코드를 통해 기능이 잘 동작함을 객관적인 결과물로 확인할 수 있는 좋은 사례가 될 수 있다. 

<br>

**테스트 자동화**<br>

또, 배포 전에 꼭 통과되어야 하는 테스트 들을 정의해두었다고 해보자. 항상 통과되어야 하는 멱등성을 가진 이런 로직 들은 배포 전에 실패하면 네트워크든, 데이터베이스든, 외부 API 든 뭔가 문제가 있는 것이다. 또는 기본 기능의 검증을 꼭 해야 하는 관례가 있다면 1차적인 배포전 테스트가 될 수 있기 때문에 좋은 예가 될 수 있다. 이런 것을 테스트 자동화라고 이야기하는 편이다.<br>

보통 이런 배포 전 테스트가 필요한 테스트 케이스들의 경우 태그들을 따로 걸어두어서 젠킨스같은 CI 툴에서 배포 전에 테스트시 해당 태그에 맞는 테스트만을 돌리는 등의 테스트 자동화 작업을 해두는 편이다.

<br>

**테스트 주도 개발 (TDD)**<br>

테스트 주도 개발이 뭔지 2년 전까지는 전혀 몰랐다. 2년 전에 어떤 분이 직접 알려주셔서 알았다. TDD와 테스트케이스를 작성하는 것은 전혀 다른 개념이다. TDD는 코드를 테스트 코드에서부터 코드를 개발하는 것을 의미한다. 테스트 케이스 작성은, 제품의 꼭 통과되어야 하는 테스트를 작성하는 것을 의미한다.<br>

실무에서 경험하면서, 로컬 톰캣과 DB 데이터소스를 직접 돌려보면서 개발을 진행하는 것보다는 소기능 하나 하나를 개발할때 테스트 환경에 한정해서 개발하는 것이 안전하다는 것을 느꼈다. 물론 버전관리시에 TDD로 작성한 코드를 커밋하기엔 좀 그렇긴 했다. 나중에 테스트 케이스를 작성한 것을 버전관리가 되도록 하는게 맞다는 생각을 했다.

<br>

## 테스트 시 웹 레이어를 구동시키는 세가지 방법들

- `@SpringBootTest` 
- `@AutoConfigureMockMvc` 
- `@WebMvcTest` 

<br>

### @SpringBootTest

스프링 애플리케이션 컨텍스트를 구동해서 테스트를 하는 방식이다.<br>

@SpringBootTest 어노테이션을 사용하면 메인 Configuration 클래스(예를 들면 @SpringBootApplication 과 함게 선언된)를 참조하고, 이 설정 파일을 이용해서 스프링 애플리케이션 컨텍스트를 구동시킨다.<br>

아래는 @SpringBootTest 어노테이션을 사용해 테스트를 구동하는 예제다. 스프링 부트 애플리케이션을 구동시켜서 테스트한다. 즉, 임베디드 톰캣을 직접 구동시켜서 하므로 어느정도는 무거운 테스트 방식이다.<br>

```java
package com.example.testingweb;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void greetingShouldReturnDefaultMessage() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
				String.class)).contains("Hello, World");
	}
}
```

- `webEnvironment = WebEnvironment.RANDOM_PORT`

- - 테스트를 위한 서버를 랜덤한 포트로 구동시킨다. (테스트 환경이 충돌날수도 있는 것을 피할 수있는 좋은 방법)

- `@LocalServerPort`

- - 할당받은 Server 포트를 멤버필드에 저장해두기 위해 사용한 어노테이션이다.

<br>

### @AutoConfigureMockMvc

`@AutoConfigureMockMvc` 어노테이션을 사용하면 서버를 구동시키지 않으면서, 서버가 동작하는 아래의 계층(레이어)만 테스트하는 방식이다.<br>

`@AutoConfigureMockMvc` 어노테이션을 사용하면 실제 HTTP 요청을 처리할 때와 동일한 방식으로 코드가 호출되지만 서버 시작 비용은 들지 않는다. 그리고 거의 모든 스택이 사용된다.<br>

`@AutoConfigureMockMvc` 어노테이션은 Spring 의 MockMvc를 사용하고, 테스트 케이스에서 `@AutoConfigureMockMvc` 를 사용한다.<br>

서버를 구동시는 것은 아니지만, 스프링 애플리케이션의 거의 모든 컨텍스트가 시작된다.<br>

```java
package com.example.testingweb;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TestingWebApplicationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello, World")));
	}
}
```

<br>

### @WebMvcTest

@AutoConfigureMockMvc 는 서버를 구동시는 것은 아니지만, 스프링 애플리케이션의 거의 모든 컨텍스트가 시작된다.<br>

@WebMvcTest 어노테이션을 사용하면 테스트 범위를 웹 레이어에 한정하도록 축소해서 테스트할 수 있다.<br>

하지만, web layer 만에 한정해서 테스트의 범위를 줄일 수 있다.<br>

또는 특정 컨트롤러에 한정해서만 @WebMvcTest를 사용할 수 있다. (ex. @WebMvcTest(HomeController.class) )<br>

```java
@WebMvcTest
public class WebLayerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello, World")));
	}
}
```

컨트롤러 계층에 다른 컴포넌트를 선언해두어서 의존성이 있을 경우 @MockBean 어노테이션을 사용해서 GreetingService 타입의 mock 객체를 생성하고 주입할 수 있다. 이건 Mockito 를 이용하여 사용가능하다.<br>

## MockMvcTest

