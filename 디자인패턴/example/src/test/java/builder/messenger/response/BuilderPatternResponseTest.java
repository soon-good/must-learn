package builder.messenger.response;

import io.study.design_pattern.builder.messenger.response.KakaoTalkResponseDto;
import io.study.design_pattern.builder.messenger.response.ResponseDto;
import io.study.design_pattern.builder.messenger.response.UserProfileDto;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BuilderPatternResponseTest {

	@Test
	void 단순_빌더패턴_UserProfileDto_테스트(){
		UserProfileDto user1 = new UserProfileDto.Builder()
			.nickName("신사임당")
			.city("부천")
			.build();

		UserProfileDto user2 = new UserProfileDto.Builder()
			.nickName("존리선생님")
			.city("뉴요크")
			.build();

		System.out.println("user1 = " + user1);
		System.out.println("user2 = " + user2);
		Assertions.assertNotSame(user1, user2);
	}

	@Test
	void 빌더패턴_Response_테스트(){
		UserProfileDto user1 = new UserProfileDto.Builder()
			.nickName("신사임당")
			.city("부천")
			.build();

		System.out.println("user1 = " + user1 + "\n");

		ResponseDto responseDto = new KakaoTalkResponseDto.Builder(user1)
			.code(200)
			.message("메시지 발송에 성공했습니다.")
			.receviedTime(LocalDateTime.now())
			.build();

		System.out.println("responseDto = " + responseDto);
	}
}
