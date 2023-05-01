package com.pseong.spring3template.reqreslog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.ContentCachingRequestWrapper;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RequestLoggingAspect {
    private final ObjectMapper objectMapper;

    private String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
                .map(entry -> String.format("%s -> (%s)",
                        entry.getKey(), Joiner.on(",").join(entry.getValue())))
                .collect(Collectors.joining(", "));
    }

    @Pointcut(
            "within(@org.springframework.web.bind.annotation.RestController *) && " +
            "!within(@com.pseong.spring3template.reqreslog.NoLogging *) && " +
            "!@annotation(com.pseong.spring3template.reqreslog.NoLogging)"
    )
    public void controllerPointcut() {}

    @Around("com.pseong.spring3template.reqreslog.RequestLoggingAspect.controllerPointcut()")
    public Object doLogging(ProceedingJoinPoint pjp) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;

        Map<String, String[]> paramMap = request.getParameterMap();
        String params = "";
        if (paramMap.isEmpty() == false) {
            params = " [" + paramMapToString(paramMap) + "]";
        }

        Object result = null;

        long start = System.currentTimeMillis();
        try {
            result = pjp.proceed(pjp.getArgs());
            return result;
        } finally {
            String host = request.getRemoteHost();
            long end = System.currentTimeMillis();
            String str = String.format("Request: %s %s%s < %s (%dms)", request.getMethod(), request.getRequestURI(), params, host, end - start);
            if (cachingRequest.getContentType() != null && cachingRequest.getContentType().contains("application/json")) {
                if (cachingRequest.getContentAsByteArray() != null && cachingRequest.getContentAsByteArray().length != 0){
                    str += " " + objectMapper.readTree(cachingRequest.getContentAsByteArray());
                }
            }
            log.info(str);
            log.info("Response: {} < {}", objectMapper.writeValueAsString(result), host);
        }
    }
}