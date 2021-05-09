package io.study.design_pattern.http;

public enum HttpStatus {
	OK(200, Series.SUCCESSFUL,"OK"),
	MOVED_TEMPORARILY(302, Series.REDIRECTION, "Moved Temporarily"),
	SEE_OTHER(303, Series.REDIRECTION, "See Other"),
	NOT_MODIFIED(304, Series.REDIRECTION, "Not Modified"),
	TEMPORARY_REDIRECT(307, Series.REDIRECTION, "Temporary Redirect"),
	PERMANENT_REDIRECT(308, Series.REDIRECTION, "Permanent Redirect"),
	FORBIDDEN(403, Series.CLIENT_ERROR, "Forbidden"),
	NOT_FOUND(404, Series.SERVER_ERROR, "Not Found"),
	INTERNAL_SERVER_ERROR(500, Series.SERVER_ERROR,"Internal Server Error"),
	BAD_GATEWAY(502, Series.SERVER_ERROR, "Bad Gateway"),
	SERVICE_UNAVAILABLE(503, Series.SERVER_ERROR,"Service Unavailable"),
	GATEWAY_TIMEOUT(504, Series.SERVER_ERROR,"Gateway Timeout"),
	HTTP_VERSION_NOT_SUPPORTED(505, Series.SERVER_ERROR,"HTTP Version not supported");

	private final int value;
	private final Series series;
	private final String reasonPhrase;

	HttpStatus(int value, Series series, String reasonPhrase){
		this.value = value;
		this.series = series;
		this.reasonPhrase = reasonPhrase;
	}

	public Series series(){
		return this.series;
	}

	public boolean is1xxInformational(){
		return this.series() == Series.INFORMATIONAL;
	}

	public boolean is2xxSuccessful(){
		return this.series() == Series.SUCCESSFUL;
	}

	public boolean is3xxRedirection(){
		return this.series() == Series.REDIRECTION;
	}

	public boolean is4xxClientError(){
		return this.series() == Series.CLIENT_ERROR;
	}

	public boolean is5xxServerError(){
		return this.series() == Series.SERVER_ERROR;
	}

	public boolean isError() {
		return this.is4xxClientError() || this.is5xxServerError();
	}

	public String toString() {
		return this.value + " " + this.name();
	}

	public static HttpStatus valueOf(int statusCode){
		HttpStatus status = resolve(statusCode);
		if (status == null) {
			throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
		} else {
			return status;
		}
	}

	public static HttpStatus resolve(int statusCode) {
		HttpStatus[] var1 = values();
		int var2 = var1.length;

		for(int var3 = 0; var3 < var2; ++var3) {
			HttpStatus status = var1[var3];
			if (status.value == statusCode) {
				return status;
			}
		}

		return null;
	}

	public static enum Series{
		INFORMATIONAL(1),
		SUCCESSFUL(2),
		REDIRECTION(3),
		CLIENT_ERROR(4),
		SERVER_ERROR(5);

		private final int value;

		private Series(int value) {
			this.value = value;
		}

		public int value() {
			return this.value;
		}

		public static HttpStatus.Series valueOf(int statusCode) {
			HttpStatus.Series series = resolve(statusCode);
			if (series == null) {
				throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
			} else {
				return series;
			}
		}

		public static HttpStatus.Series resolve(int statusCode) {
			int seriesCode = statusCode / 100;
			HttpStatus.Series[] var2 = values();
			int var3 = var2.length;

			for(int var4 = 0; var4 < var3; ++var4) {
				HttpStatus.Series series = var2[var4];
				if (series.value == seriesCode) {
					return series;
				}
			}

			return null;
		}
	}
}
