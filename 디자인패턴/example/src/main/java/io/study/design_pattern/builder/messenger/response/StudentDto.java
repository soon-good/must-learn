package io.study.design_pattern.builder.messenger.response;

public class StudentDto {
	private final String name;
	private final String city;

	public StudentDto(Builder builder){
		this.name = builder.name;
		this.city = builder.city;
	}

	public static class Builder{
		private String name;
		private String city;

		public Builder(){}

		public Builder name(String name){
			this.name = name;
			return this;
		}

		public Builder city(String city){
			this.city = city;
			return this;
		}

		public StudentDto build(){
			return new StudentDto(this);
		}
	}
}
