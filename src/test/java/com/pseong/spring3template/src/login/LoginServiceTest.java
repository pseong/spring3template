package com.pseong.spring3template.src.login;

import com.pseong.spring3template.config.BaseException;
import com.pseong.spring3template.domain.OAuth;
import com.pseong.spring3template.domain.User;
import com.pseong.spring3template.src.oauth.OAuthRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class LoginServiceTest {

    @Autowired
    LoginService loginService;
    @Autowired
    OAuthRepository oAuthRepository;


    @Test
    void 아이디_비밀번호_유저_생성() throws Exception {
        String username = "pseong";
        String password = "mypasword1234@";
        loginService.createUser(username, password);
        loginService.loginPassword(username, password);
        Assertions.assertThrows(BaseException.class, () -> {
            loginService.loginPassword(username, "dd");
        });
    }

    @Test
    void 중복_아이디_생성() throws Exception {
        String username = "pseong";
        String password = "mypasword1234@";
        loginService.createUser(username, password);
        Assertions.assertThrows(BaseException.class, () -> {
            loginService.createUser(username, password);
        });
    }

    @Test
    void OAuth_생성() throws Exception {
        User user = loginService.createUser();
        loginService.createOAuth(user, 3L, "mysub");
        OAuth oauth = oAuthRepository.findByTypeAndSub(3L, "mysub");
        assertEquals(oauth.getUser().getUsername(),user.getUsername());
    }
}