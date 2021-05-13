# JVM과 메모리 구조

JVM에 대해서도 정리해보려고 한다. 위키피디아만 보고 최대한 간단하게 리스트업 하듯이 정리하려고 했는데, 더 좋은 자료가 있어서 [jeong-pro.tistory.com/148](https://jeong-pro.tistory.com/148) 을 보고 정리했다.<br>

<br>

## JVM(Java Virtual Machine)

> 자바 가상머신. 자바 바이트 코드를 실행하는 역할을 수행한다.<br>

<br>

자바 소스 코드는 자바 컴파일러로 바이트코드로 컴파일 된다. <br>

컴파일된 바이트 코드는 JVM에 의해 기계어로 변환된다.<br>

(이때 기계어로 변환할 때 사용하는 것이 JIT 컴파일러이다.)<br>

![이미지](https://upload.wikimedia.org/wikipedia/commons/d/dd/JvmSpec7.png)

<br>

## JVM의 각 모듈들

**클래스로더(Class Loader)**

- .java 형태의 파일을 컴파일하면 .class 파일이 생성된다.<br>
- 클래스로더는 이렇게 생성된 클래스 파일들을 엮어서 Runtime Data Area 로 적재하는 역할을 수행한다.<br>
- Runtime Data Area 는 JVM이 운영체제로부터 할당받은 메모리 영역이다.<br>

<br>

**실행엔진(Execution Engine)**

- 클래스 로더가 Runtime Data Area 에 적재한 클래스(바이트코드)를 기계어로 변경해서 명령어 단위로 실행하는 역할을 수행한다.<br>
- 명령어를 하나 하나 실행하는 인터프리터(interpreter) 방식, JIT(Just In Time) 컴파일러를 이용하는 방식이 있다.<br>

<br>

**가비지 컬렉터(Garbage Collector, GC)**<br>

- Heap 메모리 영역에 생성(적재)된 객체 들 중에 참조되지 않는 객체들을 탐색 후 제거하는 역할을 수행한다.<br>
- GC가 동작하는 시점은 정확히 알 수 없다.<br>
- GC가 수행되는 동안 GC를 수행하는 스레드가 아닌 모든 스레드가 일시정지 된다.<br>
- Full GC가 일어나서 몇 초간 모든 스레드가 정지하게 되면 장애로 이어지는 치명적인 문제가 발생할 수 있다.<br>

**런타임 데이터 영역(Runtime Data Area)**<br>

- Method Area, 힙 영역, 스택 영역, PC Register, Method 스택 으로 구성되어 있다.<br>
- JVM 의 메모리 영역이다.<br>
- 클래스로더는 런타임 데이터 영역에 .class 파일을 적재한다.<br>

<br>

## Runtime Data Area 

- 스레드가 생성되었을 때 기준으로 메소드 영역과 힙 영역을 모든 스레드가 공유한다.<br>
- 스택영역과 PC레지스터, Native Method Stack 은 각각의 스레드마다 생성되고 공유되지 않는다.<br>

<br>

### Method Area (메소드 영역)

- 필드 정보 : 클래스 멤버 필드명, 데이터 타입, 접근 제어자 등의 필드 정보<br>
- 메서드 정보 : 메서드의 이름, 리턴 타입, 파라미터, 접근 제어자 등의 메서드 정보<br>
- Type 정보 : interface 인지, class 인지<br>
- Constant Pool (상수 풀) : 문자상수, 타입, 필드, 객체 참조가 저장된다.<br>
- 이 외에도 static 변수, final class 변수 들이 생성된다.<br>

<br>

### Heap Area (힙 영역)

- new 연산으로 생성된 인스턴스가 생성되는 영역<br>
- 메서드 영역에 로드된 클래스만 생성 가능하다.<br>
- Garbage Collector는 힙 영역에서 사용되지 않는 부분들을 검사해서 제거하는 작업을 수행한다.<br>

<br>

### Stack Area (스택 영역)

- 지역변수, 파라미터, 리턴값, 연산을 위한 임시 값 등을 생성하는 영역이다.<br>
- int a = 10; 이라는 소스를 작성하면 정수 값이 할당 될 수 있는 메모리 공간을 a 라고 잡아두고 그 메모리 영역에 값이 10이 들어간다. 스택에 메모리 이름을 a 라고 붙여두고 값이 10인 메모리 공간을 만든다.<br>
- Product p = new Product(); 라는 소스를 작성하면 Product p 는 스택 영역에 생성되고, new 로 생성된 Product 인스턴스는 힙 영역에 생성된다.<br>
  - 이 때 스택 영역에 생성된 p 가 힙 영역에 생성된 객체를 가리키고 있는다.<br>
  - 메서드를 호출할 때마다 개별적으로 스택이 생성된다.<br>

<br>

### PC Register (PC 레지스터)

스레드가 생성될 때마다 생성되는 영역이다. Program Counter이다.<br>

현재 스레드가 실행되는 부분의 주소와 명령을 저장하고 있는 영역이다. (CPU의 레지스터와는 다른 개념이다.)<br>

<br>

### Native Method Stack

JNI처럼 c/c++ 등 과 같은 자바 언어 외의 언어로 작성된 네이티브 코드를 위한 메모리 영역이다.<br>

<br>

## GC (Garbage Collector)

아직 괜찮은 자료를 찾지 못했다. 자료 검색을 조금 더 해봐야 할 것 같다.<br>

**Major GC (Full GC)**

- 

<br>

**Minor GC (New 영역에서 일어나는 GC)**<br>

- 

<br>

