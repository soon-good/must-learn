# MongoDB의 참조방식들

MongoDB에서는 컬렉션의 도큐먼트 간 서로 데이터를 참조하는 방식은 아래의 방식들이 있다. (RDB에서의 JOIN 과 유사한 개념이다.) <br>

- 레퍼런스 방식<br>
- 확장된 레퍼런스 방식<br>
- 임베디드 방식<br>
- subset 패턴<br>

<br>

[맛있는 MongoDB](http://www.yes24.com/Product/Goods/85011885) 에서는 레퍼런스 방식, 임베디드 방식을 설명하고 있다. 이중 임베디드 방식을 사용할 경우의 단점에 대해서도 자세히 설명하고 있다. 호기심이 많아서 이것 말고도 다른 패턴이 있나 하고 여기 저기 검색하면서 찾아봤는데, subset 패턴([드림어스컴퍼니 기술블로그 - MongoDB 도입기](https://www.blog-dreamus.com/post/flo-tech-mongodb-%EB%8F%84%EC%9E%85%EA%B8%B0))에 대해서 알게 되었고, 이 외에도 [Extended Reference Pattern](https://www.mongodb.com/blog/post/building-with-patterns-the-extended-reference-pattern) 방식에 대해서 알게 되었다.<br>

이 외에도 아래의 패턴들이 있는데, 차차 정리해나갈 예정이다. 오늘은 일단 임베디드 방식과 레퍼런스 방식에 대해서 최대한 간결하고 명료하게 정리해 볼 예정이다. <br>

- [Attribute Pattern](https://www.mongodb.com/blog/post/building-with-patterns-the-attribute-pattern)<br>
- [Outlier Pattern](https://www.mongodb.com/blog/post/building-with-patterns-the-outlier-pattern)<br>

<br>

## 목차

- [참고자료](#참고자료)<br>
- [레퍼런스 방식](#레퍼런스-방식)<br>
- [임베디드 방식](#임베디드-방식)<br>
- [레퍼런스 방식과 임베디드 방식의 차이점](#레퍼런스-방식과-임베디드-방식의-차이점)<br>
- [임베디드 방식의 장단점](#임베디드-방식의-장단점)<br>
  - [단점](#단점)<br>
  - [장점](#장점)<br>
- [Extended Reference 패턴 (확장된 레퍼런스 방식)](#Extended-Reference-패턴-확장된-레퍼런스-방식)<br>
- [subset 패턴](#subset-패턴)<br>

<br>

## 참고자료

- 주교재<br>
  - [맛있는 MongoDB](http://www.yes24.com/Product/Goods/85011885)<br>

- 참조관계 방식에 관련된 자료<br>
  - [드림어스컴퍼니 기술블로그 - MongoDB 도입기](https://www.blog-dreamus.com/post/flo-tech-mongodb-%EB%8F%84%EC%9E%85%EA%B8%B0)<br>

- MongoDB 공식 매뉴얼
  - [(레퍼런스 방식) Building with Patterns : The Extended Reference Pattern](https://www.mongodb.com/blog/post/building-with-patterns-the-extended-reference-pattern)<br>

<br>

## 레퍼런스 방식

레퍼런스 방식은 아래와 같은 형식이다. 학교에서 처음 데이터베이스를 배울 때 Employee,Department 를 예제로 하는 경우가 많아서 아래의 예제를 만들어봤다.

![이미지](./img/REFERENCE-MODEL.png)

레퍼런스 방식에서는 정보의 양이 늘어날수록 크기가 작은 도큐먼트의 갯수가 늘어난다. 그리고 도큐먼트 들 사이의 관계를 저장하기 위한 필드가 더 필요하게 된다. 레퍼런스 방식과, 임베디드 방식은 자주 비교되며 장단점을 비교하는 편인데 여기에 대해서는 뒤에서 레퍼런스 방식과 임베디드 방식의 차이점을 정리해볼 예정이다.<br>

<br>

## 임베디드 방식

> 참고) MongoDB 에서는 관계형 DB에서의 각 테이블의 row 와 같은 개념을 **'도큐먼트'** 라고 부른다.<br>

아래와 같이 참조관계에 있는 도큐먼트 들을 배열로 표현해 저장하는 방식을 임베디드 방식의 모델이라고 한다. 이 방식에는 한계점이 있다. MongoDB의 도큐먼트의 크기는 16MB까지가 제한되어 있다. 이 16MB 의 제한이 걸려있는 문제로 인해 배열 사이즈가 무한히 길어질 경우 데이터가 유실될 위험 역시 존재한다. 이런 이유로 **subset** 패턴 등을 적용하는데, 이것은 내일 이후로 정리하게 될 것 같다.

subset pattern 에 대해서는 아래에서 자세히 다루겠지만, 참고할 만한 자료들은 아래와 같다.

- [www.mongodb.com - subset pattern](https://www.mongodb.com/blog/post/building-with-patterns-the-subset-pattern)
- [드림어스컴퍼니 기술블로그 - MongoDB 도입기](https://www.blog-dreamus.com/post/flo-tech-mongodb-%EB%8F%84%EC%9E%85%EA%B8%B0)<br>

<br>

![이미지](./img/EMBEDED-MODEL.png)

<br>

## 레퍼런스 방식과 임베디드 방식의 차이점

**레퍼런스 방식**의 경우 정보의 양이 늘어날 수록 도큐먼트의 수가 늘어난다.<br>

**임베디드 방식**의 경우 정보의 양이 늘어날 수록 도큐먼트의 크기가 커진다.<br>

**레퍼런스 방식**의 경우 도큐먼트들 사이의 관계를 저장하기 위한 필드가 더 필요하다. <br>

**임베디드 방식**은 도큐먼트 내의 배열 필드를 수정하는 방식으로 정보를 추가한다.<br>

**레퍼런스 방식**의 경우 연결된 정보를 SELECT ALL 해올 경우는 부하가 있을 수 있다.<br>

**임베디드 방식**은 연결된 정보를 SELECT ALL 해올 경우 단순히 배열 형태의 필드를 반환하면 되므로 부하가 크지 않다. (하지만, 전체 내용을 불러오기보다는 페이지네이션을 적용하는 경우가 더 많기 때문에, 현실적으로 SELECT ALL을 해오는 경우는 많지 않다는 점도 생각해야 한다.)<br>

<br>

## 임베디드 방식의 장단점

### 단점

**배열 필드 내에서 필요한 정보를 추려낼때 조회 성능 저하**<br>

임베디드 방식을 사용할 경우 임베디드 된 데이터의 경우 배열과 같은 자료구조로 저장되어 있는데, 이 배열 내에서 필요한 정보만을 불러와야 할 경우 읽기 성능이 느려질 수 있다.<br>

<br>

**전체 데이터를 통으로 불러올 경우는 유리, 하지만 일부 페이지를 선택해 들고올 때는 불리**<br>

전체 데이터를 한꺼번에 들고오는 경우는 배열 방식의 경우 유리하다. 하지만 일부 페이지를 선택해 들고 올 때는 조금 불리한 편이다. 페이지네이션을 적용하거나 탑N 성 쿼리를 보낼때는 불리해지는 편이다.<br>

<br>

**subset 패턴, TopN 쿼리의 결과를 도큐먼트 내에 배열로 가지고 있기 위한 대안**<br>

TopN 쿼리가 제품의 특성으로 이미 결정지어져 있고, 자주 사용한다면 쿼리를 따로 작성하지 않고 subset 패턴을 통해 결합하는것도 좋은 방식이다. 제품에 대한 이해가 있다면 subset 패턴을 도입하는 것도 나쁘지 않은 방식인 것 같다. 여기에 대해서는 [드림어스컴퍼니 기술블로그 - MongoDB 도입기](https://www.blog-dreamus.com/post/flo-tech-mongodb-%EB%8F%84%EC%9E%85%EA%B8%B0) 에서 자세히 다루고 있다. 글의 뒤에서 간결 명료하게 정리해볼 예정이다.<br>

<br>

**도큐먼트 크기제한 - 16MB** <br>

MongoDB에서 하나의 도큐먼트가 가질수 있는 최대 크기는 16MB이다. 정보의 양이 무한히 쌓이는 경우 도큐먼트의 특정 필드에 계속해서 배열에 데이터를 추가하게 되어 16MB를 넘어갈 수도 있다.<br>

<br>

**데이터 수정시의 단점**<br>

임베디드 방식은 배열 기반으로 저장되어 있는 필드를 수정해야 하는데, 배열기반의 필드에서 원하는 필드를 찾아서 수정하는 것은 성능상으로 불리하다.<br>

<br>

### 장점

**트랜잭션의 원자성**<br>

게시글 수정시 하나의 도큐먼트 내에서 수정하게 되므로 트랜잭션의 원자성이 지켜지게 된다.<br>

<br>

**SELECT ALL 시 조회 성능**<br>

SELECT ALL 시에 배열 기반으로 이루어져 있는 필드를 그대로 반환하면 되기 때문에 SELECT ALL 연산에 대한 성능이 보장된다. 하지만, SELECT ALL 보다는 특정 기간 검색에 특화되어 있는 제품이 많기 때문에 그리 유용한 선택은 아니다.<br>

<br>

**적용한다면 유리할 수 있는 경우들**<br>

배열로 저장할 필드가 적고 조건 조회에 성능이 소모되지 않는다면 임베디드 방식을 고려해보는 것도 좋은 방식이다.<br>

예를 들면 '철수'라는 사용자의 주소가 2개라고 해보자. 하나는 **배송을 위한 실거주지** 이고, 다른 하나는 **등본상의 주소지**이다. 이 경우 조회시마다 주소 테이블을 한번 더 조회해서 연관 키값을 통해 두개의 주소를 불러와야 한다. 만약 이렇게 여러개의 주소지에 대한 요구사항이 제품의 특성에서 고정되어 있는 경우라면, 사용자 테이블의 주소에 배열 형태로 임베디드 방식으로 2개의 주소를 저장해 두어 조회연산을 줄이는 것도 좋은 선택일 것 같다. <br>

이후 주소지가 변경되면, 수정 페이지에서 수정 연산을 할 때 배열 내의 2개의 필드만 수정해주면 된다.<br>

이러한 방식을 subset 패턴이라고 하는데, 이 subset 패턴은 이번 문서의 뒤에서 따로 정리할 예정이다.<br>

<br>

## Extended Reference 패턴 (확장된 레퍼런스 방식)

TODO 정리<br>

참고자료 : [www.mongodb.com - The Extended Reference Pattern](https://www.mongodb.com/blog/post/building-with-patterns-the-extended-reference-pattern)<br>

![이미지](https://webassets.mongodb.com/_com_assets/cms/extended_reference1-o1xbjhqpca.png)

<br>

## subset 패턴

TODO 정리<br>

**참고자료**<br>

- [드림어스컴퍼니 기술블로그 - MongoDB 도입기](https://www.blog-dreamus.com/post/flo-tech-mongodb-%EB%8F%84%EC%9E%85%EA%B8%B0)
- [www.mongodb.com - subset pattern](https://www.mongodb.com/blog/post/building-with-patterns-the-subset-pattern)<br>

<br>

## Attribute 패턴

TODO 정리<br>

[Attribute Pattern](https://www.mongodb.com/blog/post/building-with-patterns-the-attribute-pattern)<br>

<br>

## Outlier 패턴

[Outlier Pattern](https://www.mongodb.com/blog/post/building-with-patterns-the-outlier-pattern)<br>

<br>

TODO 정리
