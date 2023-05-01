package com.pseong.spring3template.src.login;

import com.pseong.spring3template.config.BaseException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LoginServiceTest {

    @Autowired
    LoginService loginService;

    @Test
    void 아이디비밀번호_유저생성_테스트() throws Exception {
        String username = "pseong";
        String password = "mypasword1234@";
        loginService.createUser(username, password);
        loginService.loginPassword(username, password);
    }
}