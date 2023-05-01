package com.pseong.spring3template.src.test;

import com.pseong.spring3template.config.BaseException;
import com.pseong.spring3template.config.BaseResponse;
import com.pseong.spring3template.security.SecurityUser;
import com.pseong.spring3template.src.login.LoginService;
import com.pseong.spring3template.src.test.model.GetTestLoginRes;
import com.pseong.spring3template.src.test.model.GetTestRes;
import com.pseong.spring3template.src.test.model.GetTestRollAdminRes;
import com.pseong.spring3template.src.test.model.GetTestRollUserRes;
import com.pseong.spring3template.src.user.UserService;
import com.pseong.spring3template.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.pseong.spring3template.config.BaseResponseStatus.JWT_ERROR;
import static com.pseong.spring3template.config.BaseResponseStatus.REQUEST_ERROR;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
    private final LoginService loginService;
    private final JwtService jwtService;
    @GetMapping ("")
    public BaseResponse<SecurityUser> getName(@AuthenticationPrincipal SecurityUser securityUser) {
        return new BaseResponse<>(securityUser);
    }

    @GetMapping ("/error")
    public BaseResponse<GetTestRes> getError() {
        try {
            throw new BaseException(REQUEST_ERROR);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @GetMapping ("/login")
    public BaseResponse<GetTestLoginRes> getLoginToken() {
        try {
            return new BaseResponse<>(new GetTestLoginRes(jwtService.createJwt("8ca37b63-e270-4d54-931b-b8cbcfae8f92")));
        } catch (Exception exception) {
            return new BaseResponse<>(JWT_ERROR);
        }
    }

    @Secured("ROLE_USER")
    @GetMapping ("/roll/user")
    public BaseResponse<GetTestRollUserRes> getRollUser(@AuthenticationPrincipal SecurityUser securityUser) {
        return new BaseResponse<>(new GetTestRollUserRes("1"));
    }

    @Secured("ROLE_ADMIN")
    @GetMapping ("/roll/admin")
    public BaseResponse<GetTestRollAdminRes> getRollAdmin(@AuthenticationPrincipal SecurityUser securityUser) {
        return new BaseResponse<>(new GetTestRollAdminRes("1"));
    }
}