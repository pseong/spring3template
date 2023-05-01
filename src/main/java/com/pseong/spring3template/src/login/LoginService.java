package com.pseong.spring3template.src.user;

import com.pseong.spring3template.config.BaseException;
import com.pseong.spring3template.domain.User;
import com.pseong.spring3template.utils.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.pseong.spring3template.config.BaseResponseStatus.DATABASE_ERROR;
import static com.pseong.spring3template.config.BaseResponseStatus.DISABLED_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public String login(Long id) throws BaseException {
        try {
            Optional<User> opUser = userRepository.findById(id);
            User user = opUser.get();
            if (user == null) {
                user = new User();
                user.setRoll("ROLE_USER");
                user.setCreateDate(LocalDateTime.now());
                user.setActive(true);
                userRepository.save(user);

                // Init User name using user id
                user.setUsername("유저-" + user.getId());
                userRepository.save(user);
            }
            if (user.getActive() == false) {
                throw new BaseException(DISABLED_USER);
            }
            return jwtService.createJwt(id);
        } catch (BaseException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}