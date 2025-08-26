package com.backsuend.coucommerce.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.backsuend.coucommerce.auth.dto.LoginRequest;
import com.backsuend.coucommerce.auth.dto.SignupRequest;
import com.backsuend.coucommerce.member.domain.Member;
import com.backsuend.coucommerce.member.domain.Role;
import com.backsuend.coucommerce.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AuthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private Member testMember;

	@BeforeEach
	void setUp() {
		memberRepository.deleteAll();
		testMember = Member.builder()
			.email("test@test.com")
			.password(passwordEncoder.encode("password123!"))
			.name("테스트")
			.phone("01012345678")
			.role(Role.BUYER)
			.build();
		memberRepository.save(testMember);
	}

	@Test
	@DisplayName("회원가입 API 성공")
	void signupApi_success() throws Exception {
		// given
		SignupRequest request = SignupRequest.builder()
			.email("newuser@test.com")
			.password("password123!")
			.name("새로운유저")
			.phone("01087654321")
			.build();

		// when & then
		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.message").value("회원가입이 완료되었습니다."))
			.andDo(print());
	}

	@Test
	@DisplayName("회원가입 API 실패 - 이메일 중복")
	void signupApi_fail_email_duplication() throws Exception {
		// given
		SignupRequest request = SignupRequest.builder()
			.email("test@test.com") // 이미 존재하는 이메일
			.password("password123!")
			.name("테스트")
			.phone("01011112222")
			.build();

		// when & then
		mockMvc.perform(post("/api/auth/signup")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isConflict()) // 409 Conflict
			.andExpect(jsonPath("$.success").value(false))
			.andDo(print());
	}

	@Test
	@DisplayName("로그인 API 성공")
	void loginApi_success() throws Exception {
		// given
		LoginRequest request = new LoginRequest("test@test.com", "password123!");

		// when & then
		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.success").value(true))
			.andExpect(jsonPath("$.data.accessToken").exists())
			.andExpect(jsonPath("$.data.refreshToken").exists())
			.andDo(print());
	}
}
