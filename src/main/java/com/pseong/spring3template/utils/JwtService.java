package com.pseong.spring3template.utils;

import com.pseong.spring3template.config.BaseException;
import com.pseong.spring3template.config.BaseResponseStatus;
import com.pseong.spring3template.config.Secret;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtService {

    public String createJwt(String username) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("username", username)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*6)) //1*(1000*60*60*24*365)))
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_SECRET_KEY)
                .compact();
    }

    public String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }

    public String getUsername() throws BaseException {
        // JWT 추출
        String accessToken = getJwt();
        if (accessToken == null || accessToken.length() == 0) {
            throw new BaseException(BaseResponseStatus.EMPTY_JWT);
        }
        try {
            accessToken = accessToken.split("Bearer ")[1];
        } catch (Exception e) {
            throw new BaseException(BaseResponseStatus.REQUEST_JWT_ERROR);
        }

        // JWT parsing
        Jws<Claims> claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_SECRET_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(BaseResponseStatus.INVALID_JWT);
        }

        // userIdx 추출
        String username = claims.getBody().get("username", String.class);
        if (username == null) throw new BaseException(BaseResponseStatus.INVALID_JWT);

        return username;
    }
}
