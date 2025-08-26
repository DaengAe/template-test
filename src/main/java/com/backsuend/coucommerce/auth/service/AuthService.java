package com.backsuend.coucommerce.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backsuend.coucommerce.auth.dto.LoginRequest;
import com.backsuend.coucommerce.auth.dto.SignupRequest;
import com.backsuend.coucommerce.auth.dto.TokenRequest;
import com.backsuend.coucommerce.common.jwt.JwtTokenProvider;
import com.backsuend.coucommerce.common.jwt.RefreshToken;
import com.backsuend.coucommerce.common.jwt.RefreshTokenRepository;
import com.backsuend.coucommerce.common.jwt.TokenDto;
import com.backsuend.coucommerce.common.security.SecurityUtil;
import com.backsuend.coucommerce.member.domain.Member;
import com.backsuend.coucommerce.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	private final RefreshTokenRepository refreshTokenRepository;

	public void signup(SignupRequest memberRequestDto) {
		if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
			throw new com.backsuend.coucommerce.common.exception.EmailAlreadyExistsException("이미 가입되어 있는 유저입니다.");
		}

		Member member = memberRequestDto.toEntity(passwordEncoder);
		memberRepository.save(member);
	}

	public TokenDto login(LoginRequest loginRequestDto) {
		UsernamePasswordAuthenticationToken authenticationToken =
			new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

		Authentication authentication = authenticationManager.authenticate(authenticationToken);

		// 토큰 생성
		TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

		// 리프레시 토큰 저장
		RefreshToken refreshToken = RefreshToken.builder()
			.key(authentication.getName())
			.value(tokenDto.getRefreshToken())
			.build();

		refreshTokenRepository.save(refreshToken);

		return tokenDto;
	}

	public TokenDto reissue(TokenRequest tokenRequestDto) {
		// 1. Refresh Token 검증
		if (!jwtTokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
			throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
		}

		// 2. Access Token 에서 Member ID 가져오기
		Authentication authentication = jwtTokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

		// 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
		RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
			.orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

		// 4. Refresh Token 일치하는지 검사
		if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
			throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
		}

		// 5. 새로운 토큰 생성
		TokenDto tokenDto = jwtTokenProvider.generateTokenDto(authentication);

		// 6. 저장소 정보 업데이트
		RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
		refreshTokenRepository.save(newRefreshToken);

		// 토큰 발급
		return tokenDto;
	}

	@Transactional
	public void logout() {
		// 현재 사용자의 인증 정보에서 username(email)을 가져옵니다.
		String username = SecurityUtil.getCurrentUsername();
		// 해당 username을 키로 하는 RefreshToken을 데이터베이스에서 삭제합니다.
		refreshTokenRepository.findByKey(username).ifPresent(refreshTokenRepository::delete);
	}
}
