# MongoDB에서의 인덱싱

인덱싱의 개념을 알면 조회시에도 적절하게 활용할수 있고, 운영상의 장애 역시 파악할 수 있을 것 같다.<br>

<br>

## 목차

- [참고자료](#참고자료)
- [인덱스의-특징](#인덱스의-특징)
- [_id 필드](#id-필드)
- [B-Tree 구조](b---tree-구조)
- [균형 맞춤](#균형-맞춤)
- [인덱스](#인덱스)
- [단일키 인덱스](#단일키-인덱스)
- [복합키 (Compound) 인덱스](#복합키-compound-인덱스)
- [multikey (다중키) 인덱스](#multikey---다중키-인덱스)
- [텍스트 인덱스](#텍스트-인덱스)
- [지리공간적 인덱스](#지리공간적-인덱스)
- [해시 인덱스](#해시-인덱스)<br>

<br>

## 참고자료

- [docs.mongodb.com - Indexes](https://docs.mongodb.com/manual/indexes/)
- [wikipedia - B 트리](https://ko.wikipedia.org/wiki/B_%ED%8A%B8%EB%A6%AC)<br>

<br>

## 인덱스의 특징

- 조회 쿼리를 효율적으로 만들어준다.
  - 인덱싱이 적용되어 있지 않다면 모든 도큐먼트를 하나씩 조회하게된다.
- 생성/삭제 작업이 빈번할 경우는 속도의 저하가 발생할 수 있다.
  - 생성 작업 발생시마다 매번 인덱스를 업데이트하게 된다.
- 단순인덱스, 복합인덱스의 개념이 있다.
- 단순인덱스는 필드 하나에 대해 인덱스가 적용된 것을 의미한다.
- 복합인덱스는 여러가지 필드에 인덱스가 적용된 것을 의미한다.<br>

<br>

## _id 필드

MongoDB는 새롭게 생성되는 도큐먼트 각각에는 `_id` 필드가 부여 된다. `_id`필드는 도큐먼트 각각에 부여되는 고유의 값이다. 고유하게 생성되는 특징 덕분에 기본키(primary key)라고 불리기도 한다.<br>

<br>

## B-Tree 구조

MongoDB가 데이터를 조회해서 원하는 정보를 가져올 때 MongoDB는 `B-Tree` 라는 자료구조를 사용한다.<br>

![이미지](https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/B-tree.svg/400px-B-tree.svg.png)

그림 출처 : [https://ko.wikipedia.org/wiki/B_%ED%8A%B8%EB%A6%AC](https://ko.wikipedia.org/wiki/B_%ED%8A%B8%EB%A6%AC)<br>

<br>

B-Tree 노드는 이진트리를 확장해 만든 자료구조인데, 하나의 노드가 가질수 있는 최대 자식노드의 갯수가 2 이상인 트리구조이다. 이진트리에 대한 자세한 내용은 [여기 (ko.wikipedia.org - B 트리)](https://ko.wikipedia.org/wiki/B_%ED%8A%B8%EB%A6%AC) 에서 확인 가능하다.<br>

위의 그림을 설명해보면 아래와 같다.<br>

- 부모노드 {7,16} 의 자식 노드 들 중 첫번째 노드에는 {1, 2, 5, 6} 이 있다. <br>
  - {1,2,5,6} 부모 노드인 7보다 작은 요소들 이다.<br>
  - 검색시 부모노드 {7,16} 에서 7보다 작은 값을 찾을 때 이 자식노드인 {1,2,5,6}을 찾게 된다.<br>
- 부모노드 {7,16} 의 자식 노드 들 중 두번째 노드에는 {9, 12} 가 있다.<br>
  - {9,12} 는 부모노드 내의 7, 16 사이의 값들 이다.<br>
  - 검색시 부모노드 {7,16} 에서 7 에서 16 사이의 값일 경우 자식 노드인 {9,12}  내에서 검색하게 된다.<br>
- 부모노드 {7,16} 의 자식노드 들 중 세번째 노드에는 16보다 큰 {18,21}이 있다.<br>
  - {18, 21} 은 부모노드 내의 {7,16} 에서 16 보다 큰 값들이다.<br>
  - 검색시 부모노드 {7,16} 에서 16 보다 큰 값을 조회해야 하는 경우, 16보다 큰 값을 찾을 때 이 자식노드인 {18, 21}을 검색하게 된다.<br>

<br>

## 균형 맞춤

하나의 노드에 키값이 적게 들어있을 경우 검색 효율이 떨어지게 될 수 있다. 이런 이유로 검색시 트리의 조회를 최소로 할 수 있도록 내부적으로 인덱스를 정리하는 절차를 거친다. 각 노드에 키를 추가하거나, 노드를 쪼개거나 합치는 과정을 거친다. 이러한 과정을 `균형 맞춤` 이라고 한다.<br>

<br>

새로운 데이터를 추가하거나, 삭제하는 경우 역시도 균형 맞춤 작업이 발생한다. 이런 이유로 정말 필요한 인덱스가 아니라면, 함부로 인덱스를 생성하지 않는 것이 좋다. 또는 생성 및 삭제 작업이 빈번하다면 인덱스를 만들지 않는 것도 때로는 고려해야 한다.<br>

<br>

## 인덱스

인덱스는 정렬, 검색 하는 경우로 나누어 인덱스로 사용할 필드 및 조건을 결정한다. MongoDB는 숫자 이외의 모든 데이터 타입에 대해 대소비교가 가능하기 때문에, 어떤 형식이든 인덱스를 만들 수 있다. 아래에서부터는 여러가지 인덱스의 유형들을 정리한다.<br>

<br>

## 단일키 인덱스

인덱스 키 값을 한가지를 갖는 방식을 `단일키 인덱스`라고 부른다. 대표적으로 `_id` 필드를 예로 들 수 있다.

![이미지](https://docs.mongodb.com/manual/images/index-ascending.bakedsvg.svg) 

이미지 출처 : [https://docs.mongodb.com/manual/images/index-ascending.bakedsvg.svg](https://docs.mongodb.com/manual/images/index-ascending.bakedsvg.svg)<br>

위의 그림을 보면 `{score : 1}` 이라는 구문을 통해 인덱스를 지정하고 있다. 이 `{score : 1}` 이라는 것은 score 필드를 오름차순으로 해서 인덱스를 생성하겠다는 의미이다. 만약 `{score: -1}` 과 같이 -1로 지정할 경우 내림차순으로 인덱스가 설정된다.<br>

만약 현재 인덱싱이 되어있는 `score` 필드는 오름차순으로,  `name` 필드에 대해서는 내림차순으로 조건을 주고, 정렬 옵션을 걸어서 조회요청을 해봤다고 해보자. 이 경우는 `score`에 대해 오름 차순으로 정렬하여 조회하는 과정 까지는 인덱스를 활용하게 된다. 하지만 `name` 필드에 대해서는 `score` 가 같은 도큐먼트들 사이에서 `name` 필드에 대해서 하나 하나 크기비교를 해가면서 내림차순 정렬을 완성하게 된다.<br>

<br>

## 복합키 (Compound) 인덱스

복합키 인덱스는 여러 가지의 필드를 키로 하는 인덱스이다. 데이터베이스의 복합키와 비슷한 것이라고 이해하면 될 것 같다. <br>

복합키 인덱스를 설정할 때 두 개 이상의 필드들을 어떤 방식으로 정렬할 지 지정해서 인덱스를 설정한다. 합키 인덱스는 여러가지 필드에 대해서 범위검색, 값 검색, 정렬이 가능하다.<br>

정렬시와 검색시에 인덱스가 활용되는지 아닌지를 파악하고 있어야 한다. 정렬의 경우는 모든 키조합의 순서와, 정렬 순서(역순/오름차순)를 어떻게 설정했느냐에 따라 인덱스가 활용되지 않는 경우도 있다. 반면 조회의 경우는 복합키의 키조합 순서나 키 정렬 순서와는 무관하게 적용된 인덱스가 적용되어 결과를 조회하게 된다.<br>

![이미지](https://docs.mongodb.com/manual/images/index-compound-key.bakedsvg.svg)

이미지 출처 : [https://docs.mongodb.com/manual/images/index-compound-key.bakedsvg.svg](https://docs.mongodb.com/manual/images/index-compound-key.bakedsvg.svg)<br>

위의 그림의 경우 userid 에 대해서는 오름차순으로 정렬한다. 그리고 score 에 대해서는 내림차순으로 정렬하도록 인덱스를 설정하고 있다.<br>

**정렬**<br>

> 정렬의 경우는 인덱스 설정시 지정하는 정렬 순서(방향) 및 키 조합의 선후관계가 검색시에 모두 관여된다. <br>

만약 userid, score 모두를 오름차순으로 정렬해야 하는 경우를 예로 들어보자. 이런 경우는 주어진 인덱스를 온전히 활용하지 못한다. userid의 경우는 오름차순으로 지정되어 있으므로, 인덱싱을 활용해 정렬된다. 하지만, score 의 경우는 내림차순 인덱스이므로, 오름차순으로 조회시에는 인덱싱을 활용하지 못하고, 같은 userid에 대해서는 하나씩 순차적으로 값을 비교해가며 정렬을 수행하게 된다.<br>

<br>

**검색**<br>

> 검색의 경우는 값,범위를 검색할 때에는 인덱스 키의 순서와, 키조합의 순서가 다르더라도 원하는 필드가 모두 포함되어 있다면 사용가능하다.<br>

예를 들어 위 그림에서의 userid가 "ca2"인 사용자의 score 가 75인 데이터를 찾는다고 하면, 정렬순서가 역순이든, 오름차순이든 무관하게 원하는 데이터를 인덱스를 활용해 조회해올수 있다.<br>

<br>

## multikey (다중키) 인덱스

> 참고 : [https://docs.mongodb.com/manual/indexes/#multikey-index](https://docs.mongodb.com/manual/indexes/#multikey-index)

MongoDB는 도큐먼트 기반 데이터베이스이기 때문에, 배열을 값으로 가지고 있는 경우 역시 존재한다. 이렇게 배열 값으로 저장된 필드를 검색하게 될 경우를 대비해 MongoDB는 '배열에 대한 인덱스'를 지원하고 있다.(If you index a field that holds an array value, MongoDB creates separate index entries for *every* element of the array. 참고: [docs.mongodb.com](https://docs.mongodb.com/manual/indexes/))<br>



![이미지](https://docs.mongodb.com/manual/images/index-multikey.bakedsvg.svg)

이미지 출처 : [https://docs.mongodb.com/manual/images/index-multikey.bakedsvg.svg](https://docs.mongodb.com/manual/images/index-multikey.bakedsvg.svg)<br>

MongoDB는 색인된 필드에 배열 값이 포함되어 있으면 다중키 색인을 작성할지 여부를 자동으로 결정한다. 다중키 유형을 명시적으로 지정할 필요는 없다. 명령어로 직접 명시적으로 다중키 인덱스를 만들려고 할 경우는 배열을 값으로 하는 필드를 인덱싱하도록 지정하면된다. 또는 배열속 임베디드 도큐먼트 필드를 가리키면 된다.<br>

<br>

## 텍스트 인덱스

> 참고 : 
>
> - [docs.mongodb.com - Indexes](https://docs.mongodb.com/manual/indexes/#multikey-index)
> - [docs.mongodb.com - Text Indexes](https://docs.mongodb.com/manual/core/index-text/)<br>

<br>

다른 인덱스의 경우 값/범위에 대한 검색을 최적화하고, 정렬할 때에 활용할 수 있다. 하지만, 이런 방식의 인덱스는 문자열 검색시에는 한계점이 있다. 기사의 제목을 저장할 경우 기사의 제목 그대로 조회를 하는 경우는 그리 많지 않다. 마치 SQL의 LIKE 연산과 같은 연산이 더 자주 쓰이는 것을 예로 들 수 있다.<br>

문자열 검색시에는 정확한 문자를 검색하거나, 범위검색, 정렬 기능을 사용할 일이 그리 많지 않다. 텍스트 인덱스는 단어 단위, 단어의 원형으로 검색하게 도와주는 인덱스이다. <br>

한국어의 경우 아직까지는 형태소 기능이 탑재되어 있지 않아서 단어 단위로 인덱싱해야 한다. 텍스트 인덱스는 하나의 컬렉션에 하나만 만들 수 있다. 하지만 복합키 방식을 사용하는 것이 가능하기에 다수의 필드에 텍스트 인덱스가 필요하면 복합키로 만들면 된다.<br>

텍스트 인덱스에 대한 자세한 설명은 [여기](https://docs.mongodb.com/manual/core/index-text/)에서 확인 가능하다.<br>

<br>

## 지리공간적 인덱스

MongoDB에는 지리 정보를 저장하고 검색하는 기능이 들어있다. 자세한 내용은 [여기](https://docs.mongodb.com/manual/indexes/#geospatial-index) 를 참고.<br>

<br>

## 해시 인덱스

해시 인덱스 방식은 필드에 있는 값을 직접 인덱싱하는 것이 아니라 해시화 함수를 통해 더 작은 크기로 변형된 값을 B-Tree 구조로 인덱싱한다. 해시 인덱싱을 이용하면 일반 인덱싱 방식보다 크기면에서 훨씬 작아지게 된다. 값 검색 속도 역시 일반 인덱싱보다 훨씬 빨라질 수 있다.<br>

하지만, 원래의 값이 변경되기 때문에 범위검색, 정렬에 이 방식의 인덱스를 사용하는 것은 불가능하다. 이 외에도 해시 인덱스 필드를 포함해서 복합키 인덱스를 생성하는 것 역시 불가능하다. 배열을 값으로 가지는 필드에도 해시 인덱스를 지정하는 것 역시 불가능하다. 배열 자체를 해시화하여 저장하기 때문이다.<br>

<br>

