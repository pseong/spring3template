package com.pseong.spring3template.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pseong.spring3template.config.BaseException;
import com.pseong.spring3template.config.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

import static com.pseong.spring3template.config.BaseResponseStatus.UNAUTHORIZED;

@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        log.error(UNAUTHORIZED.getMessage());
        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

        try (OutputStream os = httpServletResponse.getOutputStream()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(os, new BaseResponse<>(new BaseException(UNAUTHORIZED).getStatus()));
            os.flush();
        }
    }
}