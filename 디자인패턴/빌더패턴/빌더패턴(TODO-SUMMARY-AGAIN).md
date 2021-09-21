# 빌더패턴(TODO SUMMARY AGAIN)

간단한 버전의 빌더 패턴이 있고, 더 복잡한 버전의 빌더 패턴이 있다. 실제 상용서비스의 특정 팀에서 공통으로 사용할 알림톡 API를 개발할 때 해당 서비스 팀에서 10년 가까이 일하신 개발자 분이 빌더패턴을 사용해보는 것을 제안해주셨었다.

이렇게 리뷰를 해주셔서 기존 소스와 [백기선님의 스터디 자료](https://github.com/keesun/study/blob/master/effective-java/item2.md) 를 참고하며 빌더 패턴을 공부했다. 사실 이때 이 개발자 분의 리뷰가 없었다면 빌더 패턴을 실무에서 자바의 기본기능만을 이용해 구현할 일이 없었을 것 같다. 아마도 [@Builder](https://projectlombok.org/features/Builder) 라는 롬복의 빌더 라이브러리만 사용했을 것 같다. 이 때 사용했던 라이브러리에는 롬복을 추가하면 하위 의존성 라이브러리에 의존성을 충돌을 일으키기에 최대한 JAVA SE의 기능만 활용해서 기능을 구현해야 했었다.<br>

현재 서비스 계열 애플리케이션들은 빌더 패턴을 대부분 롬복의 @Builder 패턴으로 사용하는 경우가 많다. 하지만, 언젠가는 공통단 코드 구현을 하게 되는 날이 오고, 외부 라이브러리 없이 보통 자바의 기본 기능만을 활용해야 할 때가 많다. 이런 경우는 빌더 패턴을 직접 손수 구현해서 사용해야 한다. 그래서 정리를 차분하게 처음부터 다시 정리 하기로 마음먹었다. 쉬는 날마다 예제 하나씩 코딩하고 다음 쉬는 날에는 요약하고 이렇게 하게 될것 같다.<br>

<br>

**빌더패턴**<br>

- 생성자에 인자를 받아서 객체를 생성과 동시에 초기화 하는 것은 멀티 스레딩환경에서 객체를 안정적으로 초기화하기 위해서이다. 하지만 생성자에 인자가 매우 많을 때가 있는데 이것을 모두 기억하면서 초기화하는 코드를 사용하는 것은 고통이다.

- 이렇게 생성자에 인자가 많을 수밖에 빌더패턴을 사용하게 된다.
- 빌더 패턴에서 객체내에 인자들을 초기화 할때 어떤 방식으로 불변성을 확보하는지 예제 코드들을 통해 확인해볼 예정이다.

<br>

## 목차

- [참고자료](#참고자료)<br>
- [아쉬운 점](#아쉬운-점)<br>

<br>

## 참고자료

- [Adopting Builder Pattern with Abstract class](https://cindystlai.wordpress.com/2017/04/20/adopting-builder-pattern-with-abstract-class/)<br>
  - Enum 을 조합하는 것이 아닌 Dto 를 빌드하는 빌더패턴을 구현할 것이기 때문에 이 자료가 가장 도움이 되었다.<br>
- [백기선님의 스터디 자료](https://github.com/keesun/study/blob/master/effective-java/item2.md)를 보면서 아이디어를 얻었고 구글에서 자료 검색 후 [Adopting Builder Pattern with Abstract class](https://cindystlai.wordpress.com/2017/04/20/adopting-builder-pattern-with-abstract-class/)의 자료가 더 내 예제에 잘 맞는 케이스였던것 같다.<br>
- [altongmon.tistory.com - T extends 타입, 와일드카드 타입, 제너릭 타입 상속](https://altongmon.tistory.com/241)<br>

- [HttpStatus 상태코드 (SpringFramework)](https://github.com/spring-projects/spring-framework/blob/main/spring-web/src/main/java/org/springframework/http/HttpStatus.java) <br>
  - enum 기반의 HttpStatus 상태코드를 참고해서, 예제에 필요한 부분들만을 추려내서 구현<br>
  - 참고 : [github/spring-projects/spring-framework/.../HttpStatus.java](https://github.com/spring-projects/spring-framework/blob/main/spring-web/src/main/java/org/springframework/http/HttpStatus.java)<br>

<br>

## etc

> 예제를 만들 때는 실무로 사용했던 예제를 그대로 사용할 수 없어서 예제를 떠올려서 만들었었는데, 예제를 생각할 때 RequestDto 를 표현하려고 했었지만, RequestDto는 특정 API 제공업체의 API 명세에 제한적으로 코드가 변해서 나중에 ResponseDto 를 예제로 표현했었던 것 같다. 그래서 그냥 ResponseDto를 만드는 예제를 그대로 정리해보려 한다. 회사마다 다르겠지만, 실제 업무에서도 회사에서 자체 정의한 ResponseDto 나 Exception 을 사용하는 경우가 많기에 유용하지 않을까 싶다.

<br>

## 핵심 요약

아주 간단한 빌더 패턴의 형식을 표현해보면 아래와 같다. 빌더 패턴을 구현하는 것은 정해져있는 것은 아니지만, 대부분의 예제에서는 아래와 같은 형식을 취하지 않을까 싶다. 주로 Inner Class 의 이름은 Builder 로 지정하고, 외부 클래스의 생성자에는 Builder 타입 객체를 받아서 초기화를 수행한다.<br>

```java
class ProductDto{
	private String grantType;
	// ...

	// private 생성자
	private ProductDto(Builder builder){
		// ...
		this.grantType = grantType;
	}

	// Builder 클래스, Inner Class 이다.
	public static class Builder{
		private String grantType;

		public Builder(){}

		// 여기를 참고 !!! this 를 리턴한다. 즉 Builder 인스턴스를 리턴하면서 체이닝이 가능하도록 한다.
		public Builder grantType(String grantType){
			this.grantType = grantType;
			return this;
		}

		public ProductDto build(){
			return new ProductDto(this);
		}

	}

}
```

<br>

- 바깥 클래스인 `ProductDto` 클래스의 private 생성자는 이너클래스인 `Builder` 의 Builder 인스턴스를 받는 생성자를 가지고 있다.<br>
- 그리고 `ProductDto` 클래스의 private 생성자 에서는  전달받은 `Builder` 타입 인스턴스로 자기 자신의 필드들을 세팅하고 있는다.<br>
  - ex) this.grantType = builder.grantType; this.clientId = builder.clientId;<br>
- 그리고 `Builder` 클래스 내에는 바깥 클래스의 필드들을 똑같이 가지고 있는다.<br>
  - private String grantType;<br>
  - private String clientId;<br>
  - private String clientSecret;<br>
- Builder 클래스 자신의 setter 와 같은 역할을 하는 아래와 같은 메서드들이 있다. 그리고 체이닝이 가능하도록 자기자신의 인스턴스인 this를 return 한다. <br>
- `build()` <br>
  - 바깥클래스의 생성자에 내부클래스 Builder 인스턴스를 넘겨주어 바깥클래스의 인스턴스를 생성한다.<br>
  - 바깥클래스의 생성자는 private 이므로, 외부에서 접근하기 어렵다.<br>

<br>

## Builder 패턴을 사용하는 이유

보통 객체를 생성할 때 멤버 필드들을 초기화할 때 생성과 동시에 초기화하는 경우가 많다. 그리고 멤버 필드는 final로 선언한다. 즉, 생성자 내에서 객체의 초기화를 수행한다. 이렇게 하는 이유는 멀티스레드 환경에서 객체의 불변성을 확보하기 위해서이다. 예를 들면 아래와 같이 생성자를 쓰는 경우가 많다.<br>

```java
class UserProfileDto{
  final String nickName;
  final String city;
  public UserProfileDto(final String nickName, final String city){
    this.nickName = nickName;
    this.city = city;
  }
}
```

<br>

위의 경우는 필드가 nickName 과 city 두개 밖에 없지만, 보통 필드들이 30개 이상을 넘어가는 경우도 굉장히 많을 것이다. 이 때 생성자에 일일이 30개 이상의 final 필드들에 대해 생성자로 나열할 경우 생성자 코드를 작성하는 것도 중노동이고, 해당 생성자의 인자를 하나 하나 맞추는 작업도 중노동일 것 같다.<br>

```java
class UserProfileDto{
  final String nickName;
  final String city;
  final String val1;
  final String val2;
  final String val3;
  final String val4;
  final String val5;
  final String val6;
  final String val7;
  
  public UserProfileDto(String nickName, String city, String val1, String val2, ... ){
    this.nickName = nickName;
    this.city = city;
    this.val1 = val1;
    // ...
  }
}
```

<br>

이렇게 인자가 많은 생성자를 만들 경우에 대해, Java 진영에서는 Builder 패턴의 사용을 권장하고 있다. Effective Java 에서 언급되는 내용인데, 자세한 내용은 [백기선님의 스터디 자료](https://github.com/keesun/study/blob/master/effective-java/item2.md) 을 참고하는 것이 나을 것 같다.<br>

<br>

내 생각은 빌더 패턴을 인자가 많은 생성자 대신에 사용하는 이유는 아래의 두 가지가 주요한 이유인 것 같다.<br>

- 생성자의 인자가 많을 경우, 구현상의 편의성과 높은 유지보수성을 제공하기 위해 필요<br>
- 객체 생성 직후의 필드의 불변을 보장해야 하는데, 생성자의 인자가 매우 많을 경우, 빌더 패턴으로 구현해 구현상의 편리함과 높은 유지보수성을 제공하기 위해 필요<br>

<br>

예를 들면 위의 UserProfileDto 클래스는 아래와 같이 변경가능하다.<br>

```java
package io.study.design_pattern.builder.messenger.response;

public class UserProfileDto {
	private String nickName;	// 닉네임
	private String city;		// 주거지역

	public UserProfileDto(){}

	public static class Builder{
		private final UserProfileDto actualInstance = new UserProfileDto();

		public Builder(){}

		public Builder self(){
			return this;
		}

		public UserProfileDto build(){
			return actualInstance;
		}

		public Builder nickName(String nickName){
			actualInstance.setNickName(nickName);
			return self();
		}

		public Builder city(String city){
			actualInstance.setCity(city);
			return self();
		}
	}

	private void setNickName(String nickName) {
		this.nickName = nickName;
	}

	private void setCity(String city) {
		this.city = city;
	}

	public String getNickName() {
		return nickName;
	}

	public String getCity() {
		return city;
	}

	@Override
	public String toString() {
		return "UserProfileDto{" +
			"nickName='" + nickName + '\'' +
			", city='" + city + '\'' +
			'}';
	}
}
```

<br>

## 예제 1. 간단한 버전의 Builder - UserProfileDto

UserProfileDto 라는 이름의 클래스를 Builder 패턴을 통해 구현한 것을 확인해보자.<br>

```java
package io.study.design_pattern.builder.messenger.response;

public class UserProfileDto {
	private String nickName;	// 닉네임
	private String city;		// 주거지역

	public UserProfileDto(){}

	public static class Builder{
		private final UserProfileDto actualInstance = new UserProfileDto();

		public Builder(){}

		public Builder self(){
			return this;
		}

		public UserProfileDto build(){
			return actualInstance;
		}

		public Builder nickName(String nickName){
			actualInstance.setNickName(nickName);
			return self();
		}

		public Builder city(String city){
			actualInstance.setCity(city);
			return self();
		}
	}

	private void setNickName(String nickName) {
		this.nickName = nickName;
	}

	private void setCity(String city) {
		this.city = city;
	}

	public String getNickName() {
		return nickName;
	}

	public String getCity() {
		return city;
	}

	@Override
	public String toString() {
		return "UserProfileDto{" +
			"nickName='" + nickName + '\'' +
			", city='" + city + '\'' +
			'}';
	}
}

```

<br>

코드를 자세히 보면 아래와 같은 몇가지 주요 특징들이 눈에 띈다.<br>

- UserProfileDto 클래스 내의 setter 들이 모두 private 로 선언되어 있다.<br>
- UserProfileDto 객체는 내부클래스인 Builder 클래스 내에 final로 인스턴스화 하여 선언되어 있다.<br>

<br>

**setter 메서드들을 private 로 선언**<br>

`UserProfileDto` 클래스 내의 `nickName` , `city` 필드는 private로 선언되어 있고, setter 메서드들 역시도 모두 private 로 선언되어 있기 때문에, 런타임 시점에 외부에서 setter 를 호출하거나 개별 필드인 `nickName`, `city` 를 수정하는 것이 불가능하다. (물론 리플렉션으로 필드들을 강제로 세팅하는 것도 가능하기는 하지만, 리플렉션은 논외로 하기로 했다.)<br>

<br>

**내부 클래스 Builder 클래스 내에 final로 UserProfileDto 객체를 인스턴스화 하여 선언**<br>

자세히 보면 Bill Pugh의 싱글턴과 조금 닮아있음을 확인가능하다. 하지만 Bill Pugh의 싱글턴과 다른 점은 Inner Class 인 Builder 클래스가 public static 으로 선언되어 있다는 것이다. (Bill Pugh 싱글턴에서의 Inner Class의 경우 Inner Class 가 private 로 선언되어 있다는 점이 다르다.) <br>

Builder 클래스 내에서 UserProfileDto를 final로 생성해두어, 불변성을 확보했고, Inner Class 인 Builder 내에 UserProfileDto 인스턴스를 선언해두어 외부에서 접근하지 못하도록 해두었다.

```java
public class UserProfileDto {
	private String nickName;	// 닉네임
	private String city;		// 주거지역

	public UserProfileDto(){}
  
	public static class Builder{
		private final UserProfileDto actualInstance = new UserProfileDto();
    // ...
    public UserProfileDto build() {
      return actualInstance;
    }
  }
}
```

<br>

**테스트 코드**<br>

```java
public class BuilderPatternResponseTest {

	@Test
	void 단순_빌더패턴_UserProfileDto_테스트(){
		UserProfileDto user1 = new UserProfileDto.Builder()
			.nickName("신사임당")
			.city("부천")
			.build();

		UserProfileDto user2 = new UserProfileDto.Builder()
			.nickName("존리선생님")
			.city("뉴요크")
			.build();

		Assertions.assertNotSame(user1, user2);
	}
  
  // ...
  
}
```

<br>

**출력결과**<br>

```plain
user1 = UserProfileDto{nickName='신사임당', city='부천'}
user2 = UserProfileDto{nickName='존리선생님', city='뉴요크'}
```

<br>

## 예제 2. 조금 복잡한 버전의 Builder

> 참고자료 : [Adopting Builder Pattern with Abstract class](https://cindystlai.wordpress.com/2017/04/20/adopting-builder-pattern-with-abstract-class/) <br>

<br>

이번에는 조금 더 복잡하게 꼬아서 Dto를 만들어보려고 한다. 만들어보려고 하는 클래스는  ResponseDto, KakaoTalkResponseDto 이다. ResponseDto 는 약간은 공통적인 규격의 성격을 가지는 응답 객체이다. KakaoTalkResponseDto 는 ResponseDto 의 기능을 확장(extends) 한 자식 클래스이다.<br>

<br>

**ResponseDto.java**<br>

공통적인 성격을 가지기 위해 ResponseDto 클래스를 abstract 로 선언했다.<br>

```java
public abstract class ResponseDto {
	private HttpStatus status;

	private int code;

	private String message;

	protected abstract static class Builder<O extends ResponseDto, T extends Builder>{
		protected O actualInstance;
		protected T actualClassBuilder;

    // 이 부분을 주목하자.
		protected abstract O createActualInstance();
		protected abstract T getActualBuilder();

		protected Builder(){}

		public T code(int code){
			actualInstance.setCode(code);
			return self();
		}

		public T message(String message){
			actualInstance.setMessage(message);
			return self();
		}

		public abstract ResponseDto build();
		protected abstract T self();
	}

	protected void setCode(int code){
		this.code = code;
	}

	protected void setMessage(String message){
		this.message = message;
	}

	public HttpStatus getStatus(){
		return HttpStatus.valueOf(this.code);
	}

	public String getMessage(){
		return this.message;
	}

	public boolean isResponseSuccessful(){
		return this.status.is2xxSuccessful();
	}
}
```

<br>

오늘은 시간이 많지 않아서 다음 날 정리해놓을 내용들만을 리스트 업 해놓을 예정이다.<br>

- **abstract 메서드**<br>
  - 각 자식 클래스에서 반드시 구현해야 하는 메서드들의 목록은 아래와 같다.<br>
  - createActualInstance() : O extends ResponseDto<br>
    - 자식 클래스 자신의 인스턴스를 생성하는 역할을 한다.
  - getActualBuilder() : T extends Builder<br>
    - 자식 클래스가 가진 Builder 를 리턴한다.<br>
    - 이번 예제에서는 사용하지 않는다. 필요할 것이라고 생각해서 만들어놓긴 했지만 사용하지 않게 되었다.<br>
  - build() : ResponseDto<br>
  - self() : T 하위타입인 T를 리턴한다. 메서드 체이닝을 구현하기 위해 선언된 추상 메서드이다.<br>

- **setter**<br>
  - 빌더를 사용하는 것은 생성자에 인자가 많을 때 사용성을 조금 더 좋게 만들기 위해 사용하는 것이다.<br>
  - 객체 생성시 생성자를 통해 초기화하고, 멤버 필드를 final 로 선언해서, setter 의 사용을 가급적 배제하는 편이다. 하지만 빌더 패턴을 사용하게 되면서 setter를 사용할 수 밖에 없게 되었다.<br>
  - 여기에 대한 방지책으로 setter 를 protected 로 선언해두었고, 외부에서 setter를 접근하지 못하고 오로지 자식 클래스에서만 접근이 가능하도록 해두었다.<br>

<br>

**KakaoTalkResponseDto**<br>

```java
package io.study.design_pattern.builder.messenger.response;

import java.time.LocalDateTime;
import java.util.Optional;

public class KakaoTalkResponseDto extends ResponseDto{

	private final UserProfileDto userProfileDto;

	private LocalDateTime receivedTime;

	public static class Builder extends ResponseDto.Builder<KakaoTalkResponseDto, Builder>{

		private final UserProfileDto userProfileDto;

		public Builder(UserProfileDto userProfileDto){
			// 참고) 혼동하기 쉬운 사항
			// 	묵시적으로 super(); 가 호출된다. super() 는 ResponseBuilder 의 Builder() 이다.
			// 	즉, 상위 클래스의 생성자가 호출된다.
			// 	기본 중의 기본인 내용이지만, 사람의 의식흐름으로는 가끔 당연하다는 듯이 혼동하는 경우가 많다.
			// super();
      
			this.userProfileDto = Optional.of(userProfileDto).orElseGet(UserProfileDto::new);
			actualInstance = createActualInstance();
			actualClassBuilder = this;
		}

		public Builder receviedTime(LocalDateTime receivedTime){
			actualInstance.setReceivedTime(receivedTime);
			return self();
		}

		@Override
		public ResponseDto build() {
			return actualInstance;
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		protected KakaoTalkResponseDto createActualInstance() {
			return new KakaoTalkResponseDto(this.userProfileDto);
		}

		@Override
		protected Builder getActualBuilder() {
			return this;
		}
	}

	private KakaoTalkResponseDto(UserProfileDto userProfileDto){
		this.userProfileDto = userProfileDto;
	}

	private void setReceivedTime(LocalDateTime receivedTime){
		this.receivedTime = receivedTime;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("KakaoTalkResponseDto { \n")
			.append("\tuserProfileDto = " + userProfileDto)
			.append(", \n")
			.append("\treceivedTime = " + receivedTime)
			.append(", \n")
			.append("\tstatus = " + getStatus())
			.append(", \n")
			.append("\tmessage = " + getMessage())
			.append("\n")
			.append("} \n");

		return buffer.toString();
	}
}
```

<br>

**abstract 메서드 오버라이딩** <br>

- ResponseDto 내의 메서드를 오버라이딩하고 있는데, 그 중 오버라이딩한 메서드들은 아래와 같다.<br>

- createActualInstance() : KakaoTalkResponseDto<br>
- getActualBuilder() : Builder<br>
- build() : ResponseDto<br>
- self() : T 하위타입인 T를 리턴한다. 메서드 체이닝을 구현하기 위해 선언된 추상 메서드이다.<br>

<br>



