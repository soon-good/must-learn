# ComponentScan (컴포넌트 스캔)

실무에서 애플리케이션을 개발하다 보면 플랫폼 관련 부서에서 컴포넌트 관련 설정을 모두 제공해준다. 하지만, 테스트 코드 작성시, 서비스마다 각각의 독립된 환경설정을 해야 하는 경우가 많다. 레거시 환경이라면 더더욱 인수인계자체를 받지 않은 상태로 테스트 환경설정을 해야만 하는 상황이 많다.<br>

이때 기본적인 컴포넌트 스캔 원칙들을 알고 있다면 조금이나마 도움이 되지 않을까 싶기도 하고, 뭔가 간결하게 요약해서 정리해보고 싶다는 생각이 들어 타이핑을 시작했다.<br>

<br>

## 목차

- [참고자료](#참고자료)<br>
- [Bean 객체 명 네이밍 방식](#bean-객체-명-네이밍-방식)<br>
- [컴포넌트 탐색 위치 지정](#컴포넌트-탐색-위치-지정)<br>
- [예) 스프링 부트의 ComponentScan](#예-스프링-부트의-ComponentScan)<br>
- [권장방식](#권장방식)<br>
- [컴포넌트 스캔 기본 대상](#컴포넌트-스캔-기본-대상)<br>
- [stereotype 내의 주요 애노테이션들 기능 요약](#stereotype-내의-주요-애노테이션들-기능-요약)<br>
- [중복등록과 충돌](#중복등록과-충돌)<br>
  - [자동 Bean 등록 vs 자동 Bean 등록](#자동-Bean-등록-vs-자동-Bean-등록)<br>
  - [수동 Bean 등록 vs 자동 Bean 등록](#수동-Bean-등록-vs-자동-bean-등록)<br>
- [@Filter](#@Filter)<br>
- [FilterType](#FilterType)<br>
  - [예제) BeanA 클래스를 컴포넌트 스캔 대상에서 제외시키는 예제](#예제-beana-클래스를-컴포넌트-스캔-대상에서-제외시키는-예제)<br>

<br>

## 참고자료

<br>

## Bean 객체 명 네이밍 방식

@ComponentScan 은 @Component 가 붙은 모든 클래스를 스프링 빈으로 등록한다. <br>

스프링 내부 실행 컨텍스트 내에 등록되는 스프링의 Bean의 이름은 직접 지정하지 않으면 스프링에서 정한 기본 네이밍 규칙을 따른다. 이렇게 스프링 Bean으로 객체가 생성되어 실행 컨텍스트 내에 등록되는 Bean 이름의 규칙은 아래와 같다.<br>

- 클래스 명을 그대로 사용하되 카멜 케이스 형식으로 하고, 맨 앞글자를 소문자로 한다.<br>

<br>

예를 들면 아래와 같은 방식이다.<br>

- EmployeeServiceImpl 을 @Component 애노테이션을 통해 빈으로 등록할 경우<br>
  - `employeeServiceImpl` 라는 이름으로 스프링의 실행컨텍스트 내에 빈으로 등록된다.<br>

<br>

Bean(빈)의 이름을 직접 지정하고 싶을 경우 역시 있을 수 있다.<br>

- 이 경우, `@Component("employeeServiceCustom")` 과 같이 직접 지정할 수 있다.<br>

<br>

## 컴포넌트 탐색 위치 지정

컴포넌트 탐색위치는 아래와 같은 방식으로 지정할 수 있다.<br>

```java
@Configuration
@ComponentScan(
  	basePackages = “hello.core.member”,
    excludeFilters = @ComponentScan.Filter(
      type = FilterType.ANNOTATION, classes = Configuration.class
    )
)
public class DefaultScanningConfig {
}
```

<br>

**참고) 컴포넌트 스캔시 스캐닝 위치, 기본 스캔 대상 지정**<br>

- basePackages<br>
  - 탐색할 패키지의 시작 위치를 지정한다.<br>
  - 이 패키지를 포함해서 하위 패키지를 모두 탐색하겠다는 의미.<br>
- basePackageClasses<br>
  - 지정할 클래스의 패키지를 탐색 시작위치로 지정한다.<br>

만약 위의 baskePackages, basePackageClasses 에 대해 경로를 따로 지정하지 않으면, 기본적인 원칙으로 아래의 원칙을 따른다. <br>

- @ComponentScan 이 붙은 설정클래스가 위치하고 있는 패키지가 스캔 시작위치가 된다.<br>

<br>

## 예) 스프링 부트의 ComponentScan

스프링 부트를 예로 들면, @SpringBootApplication 애노테이션은 @ComponentScan 애노테이션을 포함하고 있는 마커애노테이션이다.<br>

**@SpringBootApplication 애노테이션 내부**<br>

아래의 코드를 직접 살펴보면 @ComponentScan 이 포함되어 있는 것을 볼 수 있다.<br>

```java
package org.springframework.boot.autoconfigure;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
    excludeFilters = {
      @Filter(
    		type = FilterType.CUSTOM,
    		classes = {TypeExcludeFilter.class}
			), 
      @Filter(
        type = FilterType.CUSTOM,
        classes = {AutoConfigurationExcludeFilter.class}
      )
    }
)
public @interface SpringBootApplication {
  // ... 
}

```

<br>

아래의 그림은 spring-basic 이라는 이름으로 스프링 부트 애플리케이션 프로젝트를 생성한 후 프로젝트를 에디터에서 열어본 화면이다.<br>

![이미지](./img/COMPONENT-SCAN-1.png)

프로젝트를 생성하고 난 후에 @SpringBootApplication 애노테이션이 적용된 애플리케이션 코드인 인  `SpringBasicApplication.java` 클래스가 프로젝트 패키지 최상단인 `io.study.springbasic` 아래에 위치해 있는 것을 확인가능하다.<br>

<br>

## 권장방식

스프링에서 권장되는 방식 역시 스프링 부트의 관례처럼 **컴포넌트를 스캔하는 클래스를 프로젝트 최상단에 두는 것**이다. <br>basePackage를 클래스 내에 따로 하드 코딩으로 텍스트로 지정하여 엉뚱한 위치에 놓게 되면 유지보수, 관리에 많은 시간이 들고, 괜한 반항심으로만 보일수도 있는 것 역시 있는 것 같다. 정말 필요한 경우가 아니라면, 기본적인 설정을 따르는 것이 맞는 것 같다.<br>

스프링 부트가 아닌 스프링 애플리케이션을 직접 처음부터 설정할 경우 **컴포넌트 스캔** 역할을 하는 클래스를 아래의 예처럼 두는 것이 필요하다.<br>

- com.hello<br>
  - DefaultScanningConfig.java<br>
  - com.hello.config<br>
    - Config1.java<br>
    - Config2.java<br>
    - ... <br>
  - ... <br>
  - com.hello.employee<br>
    - service<br>
    - repository<br>

<br>

## 컴포넌트 스캔 기본 대상

컴포넌트 스캔은 @Component 외에도 아래의 애노테이션들도 컴포넌트로 인식해 스캔한다.<br>

- @Component <br>
- @Controller, @Service, @Repository <br>
- @Configuration <br>

<br>

위의 애노테이션들의 내부를 직접 확인해보면 모두 @Component 를 포함하고 있는 것을 확인할 수 있다.<br>

<br>

## stereotype 내의 주요 애노테이션들 기능 요약

- @Controller<br>
  - 스프링 mvc 컨트롤러로 인식<br>
- @Repository<br>
  - 스프링 데이터 접근 계층<br>
  - 데이터 계층의 예외를 스프링 예외로 변환해준다<br>
- @Configuration<br>
  - 스프링 설정 정보로 인식한다.<br>
  - 스프링 빈이 싱글톤을 유지하도록 추가 처리를 한다.<br>
- @Service<br>
  - 사실 @Service는 특별한 처리를 하지는 않는다.<br>
  - 개발자들이 핵심 비즈니스 로직이 여기에 있겠구나 하고 비즈니스 계층을 인식하는데에 도움이 된다.<br>

<br>

## 중복등록과 충돌

컴포넌트 스캔 중복 등록이 아래의 두가지 경우와 같이 등록되는 경우에 대해서 정리할 예정이다.

- 자동 Bean 등록 vs 자동 Bean 등록<br>
- 수동  Bean 등록 vs 자동 Bean 등록<br>

<br>

### 자동 Bean 등록 vs 자동 Bean 등록

컴포넌트 스캔에 의해 자동으로 등록되는 스프링 Bean 들은 이름이 같을 때 `ConflictBeanDefinitionException` 예외를 발생시킨다.<br>

<br>

### 수동 Bean 등록 vs 자동 Bean 등록

```java
@Configuration
@ComponentScan(
    excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {

    @Bean(name = “memoryMemberRepository”)
    MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
```

이 경우 **수동 등록 Bean 이 자동으로 지정된  Bean을 오버라이딩**하게 된다.<br>

스프링 부트의 경우 수동 Bean 등록과 자동 Bean 등록이 충돌이 나면, 오류가 발생하도록 기본 설정을 바꾸었다.<br>

```plain
Consider renaming one of the beans or enabling overriding by setting
spring.main.allow-bean-definition-overriding=true
```

<br>

## @Filter

**참고) useDefaultFilters 옵션**<br>

> useDefaultFilters 옵션은 기본으로 켜져 잇는데 이 옵션을 끄면 기본 스캔 대상들이 제외된다. 그냥 이런 옵션이 있구나 정도로 알고 넘어가자. <br>

<br>

**@MyIncludeComponent**<br>

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeComponent {
}
```

<br>

**@MyExcludeComponent**<br>

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyExcludeComponent {
}
```

<br>

**BeanA.java**<br>

```java
@MyIncludeComponent
public class BeanA{
}
```

<br>

**BeanB.java**<br>

```java
@MyExcludeComponent
public class BeanB{
}
```

<br>

**ComponentFilterAppConfigTest.java**<br>

```java
public class ComponentFilterAppConfigTest {
  // ...
  @Test
  void filterScan() {
    ApplicationContext ac = new AnnotationConfigApplicationContext (ComponentFilterAppConfig.class);
    BeanA beanA = ac.getBean(“beanA”, BeanA.class);
    assertThat(beanA).isNotNull();

    // ac.getBean(“beanB”, BeanB.class); // exclude 했으므로… 에러

    Assertions.assertThrows(
      NoSuchBeanDefinitionException.class, 
      () -> ac.getBean(“beanB”, BeanB.class)
    );
  }

  @Configuration
  @ComponentScan(
    includeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
    excludeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
  )
  static class ComponetFilterAppConfig {

  }
 
}
```

<br>

## FilterType

**ANNOTATION**<br>

- 기본 값이다. type으로 아무 값도 지정하지 않으면 FilterType.ANNOTATION 으로 지정된다.<br>
- ex) org.example.SomeAnnotation<br>

**ASSIGNABLE_TYPE**<br>

- 지정한 타입과 자식 타입을 인식해서 동작한다.<br>
- ex) org.example.SomeClass<br>

**ASPECTJ**<br>

- AspectJ 패턴 사용<br>
- ex) `org.example..*Service*`<br>

**REGEX**<br>

- 정규표현식<br>
- ex) `org.example.Default.*`<br>

**CUSTOM**<br>

- TypeFilter 라는 인터페이스를 구현해서 처리한다.<br>
- ex) `org.example.MyTypeFilter` <br>

<br>

### 예제) BeanA 클래스를 컴포넌트 스캔 대상에서 제외시키는 예제

```java
public class ComponentFilterAppConfigTest {

    @Test
    void filterScan() {
        ApplicationContext ac = new AnnotationConfigApplicationContext (ComponentFilterAppConfig.class);
        BeanA beanA = ac.getBean(“beanA”, BeanA.class);
        assertThat(beanA).isNotNull();

        // ac.getBean(“beanB”, BeanB.class); // exclude 했으므로… 에러

        Assertions.assertThrows(
            NoSuchBeanDefinitionException.class, 
            () -> ac.getBean(“beanB”, BeanB.class)
        );
    }

    @Configuration
    @ComponentScan(
        includeFilters = @Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
        excludeFilters = {
            @Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class),
            @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = BeanA.class)
        }
    )
    static class ComponetFilterAppConfig {

    }
 
}
```



## 예제

