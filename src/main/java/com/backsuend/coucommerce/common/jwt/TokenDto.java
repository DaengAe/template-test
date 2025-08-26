package com.backsuend.coucommerce.common.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenDto {
	/**
	 * [grantType를 Bearer로 명시]
	 * "우리가 발급한 토큰은 Bearer 타입이니, 앞으로 API를 요청할 때
	 * `Authorization` 헤더에 `Bearer <token>`형식으로 담아서 보내야 합니다"
	 * 라는 것을 클라이언트에게 알려주는 일종의 약속이자 명세
	 */
	private String grantType;
	private String accessToken;
	private String refreshToken;
}
