package io.study.design_pattern.builder.messenger.response;

import io.study.design_pattern.http.HttpStatus;

public abstract class ResponseDto {
	private HttpStatus status;

	private int code;

	private String message;

	protected abstract static class Builder<O extends ResponseDto, T extends Builder>{
		protected O actualInstance;
		protected T actualClassBuilder;

		protected abstract O createActualInstance();
		protected abstract T getActualBuilder();

		protected Builder(){}

		public T code(int code){
			actualInstance.setCode(code);
			return self();
		}

		public T message(String message){
			actualInstance.setMessage(message);
			return self();
		}

		public abstract ResponseDto build();
		protected abstract T self();
	}

	protected void setCode(int code){
		this.code = code;
	}

	protected void setMessage(String message){
		this.message = message;
	}

	public HttpStatus getStatus(){
		return HttpStatus.valueOf(this.code);
	}

	public String getMessage(){
		return this.message;
	}

	public boolean isResponseSuccessful(){
		return this.status.is2xxSuccessful();
	}
}
