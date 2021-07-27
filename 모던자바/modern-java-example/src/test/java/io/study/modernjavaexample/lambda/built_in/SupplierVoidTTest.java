package io.study.modernjavaexample.lambda.built_in;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class SupplierVoidTTest {

	@Test
	@DisplayName("단순예제1_단순하게_선언해서_사용해보기")
	void 단순예제1_단순하게_선언해서_사용해보기(){
		Supplier<String> supplier = () -> "안녕하세요";
		String hello_kr = supplier.get();
		System.out.println(hello_kr);
	}

	@Builder @AllArgsConstructor @Getter @ToString
	class Employee{
		private Long id;
		private String name;
	}

	@Test
	@DisplayName("단순예제2_사용자_정의_자료형_리턴하기")
	void 단순예제2_사용자_정의_자료형_리턴하기(){
		Supplier<Employee> supplier = ()->{
			return Employee.builder().id(1L).name("최준").build();
		};

		System.out.println(supplier.get());
	}
}
