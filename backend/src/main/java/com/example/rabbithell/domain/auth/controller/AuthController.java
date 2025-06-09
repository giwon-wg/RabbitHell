package com.example.rabbithell.domain.auth.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbithell.common.response.CommonResponse;
import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.auth.domain.MiniAuthUser;
import com.example.rabbithell.domain.auth.dto.request.FullJwtRequest;
import com.example.rabbithell.domain.auth.dto.request.LoginRequest;
import com.example.rabbithell.domain.auth.dto.request.SignupRequest;
import com.example.rabbithell.domain.auth.dto.request.TokenRefresRequest;
import com.example.rabbithell.domain.auth.dto.response.LoginResponse;
import com.example.rabbithell.domain.auth.dto.response.TokenResponse;
import com.example.rabbithell.domain.auth.service.AuthService;
import com.example.rabbithell.domain.auth.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final TokenService tokenService;

	@GetMapping("/login-failed")
	@ResponseBody
	public String loginFailed() {
		System.out.println("카카오 로그인 실패, 잠시 후 다시 시도해주세요.");
		return "카카오 로그인 실패, 잠시 후 다시 시도해주세요.";
	}

	@PostMapping("/token/full")
	public ResponseEntity<CommonResponse<TokenResponse>> createFullToken(
		@AuthenticationPrincipal MiniAuthUser authUser,
		@RequestBody FullJwtRequest request
	) {
		TokenResponse tokenResponse = tokenService.createFullToken(
			authUser.getUserId(),
			request.cloverName()
		);

		log.info("authUser = {}", authUser);

		return ResponseEntity.ok(
			CommonResponse.of(true, HttpStatus.OK.value(), "Full JWT 발급 성공", tokenResponse)
		);
	}

	@PostMapping("/signup")
	public ResponseEntity<CommonResponse<Void>> signup(@RequestBody SignupRequest request) {
		authService.signup(request);
		return ResponseEntity.ok(CommonResponse.of(true, HttpStatus.OK.value(), "회원가입 성공"));
	}

	@PostMapping("/login")
	public ResponseEntity<CommonResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
		LoginResponse loginResponse = authService.login(request.email(), request.password());
		return ResponseEntity.ok(CommonResponse.of(true, HttpStatus.OK.value(), "로그인 성공", loginResponse));
	}

	@PostMapping("/reissue")
	public ResponseEntity<CommonResponse<TokenResponse>> reissue(
		@RequestBody TokenRefresRequest request
	) {
		TokenResponse tokenResponse = authService.reissue(request);
		return ResponseEntity.ok(CommonResponse.of(true, HttpStatus.OK.value(), "토큰 재발행 성공", tokenResponse));
	}

	@PostMapping("/logout")
	public ResponseEntity<CommonResponse<Void>> logout(@AuthenticationPrincipal AuthUser authUser) {
		authService.logout(authUser.getUserId());
		return ResponseEntity.ok(CommonResponse.of(true, HttpStatus.OK.value(), "로그아웃 성공"));
	}
}
