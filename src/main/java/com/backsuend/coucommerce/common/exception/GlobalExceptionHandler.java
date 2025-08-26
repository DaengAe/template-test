package com.backsuend.coucommerce.common.exception;

import com.backsuend.coucommerce.common.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * 이메일 중복 가입 시 발생하는 예외를 처리합니다.
   *
   * @param ex EmailAlreadyExistsException
   * @return 409 Conflict 상태 코드와 에러 메시지를 담은 응답
   */
  @ExceptionHandler(EmailAlreadyExistsException.class)
  public ResponseEntity<ApiResponse<?>> handleEmailAlreadyExistsException(
      EmailAlreadyExistsException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.error(ex.getMessage()));
  }

  /**
   * DTO의 @Valid 유효성 검사에 실패했을 때 발생하는 예외를 처리합니다.
   *
   * @param ex MethodArgumentNotValidException
   * @return 400 Bad Request 상태 코드와 첫 번째 유효성 검사 실패 메시지를 담은 응답
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<?>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    // 여러 유효성 검사 오류 중 첫 번째 오류 메시지를 사용합니다.
    String errorMessage = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
    return ResponseEntity.badRequest().body(ApiResponse.error(errorMessage));
  }

  /**
   * 위에서 처리되지 않은 모든 예외를 처리합니다.
   *
   * @param ex Exception
   * @return 500 Internal Server Error 상태 코드와 서버 내부 오류 메시지를 담은 응답
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<?>> handleGlobalException(Exception ex) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error("서버 내부 오류가 발생했습니다: " + ex.getMessage()));
  }
}
