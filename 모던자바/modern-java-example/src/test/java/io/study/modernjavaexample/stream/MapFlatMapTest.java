package io.study.modernjavaexample.stream;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MapFlatMapTest {

	// String [] 으로 나뉜 각각의 스트림을 갖는다는 것은 조금 불편한 점이긴 하다.
	@Test
	@DisplayName("map함수로_문자열_조각내기")
	void map함수로_문자열_조각내기(){
		List<String> words = Arrays.asList("Hello", "World");
		List<String[]> wordArrays = words.stream()
			.map(word -> word.split(""))
			.distinct()
			.collect(Collectors.toList());

		wordArrays
			.stream()
			.forEach(array->{
				String str = Arrays.toString(array);
				System.out.println(str);
			});
	}


	// [h, e, l, l, o] [w, o, r, l, d] 가 [ h, e, l, l, o], [ W,o,r,l,d] 처럼 변경된 것 같지만 실제로는 아래와 같은 모양이다.
	// [ Stream h, Stream e, Stream l, Stream l, Stream o ], [ Stream w, Stream o, Stream r, Stream l, Stream d]
	// Stream<String> 으로 각각 분할되었다. 데이터의 원본인 String 으로 처리하는게 더 깔끔한데, 이건 조금 아직도 불편한 감이 있다.
	@Test
	@DisplayName("map함수에_트릭을_써서_flatMap_흉내내보기")
	void map함수에_트릭을_써서_flatMap_흉내내보기(){
		List<String> words = Arrays.asList("Hello", "World");
		List<Stream<String>> streamedArray = words.stream()
			.map(word -> word.split(""))
			.map(array -> Arrays.stream(array))
			.collect(Collectors.toList());

		streamedArray
			.stream()
			.forEach(stream -> {
				stream.forEach(str -> {
					System.out.print(str + ", ");
				});
			});
	}

	// 위에서 Stream<String> 으로 변환한 것에 착안해
	//  String [] -> Stream<String> 으로 변환하고
	//  flatMap 을 이용해 Stream<String> -> String 으로 변환해준다.
	//  이렇게 String 으로 변환되었음을 확인하기 위해 아래 예제에서는 toUpperCase() 함수를 적용했다.
	@Test
	@DisplayName("flatMap함수로_스트림을_평면화해보자_1")
	void flatMap함수로_스트림을_평면화해보자_1(){
		List<String> words = Arrays.asList("Hello", "World");
		List<String> flatResult = words.stream()
			.map(word -> word.split(""))
			.flatMap(array -> Arrays.stream(array))
			.map(s-> s.toUpperCase())
			.collect(Collectors.toList());

		System.out.println(flatResult);
	}

	// 위의 flatMap 예제에서는 [Hello, World] 를 [H,E,L,L,O, W,O,R,L,D] 로 변환했었다.
	// 이렇게 변환된 [H,E,L,L,O, W,O,R,L,D] 를 중복된 문자를 제거해서
	// [H, E, L, O, W, R, D] 로 변환해보자.
	@Test
	@DisplayName("flatMap함수로_스트림을_평면화해보자_2")
	void flatMap함수로_스트림을_평면화해보자_2(){
		List<String> words = Arrays.asList("Hello", "World");
		List<String> characters = words.stream()
			.map(word -> word.split(""))
			.flatMap(array -> Arrays.stream(array))
			.map(string -> string.toUpperCase())
			.distinct()
			.collect(Collectors.toList());

		System.out.println(characters);
	}
}
