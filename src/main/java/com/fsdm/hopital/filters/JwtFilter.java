package com.fsdm.hopital.filters;

import com.fsdm.hopital.auth.jwt.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final RequestMatcher ignoredPaths = new AntPathRequestMatcher("/api/**/**");

    @Override
    @SneakyThrows
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) {
        if (this.ignoredPaths.matches(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        }else{
            String token = extractTokenFromHeaders(request);
            if(token == null || token.isEmpty()){
                token = extractTokenFromCookie(request);
            }
            if (token == null) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            try {
                boolean valid = jwtUtils.validateTokenSignature(token);
                if(!valid) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
                response.addHeader("x-auth", token);

                filterChain.doFilter(request, response);
            }
            catch(ExpiredJwtException expiredJwtException) {
                System.out.println(expiredJwtException.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "x-auth".equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
    private String extractTokenFromHeaders(HttpServletRequest request) {
        return request.getHeader("x-auth");
    }
}
