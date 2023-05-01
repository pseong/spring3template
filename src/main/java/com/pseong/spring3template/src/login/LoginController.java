package com.pseong.spring3template.src.login;

import com.pseong.spring3template.config.BaseException;
import com.pseong.spring3template.config.BaseResponse;
import com.pseong.spring3template.src.login.model.PostLoginKakaoReq;
import com.pseong.spring3template.src.login.model.PostLoginKakaoRes;
import com.pseong.spring3template.src.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    LoginService loginService;
    @PostMapping("/kakao")
    public BaseResponse<PostLoginKakaoRes> postKaKao(@RequestBody @Valid PostLoginKakaoRes postLoginKakaoReq) {
        try {
            return new BaseResponse<>(new PostLoginKakaoRes(loginService.loginKakao(postLoginKakaoReq.getToken())));
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
}