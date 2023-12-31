package com.example.cooing.domain.auth;

import static com.example.cooing.global.RequestURI.AUTH_URI;

import com.example.cooing.domain.auth.dto.request.BabyRequest;
import com.example.cooing.domain.auth.dto.request.LoginRequest;
import com.example.cooing.domain.auth.dto.response.BabyResponseDto;
import com.example.cooing.domain.auth.dto.response.InfoResponseDto;
import com.example.cooing.domain.auth.dto.response.LoginResponseDto;
import com.example.cooing.global.base.BaseResponseDto;
import com.example.cooing.global.exception.CustomException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping(AUTH_URI)
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Synchronized
    @Operation(summary = "카카오 로그인", description = "토큰X. RequestDto를 넣으면 넣으면 JWT AccessToken이 나옵니다. oAuthProvider 예시 값 APPLE / KAKAO")
    public synchronized BaseResponseDto<LoginResponseDto> loginKakao(
        @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponseDto loginResponseDto = authService.login(loginRequest);

            return BaseResponseDto.success("로그인 성공", loginResponseDto);
        } catch (CustomException e) {
            // 실패 시
            return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
        }

    }

    @PostMapping("/baby")
    @Synchronized
    @Operation(summary = "아이 정보 등록", description = "토큰O. 회원가입 후, 아이를 등록하세요")
    public synchronized BaseResponseDto<BabyResponseDto> createBaby(
        @AuthenticationPrincipal CustomUserDetails userDetail,
        @RequestBody BabyRequest babyRequest) {
        return BaseResponseDto.success("아이 정보 등록 완료",
            authService.createBaby(userDetail, babyRequest));
    }

    @GetMapping("/baby")
    @Operation(summary = "[마이페이지] 아이 정보 조회", description = "토큰O.")
    public BaseResponseDto<InfoResponseDto> MyInfo(
        @AuthenticationPrincipal CustomUserDetails userDetail) {
        try {
            InfoResponseDto infoResponseDto = authService.getMyInfo(userDetail);

            return BaseResponseDto.success("마이페이지 조회 성공", infoResponseDto);
        } catch (CustomException e) {
            // 실패 시
            return BaseResponseDto.fail(e.getCustomErrorCode().getCode(), e.getMessage());
        }
    }

    @DeleteMapping("/delete-account")
    @Operation(summary = "회원 탈퇴")
    public BaseResponseDto<String> deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return BaseResponseDto.success(authService.deleteAccount(userDetails));
    }

}
