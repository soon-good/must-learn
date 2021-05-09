package io.study.design_pattern.builder.messenger.response;

public class UserProfileDto {
	private String nickName;	// 닉네임
	private String city;		// 주거지역

	public UserProfileDto(){}

	public static class Builder{
		private final UserProfileDto actualInstance;

		public Builder(){
			actualInstance = new UserProfileDto();
		}

		public Builder self(){
			return this;
		}

		public UserProfileDto build(){
			return actualInstance;
		}

		public Builder nickName(String nickName){
			actualInstance.setNickName(nickName);
			return self();
		}

		public Builder city(String city){
			actualInstance.setCity(city);
			return self();
		}
	}

	private void setNickName(String nickName) {
		this.nickName = nickName;
	}

	private void setCity(String city) {
		this.city = city;
	}

	public String getNickName() {
		return nickName;
	}

	public String getCity() {
		return city;
	}

	@Override
	public String toString() {
		return "UserProfileDto{" +
			"nickName='" + nickName + '\'' +
			", city='" + city + '\'' +
			'}';
	}
}
