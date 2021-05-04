# Spring Bean 생명주기와 콜백

`객체 생성` 은 객체를 생성해 해당 데이터를 메모리에 저장하는 역할을 한다. `초기화`는 객체가 생성된 후에 수행하는 작업을 의미하는데, 경우에 따라 무거운 작업을 수행하는 경우 역시 존재한다. 예를 들면, 외부 커넥션을 연결하는 등의 동작을 수행하는 것을 예로 들 수 있다.<br>

이런 이유로 객체가 생성되는 시점과 생성이 완료된 후의 시점을 구분해서 초기화 로직을 수행하고자 할 때가 있다. 오늘 정리할 내용은 여기에 관련된 내용이다.<br>

가급적 요약 형태로 깔끔하게 정리해볼 예정이다.<br>



## 목차

- [참고자료](#참고자료)<br>
- [스프링의 Bean 생명주기,콜백](#스프링의-Bean-생명주기-콜백)<br>
- [InitializingBean, DisposableBean 사용방식](#initializingbean-disposablebean-사용방식)<br>
  - [단점](#단점)<br>
  - [@Bean(initMethod, destroyMethod) 사용방식](#@bean-initmethod-destroymethod-사용방식)<br>
- [@PostConstruct, @PreDestroy](#postconstruct-predestroy)<br>
  - [PostConstruct](#postconstruct)<br>
  - [PreDestroy](#predestroy)<br>
  - [특징](#특징)<br>
  - [예제](#예제)<br>
- [결론](#결론)<br>

<br>

## 참고자료

- 참고자료 정리 





## 스프링의 Bean 생명주기,콜백

스프링은 크게 3가지 방식으로 빈 생명주기 (Bean Lifecycle) 및 콜백을 제공한다.<br>

- InitializingBean, DisposableBean (인터페이스) 사용방식<br>
- @Bean(initMethod, destroyMethod) 사용방식<br>
- @PostConstruct, @PreDestroy <br>

<br>

## InitializingBean, DisposableBean 사용방식

InitializingBean, DisposableBean 을 implements 해서 스프링의 내부 실행 컨텍스트가 가로챌 수 있도록 해주는 방식이다. 예제는 아래와 같다. (InitializingBean, DisposableBean 을 사용하는 초기화/종료 방식들은 스프링 초창기에 나온 방식들이다. 현재는 더 나은 방식들이 나와 있는데, 이것에 대해서는 추후 정리 예정)

```java
public class Helloworld implements InitializingBean, DisposableBean {
    public Helloworld () {
        // …..
    }

    // InitializingBean의 메서드
    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        // 그냥 이것 저것 초기화 작업들
    }


    // DisposableBean
    @Override
    public void destroy(){
        // 해제 작업들
        disconnect();
    }
}
```

<br>

### 단점

스프링전용 인터페이스이다. 스프링 프레임워크에 의존적인 코드가 된다. <br>

- 해당 기능이 스프링에서 Deprecated 되는 경우 단점이 될 수도 있을 것 같다.<br>

초기화, 소멸 메서드의 이름을 변경할 수 없다.<br>

- 기존 코드 중에 afterPropertiesSet(), destroy() 메서드가 있었다면 수정해야만 하게 된다.<br>

내가 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없다.<br>

<br>

## @Bean(initMethod, destroyMethod) 사용방식

설정 정보에 @Bean(initMethod = "init", destroyMethod = "close") 처럼 초기화, 소멸 메서드를 지정하는 경우이다.<br>

**destroyMethod**<br>

아래와 같이 @Bean 애노테이션 내에는 destroyMethod 라는 속성이 있다.<br>

```java
package org.springframework.context.annotation;

// ...

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {
    @AliasFor("name")
    String[] value() default {};

    @AliasFor("value")
    String[] name() default {};

    Autowire autowire() default Autowire.NO;

    String initMethod() default "";

    String destroyMethod() default "(inferred)";
}
```

<br>

destroyMethod() 는 위에서 보듯이 기본값이 "(inferred)" 로 등록되어 있다. 이 **추론** 기능은 close, shutdown 이라는 이름의 메서드를 자동으로 호출해준다. 이름 그대로 종료 메서드를 **추론** 해서 호출해준다. 따라서 직접 스프링 빈으로 등록하면, 종료메서드는 따로 적어주지 않아도 잘 동작한다.<br>

추론 기능을 사용하지 않도록 하려면 destroyMethod = "" 으로 지정해 Bean을 등록하면 된다.<br>

**예제)** <br>

**Helloword.java**<br>

```java
public class Helloworld {
    public Helloworld () {
        // ... 
    }

    public void init () throws Exception {
        System.out.println(“블라블라");
    }

    public void destroy() throws Exception {
        System.out.println(“블라블라");
    }
}
```

<br>

**Configuration 파일 내에서 Bean 객체로 Helloworld 객체를 생성하기**<br>

아래의 예에서 Helloworld 타입의 객체를 생성하고 있는 `helloWorld()` 메서드에 선언된 @Bean 애노테이션에서는 initMethod, destroyMethod 속성을 지정하고 있다. 그리고 각 속성에는 Helloword 내의  init 메서드, destroy 메서드 명을 지정하고 있다.<br>

```java
public class SomeTest {
    @Configuration
    static class LifeCycleConfig {
        @Bean(initMethod = “init”, destroyMethod = “destroy”)
        public Helloworld helloWorld(){
            Helloworld h = new Helloworld();
            ….
            return h;

        }
    }
}
```

메서드 이름을 텍스트로 하드 코딩해 생성/초기화시의 동작을 지정하는 것이 가능한 것은 아마도 Spring 내부의 리플렉션 코드 때문이지 않을까 싶다. 사용자 정의 애노테이션을 직접 만들어봤다면 이해가 수월할 듯 싶다. 사용자 정의 애노테이션을 선언했을때 보통 그 애노테이션을 실행시키는 코드 역시 작성해야 하는데, 이렇게 애노테이션을 실행시키는 코드는 스프링 내부에서 정의해두었고 여기에 대한 약속으로 initMethod 에는 초기화 메서드를, destroyMethod 에는 소멸자 메서드를 지정하도록 하고 있다. (어쩌면 C++, MFC에서는 소멸자가 있다는 것이 어쩌면 더 편한 요소일것 같기도 하다.)<br>

<br>

## @PostConstruct, @PreDestroy

PostConstruct, PreDestroy 는 JSR-250 이라는 자바 표준을 따르는 애노테이션이다. 즉, 스프링이 아닌 다른 컨테이너에서 이 애노테이션에 대한 처리를 지원한다면 별다른 구현 없이 객체 생성/소멸 로직을 PostConstruct, PreDestroy 애노테이션에 명시해두는 것으로 마무리지을 수 있다.<br>

- PostConstruct : javax.annotation.PostConstruct<br>
- PreDestroy : javax.annotation.PreDestroy<br>

<br>

일반적으로 @PostConstruct, @PreDestroy 애노테이션을 사용하는 것을 권장하는 편이다.<br>

외부라이브러리 내의 특정 객체의 생성/소멸 시점을 후킹해서 해당 시점에 수행할 동작을 지정해야 한다면, @Bean(initMethod, destroyMethod) 를 사용하자. (왜냐하면, 외부라이브러리 내에 우리가 직접 @PostConstruct, @PreDestroy 를 적어줄수는 없기 때문이다.)<br>

<br>

각 애노테이션의 내용은 아래와 같다.<br>

### PostConstruct

```java
package javax.annotation;

// ...

@Documented
@Retention (RUNTIME)
@Target(METHOD)
public @interface PostConstruct {
}
```

<br>

### PreDestroy

```java
package javax.annotation;

// ...

@Documented
@Retention (RUNTIME)
@Target(METHOD)
public @interface PreDestroy {
}
```

<br>

### 특징

- 최신 스프링에서 가장 권장하는 방식이다.<br>
- `javax.annotation` 아래에 있는 패키지로써, JSR-250 스펙을 따른다. 자바 표준을 따르는 것이기 때문에, PostConstruct, PreDestroy 를 지원하는 컨테이너를 사용한다면, 별도의 생성/소멸 시점을 후킹하는 공통 로직을 작성할 필요가 없다.<br>
- ComponentScan 과 조화를 잘 이룬다.<br>
- 외부 라이브러리 (External Library) 의 객체의 생성/소멸 시점은 후킹하지 못한다는 점이 단점이다.<br>
- 만약 외부 라이브러리의 생성/소멸 시점을 후킹해야 한다면 @Bean 의 기능을 이용하면 된다.<br>

<br>

### 예제

```java
public class Helloworld {
    public Helloworld () {
        // …..
    }

    @PostConstruct
    public void init() throws Exception {
        System.out.println(“블라블라");
    }

    @PreDestroy
    public void destroy() throws Exception {
        System.out.println(“블라블라");
    }
}
```





## 결론

일반적으로 @PostConstruct, @PreDestroy 애노테이션을 사용하는 것을 권장하는 편이다.<br>

외부라이브러리 내의 특정 객체의 생성/소멸 시점을 후킹해서 해당 시점에 수행할 동작을 지정해야 한다면, @Bean(initMethod, destroyMethod) 를 사용하자. (왜냐하면, 외부라이브러리 내에 우리가 직접 @PostConstruct, @PreDestroy 를 적어줄수는 없기 때문이다.)<br>













