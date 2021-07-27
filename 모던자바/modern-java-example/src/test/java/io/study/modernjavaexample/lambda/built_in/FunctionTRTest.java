package io.study.modernjavaexample.lambda.built_in;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FunctionTRTest {

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
}
