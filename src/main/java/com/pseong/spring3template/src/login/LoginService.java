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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

import static com.pseong.spring3template.config.BaseResponseStatus.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LoginService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final OAuthRepository oAuthRepository;

    public String loginKakao(String accessToken) throws BaseException {
        String sub = null;
        String requestURL = "https://kapi.kakao.com/v2/user/me";

        try {
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
            sub = element.getAsJsonObject().get("id").getAsString();
        } catch (Exception e) {
            throw new BaseException(FAILED_TO_KAKAO_LOGIN);
        }
        try {
            OAuth oauth = oAuthRepository.findByTypeAndSub(3L, sub);
            User user = null;
            if (oauth == null) {
                user = createUser();
                createOAuth(user.getId(), 3L, sub);
            }
            else user = oauth.getUser();
            if (!user.getActive()) {
                throw new BaseException(DISABLED_USER);
            }
            return login(oauth.getUser().getUsername());
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public OAuth createOAuth(Long userId, Long type, String sub) throws BaseException {
        try {
            OAuth oauth = new OAuth();
            oauth.setType(type);
            oauth.setSub(sub);
            oauth.setUser(userRepository.getReferenceById(userId));
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
            userRepository.save(user);
            return user;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String login(String username) throws BaseException {
        try {
            return jwtService.createJwt(username);
        } catch (Exception exception) {
            throw new BaseException(JWT_ERROR);
        }
    }
}