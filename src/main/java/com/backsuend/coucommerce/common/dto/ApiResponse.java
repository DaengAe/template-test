package com.backsuend.coucommerce.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 응답에 포함하지 않음
public class ApiResponse<T> {

	private final boolean success;
	private final String message;
	private final T data;

	// 성공 응답 (데이터 포함)
	public static <T> ApiResponse<T> success(T data, String message) {
		return new ApiResponse<>(true, message, data);
	}

	// 성공 응답 (데이터만)
	public static <T> ApiResponse<T> success(T data) {
		return new ApiResponse<>(true, "요청에 성공했습니다.", data);
	}

	// 성공 응답 (데이터 없음)
	public static <T> ApiResponse<T> success(String message) {
		return new ApiResponse<>(true, message, null);
	}

	// 실패 응답
	public static ApiResponse<?> error(String message) {
		return new ApiResponse<>(false, message, null);
	}
}
