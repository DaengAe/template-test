package com.backsuend.coucommerce.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.backsuend.coucommerce.member.domain.Member;
import com.backsuend.coucommerce.member.domain.Role;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignupRequest {

	@NotBlank(message = "이메일은 필수 입력값입니다.")
	@Email(message = "이메일 형식에 맞지 않습니다.")
	private final String email;

	@NotBlank(message = "비밀번호는 필수 입력값입니다.")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
		message = "비밀번호는 8~20자, 영문, 숫자, 특수문자를 포함해야 합니다.")
	private final String password;

	@NotBlank(message = "이름은 필수 입력값입니다.")
	private final String name;

	@NotBlank(message = "전화번호는 필수 입력값입니다.")
	@Pattern(regexp = "^\\d{11}$", message = "전화번호는 11자리 숫자여야 합니다.")
	private final String phone;

	@Builder
	public SignupRequest(String email, String password, String name, String phone) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.phone = phone;
	}

	public Member toEntity(PasswordEncoder passwordEncoder) {
		return Member.builder()
			.email(email)
			.password(passwordEncoder.encode(password))
			.name(name)
			.phone(phone)
			.role(Role.BUYER) // 기본 역할은 BUYER
			.build();
	}
}