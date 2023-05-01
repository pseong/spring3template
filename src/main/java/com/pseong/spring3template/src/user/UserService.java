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

}