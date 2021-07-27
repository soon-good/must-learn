package io.study.modernjavaexample.lambda.built_in;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConsumerTTest {

	@Test
	@DisplayName("Consumer_예제_1_문자열_길이_출력하기")
	void Consumer_예제_1_문자열_길이_출력하기(){
		Consumer<String> printLengthLn = (t)->{
			System.out.println(t.length());
		};

		String inputString = "ABC";
		printLengthLn.accept(inputString);
	}

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
}
