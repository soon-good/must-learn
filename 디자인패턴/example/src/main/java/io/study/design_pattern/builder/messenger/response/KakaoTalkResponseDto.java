package io.study.design_pattern.builder.messenger.response;

import java.time.LocalDateTime;
import java.util.Optional;

public class KakaoTalkResponseDto extends ResponseDto{

	private final UserProfileDto userProfileDto;

	private LocalDateTime receivedTime;

	public static class Builder extends ResponseDto.Builder<KakaoTalkResponseDto, Builder>{

		private final UserProfileDto userProfileDto;

		public Builder(UserProfileDto userProfileDto){
			// 참고) 혼동하기 쉬운 사항
			// 	묵시적으로 super(); 가 호출된다. super() 는 ResponseBuilder 의 Builder() 이다.
			// 	즉, 상위 클래스의 생성자가 호출된다.
			// 	기본 중의 기본인 내용이지만, 사람의 의식흐름으로는 가끔 당연하다는 듯이 혼동하는 경우가 많다.
			// super();

//			this.userProfileDto = Objects.requireNonNull(userProfileDto);
			this.userProfileDto = Optional.of(userProfileDto).orElseGet(UserProfileDto::new);
			actualInstance = createActualInstance();
			actualClassBuilder = this;
		}

		public Builder receviedTime(LocalDateTime receivedTime){
			actualInstance.setReceivedTime(receivedTime);
			return self();
		}

//		public Builder userProfileDto(UserProfileDto userProfileDto){
//			getActualInstance().setUserProfileDto(userProfileDto);
//			return self();
//		}

		@Override
		public ResponseDto build() {
			return actualInstance;
		}

		@Override
		protected Builder self() {
			return this;
		}

		@Override
		protected KakaoTalkResponseDto createActualInstance() {
			return new KakaoTalkResponseDto(this.userProfileDto);
		}

		@Override
		protected Builder getActualBuilder() {
			return this;
		}
	}

	private KakaoTalkResponseDto(UserProfileDto userProfileDto){
		this.userProfileDto = userProfileDto;
	}

	private void setReceivedTime(LocalDateTime receivedTime){
		this.receivedTime = receivedTime;
	}

//	private void setUserProfileDto(UserProfileDto userProfileDto){
//		this.userProfileDto = userProfileDto;
//	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("KakaoTalkResponseDto { \n")
			.append("\tuserProfileDto = " + userProfileDto)
			.append(", \n")
			.append("\treceivedTime = " + receivedTime)
			.append(", \n")
			.append("\tstatus = " + getStatus())
			.append(", \n")
			.append("\tmessage = " + getMessage())
			.append("\n")
			.append("} \n");

		return buffer.toString();
	}
}
