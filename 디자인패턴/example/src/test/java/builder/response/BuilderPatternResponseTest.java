package builder.response;

import io.study.design_pattern.builder.KakaoTalkResponseDto;
import io.study.design_pattern.builder.ResponseDto;
import io.study.design_pattern.builder.UserProfileDto;
import io.study.design_pattern.builder.UserProfileDto.Builder;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

public class BuilderPatternResponseTest {

	@Test
	void 빌더패턴_Response_테스트(){
		UserProfileDto user1 = new Builder()
			.nickName("신사임당")
			.city("부천")
			.build();

		System.out.println("user1 = " + user1);

		ResponseDto dto = new KakaoTalkResponseDto.Builder(user1)
			.code(200)
			.message("메시지 발송에 성공했습니다.")
			.receviedTime(LocalDateTime.now())
			.build();

		System.out.println("dto = " + dto);
	}
}
