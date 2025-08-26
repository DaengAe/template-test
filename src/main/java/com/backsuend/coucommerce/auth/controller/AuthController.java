package com.backsuend.coucommerce.auth.controller;

import com.backsuend.coucommerce.auth.dto.LoginRequest;
import com.backsuend.coucommerce.auth.dto.SignupRequest;
import com.backsuend.coucommerce.auth.dto.TokenRequest;
import com.backsuend.coucommerce.auth.service.AuthService;
import com.backsuend.coucommerce.common.dto.ApiResponse;
import com.backsuend.coucommerce.common.jwt.TokenDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<ApiResponse<Void>> signup(
      @Valid @RequestBody SignupRequest memberRequestDto) {
    authService.signup(memberRequestDto);
    return ResponseEntity.ok(ApiResponse.success("회원가입이 완료되었습니다."));
  }

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<TokenDto>> login(
      @Valid @RequestBody LoginRequest loginRequestDto) {
    TokenDto tokenDto = authService.login(loginRequestDto);
    return ResponseEntity.ok(ApiResponse.success(tokenDto, "로그인에 성공했습니다."));
  }

  @PostMapping("/reissue")
  public ResponseEntity<ApiResponse<TokenDto>> reissue(@RequestBody TokenRequest tokenRequestDto) {
    return ResponseEntity.ok(
        ApiResponse.success(authService.reissue(tokenRequestDto), "토큰이 재발급되었습니다."));
  }

  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<Void>> logout() {
    authService.logout();
    return ResponseEntity.ok(ApiResponse.success("로그아웃되었습니다."));
  }
}
