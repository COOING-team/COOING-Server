package com.example.cooing.domain.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.example.cooing.global.RequestURI.AUTH_URI;

@RequestMapping(AUTH_URI)
@RestController
@RequiredArgsConstructor
public class AuthController {

//    private final AuthService authService;
//
//
//    @PostMapping("/kakao")
//    @Synchronized
//    @Operation(summary = "카카오 로그인", description =   "https://kauth.kakao.com/oauth/authorize?client_id=c47d356d74e2a8fef49e79609d9a20c4&redirect_uri=http://localhost:3000/login/kakao&response_type=code\n\n"+
//            "kakao authCode를 넣으면 JWT AccessToken이 나옵니다. isLocal은 true이면 로컬 주소입니다. \n")
//    public synchronized ResponseEntity<?> loginKakao(@RequestBody KakaoLoginRequest kakaoLoginRequest) {
//        return ResponseEntity.ok(authService.login(kakaoLoginRequest.getAuthorizationCode(), kakaoLoginRequest.getIsLocal()));
//    }
//
//    @GetMapping("/user/info")
//    @Operation(summary = "유저 마이페이지 정보 조회", description = "토큰이 필요합니다. 프로필 이미지 제공합니다.")
//    public ResponseEntity<MyInfoResponseDto> MyInfo(@AuthenticationPrincipal CustomUserDetails userDetail) {
//        return ResponseEntity.ok(authService.getMyInfo(userDetail));
//    }
//
//    @PatchMapping("/user/info")
//    @Operation(summary = "유저 마이페이지 수정", description = "토큰이 필요합니다. 프로필 이미지 제공합니다.\n\n" +
//            "리퀘스트 바디 중에 profilePicUrl의 경우 [1.이미지 수정 안함 -> 기존에 있던 url] [2.이미지 수정 함 -> 새 url] [3.기본 이미지로 -> null 던지기]\n")
//    public ResponseEntity<MyInfoResponseDto> ChangeInfo(@AuthenticationPrincipal CustomUserDetails userDetail,
//                                                        @RequestBody MyInfoChangeRequestDto myInfoChangeRequestDto) {
//        return ResponseEntity.ok(authService.changeInfo(userDetail, myInfoChangeRequestDto));
//    }
}
