# 기본제공 함수형 인터페이스들 - Function, Consumer, Predicate, Supplier

Java8 이후부터 기본으로 제공되는 함수형 인터페이스들이 있다. <br>

오늘은 아래의 4가지의 함수형 인터페이스들에 대해 각각의 테스트 코드를 이용한 예제와 간단한 메서드 시그니처를 정리할 예정이다.

- `Function<T, R>` : T -> R
- `Consumer<T>` : T -> void
- `Predicate<T>` T-> boolean
- `Supplier<T>` () -> T

함수형 인터페이스를 처음 공부할 때는 책을 처음부터 끝까지 빠짐없이 읽고 설명들을 이해하려고 했었다. 하지만, 지금 돌아와서 생각해보면 그렇게 할 필요가 없었다. 테스트 코드 기반으로 예제를 자주 보면 된다. 회화 공부하듯이 공부하면 되는 것 같다. 그런 이유로 가능한 한도 내에서 설명을 최대한 배제해서 요약할 예정이다.

<br>

## 참고자료

- 모던 자바 인 액션
- [Codechacha.com - Java 8, Supplier 예제](https://codechacha.com/ko/java8-supplier-example/)

<br>

## Function<T,R>

함수형 인터페이스 `Function <T, R>` 의 메서드 시그니처는 `T -> R` 이다.<br>

- 공식문서 [documentation](https://docs.oracle.com/javase/8/docs/api/?java/util/function/Function.html)<br>
- [예제 깃헙](링크 추가하기)

<br>

보통 Function 타입의 인스턴스를 람다식으로 정의해서 인자로 주고받거나, 변수에 할당할 때  `T->R` 형태의 시그니처로 정의한다. 이외의 함수들인 compose, andThen 은 default 키워드로 선언되어 있는데, 이것을 보통 디폴트 메서드라고 한다. 함수형 인터페이스에 사용하는 default 키워드의 의미는 추후 다른 문서에서 정리할 예정이다. 일단은 [예전에 정리했던 문서](https://github.com/gosgjung/modern-java-in-action/blob/develop/study/summary/3.4_3.8_%EA%B8%B0%EB%B3%B8%EC%A0%9C%EA%B3%B5_%ED%95%A8%EC%88%98%ED%98%95_%EC%9D%B8%ED%84%B0%ED%8E%98%EC%9D%B4%EC%8A%A4/3.8.%EB%9E%8C%EB%8B%A4%ED%91%9C%ED%98%84%EC%8B%9D%EC%9D%84_%EC%A1%B0%ED%95%A9%ED%95%A0_%EC%88%98_%EC%9E%88%EB%8A%94_%EC%9C%A0%EC%9A%A9%ED%95%9C_%EB%A9%94%EC%84%9C%EB%93%9C.md#%EB%94%94%ED%8F%B4%ED%8A%B8-%EB%A9%94%EC%84%9C%EB%93%9C)를 참고하자.<br>

### 예제 1) 단순 예제

**테스트 코드**

```java
@Test
@DisplayName("Function_T_R_테스트_1_단순테스트")
void Function_T_R_테스트_1_단순테스트(){
  Function<String, Integer> lengthFunc = (t)->{
    return t.length();
  };

  Integer length = lengthFunc.apply("hello~");
  assertThat(length).isEqualTo(6);
  System.out.println(length);
}
```

<br>

**출력결과**

```plain
6
```

<br>

### 예제 2) Function 을 메서드의 인자로 받아서 처리해보기

**테스트코드**

```java
public <T,R> List<R> mappingByFunction(List<T> input, Function<T,R> mappingFn){
  List<R> list = new ArrayList<>();
  for(T d : input){
    list.add(mappingFn.apply(d));
  }
  return list;
}

@Test
@DisplayName("Function_T_R_테스트_2_파라미터로_전달해서_사용하기")
void Function_T_R_테스트_2_파라미터로_전달해서_사용하기(){
  List<String> words = Arrays.asList("Apple", "Banana", "Carrot", "Dragon", "Eureka");

  List<Integer> integers = mappingByFunction(words, t -> {
    return t.length();
  });

  System.out.println(integers);
}
```

<br>

**출력결과**

```plain
[5, 6, 6, 6, 6]
```

<br>

## Consumer\<T\>

함수형 인터페이스 `Consumer <T>` 의 메서드 시그니처는 `T -> void` 이다.<br>

- 공식문서 [documentation](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)<br>
- [예제 깃헙](링크 추가하기)

<br>

### 예제 1) 

**테스트코드**<br>

```java
@Test
@DisplayName("Consumer_예제_1_문자열_길이_출력하기")
void Consumer_예제_1_문자열_길이_출력하기(){
  Consumer<String> printLengthLn = (t)->{
    System.out.println(t.length());
  };

  String inputString = "ABC";
  printLengthLn.accept(inputString);
}
```

<br>

**출력결과**<br>

```plain
3
```

<br>

### 예제 2) Consumer 를 메서드의 인자로 받아서 처리해보기

**테스트 코드**<br>

```java
public <T> void consumeList (List<T> list, Consumer<T> consumer){
  for(T t: list){
    consumer.accept(t);
  }
}

@Test
@DisplayName("Consumer_예제_2_파라미터로_Consumer_T_를_전달해보기")
void Consumer_예제_2_파라미터로_Consumer_T_를_전달해보기(){
  List<String> list = Arrays.asList("ABC", "DEF", "HI", "J", "");
  consumeList(list, (t)->{System.out.println(t.length());});
}
```

<br>

**출력결과**

```plain
3
3
2
1
0
```

<br>

## Predicate \<T\>

함수형 인터페이스 `Predicate <T>` 의 메서드 시그니처는 `T -> boolean` 이다.<br>

- 공식문서 [documentation](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html)<br>
- [예제 깃헙](링크 추가하기)

<br>

### 예제1) 단순동작 확인해보기

**테스트코드**

```java
@Test
@DisplayName("Predicate_테스트_1_단순동작확인해보기")
void Predicate_테스트_1_단순동작확인해보기(){
  Predicate<String> isWhiteSpace = (t) -> {
    boolean b = t.length() == 0;
    return b;
  };

  String whiteSpace = "";
  boolean whiteSpaceFlag = isWhiteSpace.test(whiteSpace);
  System.out.println("문자열 '" + whiteSpace + "' 는 공백문자인가요? " + whiteSpaceFlag);
}
```

<br>

**출력결과**<br>

```plain
문자열 '' 는 공백문자인가요? true
```

<br>

### 예제 2) Predicate 를 메서드의 인자로 전달해서 처리해보기

**테스트코드**<br>

```java
enum DeviceType {
  CPU(100, "CPU"){},
  RAM(200, "RAM"){},
  DISK(500, "DISK"){};

  private int deviceTypeCode;
  private String deviceTypeNm;

  DeviceType(int deviceTypeCode, String deviceTypeNm){
    this.deviceTypeCode = deviceTypeCode;
    this.deviceTypeNm = deviceTypeNm;
  }
}

public <T> List<T> filterDevicesByType(List<T> input, Predicate<T> predicate){
  List<T> result = new ArrayList<>();

  for (T t : input){
    if(predicate.test(filteredList = [CPU]t)){
      result.add(t);
    }
  }

  return result;
}

@Test
@DisplayName("Predicate_테스트_2_함수의_인자로_넘겨서_리스트를_필터링해보기")
void Predicate_테스트_2_함수의_인자로_넘겨서_리스트를_필터링해보기(){
  EnumSet<DeviceType> deviceTypes = EnumSet.of(DeviceType.CPU, DeviceType.DISK, DeviceType.RAM);
  List<Object> deviceList = Arrays.asList(deviceTypes.toArray());
  List<Object> filteredList = filterDevicesByType(deviceList, t -> {
    return (DeviceType.CPU.equals(t));
  });

  System.out.println("filteredList = " + filteredList);
}
```

<br>

**출력결과**<br>

```plain
filteredList = [CPU]
```



## Supplier\<T\>

함수형 인터페이스 `Supplier <T>` 의 메서드 시그니처는 `() -> T` 이다.<br>

- 공식문서 [documentation](https://docs.oracle.com/javase/8/docs/api/java/util/function/Supplier.html)<br>
- [예제 깃헙](링크 추가하기)<br>

아무인자를 받지 않지만 어떤 무언가를 직접 리턴하는 역할을 한다. 이름은 마치 생산자 라는 의미와 같아보이기도 하다.<br>

