package io.study.modernjavaexample.lambda.built_in;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class Predicate_TTest {

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
			if(predicate.test(t)){
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
}
