# Optional 예제 및 요약

오늘은 조금 쉬어가볼까하는 생각으로 옵셔널 정리를 시작했다. 매번 일주일 중 평일 동안은 경주마처럼 집중해서 달려왔는데, 금요일 하루 정도는 루즈한 마음으로 조금은 개인정비하는 듯한 하루를 보내도 괜찮겠다는 생각이 들어서 조금은 쉬운 주제인 Optional 을 선택했다.<br>

오늘 정리하는 내용은 [예전에 정리했던 내용](https://github.com/gosgjung/modern-java-in-action/blob/develop/study/summary/ch11_optional/11.3-Optional-%EA%B8%B0%EB%B3%B8%EC%98%88%EC%A0%9C.md)을 최대한 개인적인 시각으로 요약하고 더 줄여본 내용이다. 아무것도 모를때 정리했던 내용들은 다소 사족이 많았다는 느낌이 든다. 테스트 코드도 일단은 커밋해두었는데, 적당한 리포지터리를 찾아서 새로 기록해둘 예정이다~!!<br>

<br>

## 참고자료

- [모던 자바 인 액션](http://www.yes24.com/Product/Goods/77125987)

<br>

## Optional 사용시 주의점 - 반환 값으로만 사용

Optional 은 반환값을 감싸기 위해서 사용하는 것이 주 목적이다. 필드형식으로 사용하지 못한다. 즉, 멤버 필드에 Optional 타입의 변수를 선언하지 못한다.<br>

멤버 필드로 nullable 한 값을 가질수도 있는  Optional 변수를 가질수 없기 때문에 이 경우 아래 코드처럼 헬퍼메서드를 작성하는 방식으로 Optional 한 변수를 만들어내는 것이 가능하다.

```java
public class Person {
  private Car car;
  public Optional<Car> getCarAsOptional(){
    return Optional.ofNullable(car);
  }
}
```

<br>

## Optional 객체 만들기

### Optional.empty()

비어있는 Optional 객체를 만드는 예이다.

```java
Optional<Employee> optEmptyEmployee = Optional.empty();
System.out.println("optEmployeeEmployee = " + optEmptyEmployee);
```

출력결과

```plain
optEmployeeEmployee = Optional.empty
```

<br>

### Optional.of()

null 아닌 객체를 Optional 로 감쌀때 `Optional.of()` 메서드를 사용한다.

```java
// null 이 아닌 값으로 Optional 만들기
Employee e1 = new Employee("소방관");
Optional<Employee> optEmployee = Optional.of(e1);
System.out.println("optEmployee = " + optEmployee);
```

출력결과

```plain
optEmployee = Optional[Employee{name='소방관', dept=null}]
```

<br>

### Optional.ofNullable()

null 값이 될 수 있는 값을 Optional 객체로 만들기

```java
Employee e2 = null;
Optional<Employee> optNullEmployee = Optional.ofNullable(e2);
System.out.println("optNullEmployee = " + optNullEmployee);
```

출력결과

```plain
optNullEmployee = Optional.empty
```

<br>

### 예제) empty, of, ofNullable 모두 사용해보기

```java
class Optional_BasicExample {

	class Employee{
		private String name;

		public Employee(String name){
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "Employee{" +
				"name='" + name + '\''
				'}';
		}
	}

	@Test
	@DisplayName("Optional 객체 만들기")
	void testMakeOptionalInstance(){
		// 빈 Optional
		Optional<Employee> optEmptyEmployee = Optional.empty();
		System.out.println("optEmployeeEmployee = " + optEmptyEmployee);

		// null 이 아닌 값으로 Optional 만들기
		Employee e1 = new Employee("소방관");
		Optional<Employee> optEmployee = Optional.of(e1);
		System.out.println("optEmployee = " + optEmployee);

		// null 값으로 Optional 만들기
		// null 값이 존재할 수 밖에 없고, 이것을 Optional.empty() 가 아닌 변수 값에 의해 결정되도록 하고 싶은 경우가 있다.
		// 이렇게 하면 Optional.empty 가 할당되게 된다.
		Employee e2 = null;
		Optional<Employee> optNullEmployee = Optional.ofNullable(e2);
		System.out.println("optNullEmployee = " + optNullEmployee);
	}
}
```

<br>

## Optional.map(Function)

Optional 로 감싼 객체 역시도 map() 함수를 사용 가능하다. Optional 의 map 함수는 입력값을 원한는 가공로직을 이용해 다른 객체로 변환하는 역할을 수행한다.<br>

<br>

### null 이 아닌 객체에  Optional.map 사용해보기

```java
@Test
public void OptionalMap_of_map함수_테스트(){
  Employee e1 = new Employee("지드래곤");
  Optional<String> name = Optional.of(e1).map(Employee::getName);
  System.out.println(name);
}
```

출력결과

```plain
Optional[지드래곤]
```

<br>

### null 객체에 대해 Optional.map 사용해보기

```java
@Test
public void OptionalMap_ofNullable_map함수_테스트(){
  Optional<Employee> optEmployee = Optional.ofNullable(null);
  Optional<String> optName = optEmployee.map(Employee::getName);
  System.out.println("optName = " + optName);
}
```

**출력결과**<br>

```plain
e2Name = Optional.empty
```

<br>

### 전체 코드

```java
public class OptionalTest {

	public class Employee{
		private String name;
		public Employee (String name){
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}

	@Test
	public void OptionalMap_of_map함수_테스트(){
		Employee e1 = new Employee("지드래곤");
		Optional<String> name = Optional.of(e1).map(Employee::getName);
		System.out.println(name);
	}

	@Test
	public void OptionalMap_ofNullable_map함수_테스트(){
		Optional<Employee> optEmployee = Optional.ofNullable(null);
		Optional<String> optName = optEmployee.map(Employee::getName);
		System.out.println("optName = " + optName);
	}
}
```

<br>

## Optional.flatMap(Function)

Optional.map 내부에서는  Employee 객체를 받아서 변환을 수행했었다. 하지만, flatMap 내부에서는 `Optional.of(Employee)` 를 받아서 처리를 수행한다는 점이다.<br>

```java
@Test
public void Optional_flatMap_테스트(){
  Department ff = new Department("소방서", 111D);
  Employee e1 = new Employee("태양", ff);
  Optional<String> result1 = Optional.of(e1).map(e -> e.getName());
  System.out.println("Optional.map >> result1 = " + result1);

  Optional<Double> result2 = Optional.of(new Employee("지드래곤", ff))
    .flatMap(e -> Optional.of(e.getDepartment()))
    .flatMap(d -> Optional.of(d.getSales()));
  System.out.println("Optional.flatMap >> result2 = " + result2);
}
```

<br>

## Optional 의 주요 메서드 요약

- empty
  - 빈 Optional 인스턴스 반환
- filter
  - 값이 존재하며, 프리디케이트와 일치하면 값을 포함하는 Optional 을 반환하고,
  - 값이 없거나 프리디케이트와 일치하지 않으면 빈 Optional 을 반환함
- flatMap
  - 값이 존재하면 인수로 제공된 함수를 적용한 결과 Optional 을 반환하고
  - 값이 있으면 빈 Optional 을 반환함
- get
  - 값이 존재하면 Optional 이 감싸고 있는 값을 반환하고
  - 값이 없으면 NoSuchElementException 이 발생함
- ifPresent
  - 값이 존재하면 지정된 Consumer 를 실행하고
  - 값이 없으면 아무 일도 일어나지 않음
- ifPresentOrElse
  - 값이 존재하면 지정된 Consumer 를 실행하고
  - 값이 없으면 아무 일도 일어나지 않음
- isPresent
  - 값이 존재하면 true 를 반환하고
  - 값이 없으면 false를 반환함
- map
  - 값이 존재하면 제공된 매핑함수를 적용함
- of
  - 값이 존재하면 값을 감싸는 Optional 을 반환하고
  - 값이 null 이면 NullPointerException 을 발생시킨다.
- ofNullable
  - 값이 존재하면 값을 감싸는 Optional 을 반환하고
  - 값이 null 이면 빈 Optional 을 반환한다.
- or
  - 값이 존재하면 같은 Optional 을 반환하고
  - 값이 없으면 Supplier 에서 만든 Optional 을 반환
- orElse
  - 값이 존재하면 값을 반환하고
  - 값이 없으면 기본값을 반환함
- orElseGet
  - 값이 존재하면 값을 반환하고
  - 값이 없으면 Supplier 에서 제공하는 값을 반환함
- orElseThrow
  - 값이 존재하면 값을 반환하고
  - 값이 없으면 Supplier 에서 생성한 예외를 발생함
- stream
  - 값이 존재하면 존재하는 값만 포함하는 스트림을 반환하고
  - 값이 없으면 빈 스트림을 반환함

<br>

## 주요 언랩 함수들 

Optional 로 감싸져 있는 객체는 프로그램 내에서 사용할 때는 언랩(UnWrap)해야 한다. 포장지를 언박싱해서, 내부의 값을 처리한다는 의미인데, 주요 메서드들은 아래와 같은 것들이 있다.

- get()
- orElse()
- orElseGet()
- orElseThrow()
- ifPresent()
- ifPresentOrElse()

<br>

각각의 내용들을 정리해보면 아래와 같다. TODO. 추후 더 깔끔하게 정리 예정이다<br>

### get()

Optional의 get() 메서드는  Optional<T>....get()을 수행할 때 

- 해당 옵셔널 객체가 Optional.empty가 아닐 경우는 
  - get() 을 호출할때 T에 해당하는 값을 반환한다.
- 해당 옵셔널 객체가  Optional.empty일 경우에는
  - Optional.empty 객체에 대해 get() 함수를 호출하는 것이기 때문에 `NoSuchElementException`을 발생시킨다.

<br>

### orElse()

orElse() 메서드를 이용하면 Optional 이 값을 포함하지 않을 때 기본값을 제공할 수 있다.<br>

### orElseGet (Supplier <? Extends T > other)

orElse() 메서드에 해당하는 게으른 버전의 메서드이다. (Optional 에 값이 없을 때만 Supplier 가 실행되기 때문이다.)

- 디폴트 메서드를 만드는 데에 시간이 걸리거나 (효율성 때문에)
- Optional 이 비어있을 때만 기본값을 생성하고 싶다면(기본값이 반드시 필요한 상황)

이런 경우에는 orElseGet(Supplier<? extends T> other) 를 사용해야 한다.

<br>

즉, Optional에 값이 없을 경우에 수행할 동작을 `Supplier <? Extends T > other` 에 람다식으로 정의해주면 된다. 그리고 이 Supplier 객체는  orElseGet 메서드 내에 전달해준다.

### orElseThrow(Supplier < ? extends X> exceptionSupplier)

Optional 이 비어있을 때에 예외를 발생시킨다는 점에서 get() 메서드와 비슷하다. 하지만 이 메서드는 발생시킬 예외의 종류를 선택하는 것이 가능하다.

### fPresent(Consumer <? super T> consumer)

- 값이 존재할 때 인수로 넘겨준 동작을 실행할 수 있다.
- 값이 없으면 아무 일도 일어나지 않는다.

 

### ifPresentOrElse() (java 9+)

ifPresentOrElse() 의 형태는 아래와 같다.

> ifPresentOrElse ( Consumer<? super T> action, Runnable emptyAction )

- ifPresentOrElse (Consumer c, Runnable emptyAction) 에서 emptyAction 은 Optional 이 비어있을 경우에 실행할 수 있는 Runnable 변수이다.
- Optional 이 비어있을 때만 실행할 수 있는 Runnable 을 인수로 받는 다는 점에서 ifPresent 와 다르다.

<br>

## 두 Optional 합치기



### Optional 언랩하지 않고 두 Optional 합치기

## 필터로 특정 값 거르기

## Optional 필터링





