package com.survey.api.common.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Response {

	private int responseCode;
	private String message;
	private Object responseData;
	private LocalDateTime timestamp = LocalDateTime.now();

	public Response(int responseCode, Object responseData) {
		super();
		this.responseCode = responseCode;
		this.responseData = responseData;
	}

	public Response(int responseCode, String message) {
		super();
		this.responseCode = responseCode;
		this.message = message;
	}

}
