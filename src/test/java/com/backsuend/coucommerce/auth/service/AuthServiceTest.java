package com.backsuend.coucommerce.auth.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.anyString;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.backsuend.coucommerce.auth.dto.LoginRequest;
import com.backsuend.coucommerce.auth.dto.SignupRequest;
import com.backsuend.coucommerce.common.exception.EmailAlreadyExistsException;
import com.backsuend.coucommerce.common.jwt.JwtTokenProvider;
import com.backsuend.coucommerce.common.jwt.RefreshTokenRepository;
import com.backsuend.coucommerce.common.jwt.TokenDto;
import com.backsuend.coucommerce.member.domain.Member;
import com.backsuend.coucommerce.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@InjectMocks
	private AuthService authService;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private RefreshTokenRepository refreshTokenRepository;

	@Test
	@DisplayName("회원가입 성공")
	void signup_success() {
		// given
		SignupRequest request = SignupRequest.builder()
			.email("test@test.com")
			.password("password123!")
			.name("테스트")
			.phone("01012345678")
			.build();

		when(memberRepository.existsByEmail(request.getEmail())).thenReturn(false);
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

		// when
		authService.signup(request);

		// then
		verify(memberRepository, times(1)).save(any(Member.class));
	}

	@Test
	@DisplayName("회원가입 실패 - 이메일 중복")
	void signup_fail_email_duplication() {
		// given
		SignupRequest request = SignupRequest.builder().email("test@test.com").build();
		when(memberRepository.existsByEmail(request.getEmail())).thenReturn(true);

		// when & then
		assertThrows(EmailAlreadyExistsException.class, () -> authService.signup(request));
	}

	@Test
	@DisplayName("로그인 성공")
	void login_success() {
		// given
		LoginRequest request = new LoginRequest(); // 실제 테스트에서는 필드 설정 필요
		Authentication authentication = mock(Authentication.class);
		TokenDto tokenDto = TokenDto.builder().accessToken("access").refreshToken("refresh").build();

		when(authenticationManager.authenticate(any())).thenReturn(authentication);
		when(jwtTokenProvider.generateTokenDto(authentication)).thenReturn(tokenDto);

		// when
		TokenDto result = authService.login(request);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getAccessToken()).isEqualTo("access");
		verify(refreshTokenRepository, times(1)).save(any());
	}
}
