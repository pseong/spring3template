package com.pseong.spring3template.src.login;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pseong.spring3template.config.BaseException;
import com.pseong.spring3template.domain.OAuth;
import com.pseong.spring3template.domain.User;
import com.pseong.spring3template.src.oauth.OAuthRepository;
import com.pseong.spring3template.src.user.UserRepository;
import com.pseong.spring3template.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.RandomStringUtils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.pseong.spring3template.config.BaseResponseStatus.*;
import static com.pseong.spring3template.config.Constant.KAKAO_TYPE;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final OAuthRepository oAuthRepository;
    private final PasswordEncoder passwordEncoder;

    public String getKaKaoSub(String accessToken) throws BaseException {
        try {
            String requestURL = "https://kapi.kakao.com/v2/user/me";

            URL url = new URL(requestURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            int responseCode = conn.getResponseCode();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            String result = "";
            while ((line = buffer.readLine()) != null) {
                result +=line;
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            return element.getAsJsonObject().get("id").getAsString();
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_KAKAO_LOGIN);
        }
    }
    public String loginKakao(String accessToken) throws BaseException {
        try {
            String sub = getKaKaoSub(accessToken);
            OAuth oauth = oAuthRepository.findByTypeAndSub(KAKAO_TYPE, sub);
            User user = null;
            if (oauth == null) {
                user = createUser();
                createOAuth(user, KAKAO_TYPE, sub);
            }
            else user = oauth.getUser();
            if (!user.getActive()) {
                throw new BaseException(DISABLED_USER);
            }
            return jwtService.createJwt(oauth.getUser().getUsername());
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String loginPassword(String username, String password) throws BaseException {
        try {
            User user = userRepository.findByUsername(username);
            if (user == null) {
                throw new BaseException(FAILED_TO_PASSWORD_LOGIN);
            }
            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new BaseException(FAILED_TO_PASSWORD_LOGIN);
            }
            return jwtService.createJwt(username);
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public OAuth createOAuth(User user, Long type, String sub) throws BaseException {
        try {
            OAuth oauth = new OAuth();
            oauth.setType(type);
            oauth.setSub(sub);
            oauth.setUser(user);
            oAuthRepository.save(oauth);
            return oauth;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public User createUser() throws BaseException {
        try {
            User user = new User();
            user.setRoll("ROLE_USER");
            user.setCreateDate(LocalDateTime.now());
            user.setActive(true);
            user.setUsername(UUID.randomUUID().toString());
            user.setPassword(passwordEncoder.encode(RandomStringUtils.randomAlphanumeric(16)));
            userRepository.save(user);
            return user;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public User createUser(String username, String password) throws BaseException {
        try {
            User user = new User();
            user.setRoll("ROLE_USER");
            user.setCreateDate(LocalDateTime.now());
            user.setActive(true);
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return user;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}