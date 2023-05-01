package com.pseong.spring3template.security;

import com.pseong.spring3template.config.BaseException;
import com.pseong.spring3template.utils.JwtService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.pseong.spring3template.config.BaseResponseStatus.USER_NOT_FOUND;
import static com.pseong.spring3template.config.Constant.*;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityUserDetailsService customUserDetailsService;
    private final JwtService jwtService;
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    public JwtAuthenticationFilter(SecurityUserDetailsService customUserDetailsService, JwtService jwtService,HandlerExceptionResolver resolver) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtService = jwtService;
        this.resolver = resolver;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        for (String[] uri : WHITE_LIST_URI_EQUAL) {
            if (path.equals(uri[0]) && (uri[1] == "ANY" || method.equals(uri[1]))) return true;
        }
        for (String[] uri : WHITE_LIST_URI_CONTAIN) {
            if (path.contains(uri[0]) && (uri[1] == "ANY" || method.equals(uri[1]))) return true;
        }
        return false;
    }

    // 나중에 provider로 바꾸면 좋음
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String path = request.getRequestURI();
            String sub = jwtService.getUserSub();
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(sub);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (BaseException exception) {
            resolver.resolveException(request, response, null, exception);
            return;
        } catch (UsernameNotFoundException exception) {
            resolver.resolveException(request, response, null, new BaseException(USER_NOT_FOUND));
            return;
        } catch (Exception exception) {
            resolver.resolveException(request, response, null, exception);
            return;
        }
        filterChain.doFilter(request, response);
    }
}