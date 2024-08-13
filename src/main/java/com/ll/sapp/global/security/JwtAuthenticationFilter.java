package com.ll.sapp.global.security;

import com.ll.sapp.member.AuthTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthTokenService authTokenService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        log.debug("JwtAuthenticationFilter request: {}", request.getRequestURI());

        String authorization = request.getHeader("Authorization");

        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = authorization.split("Bearer ")[1];
        Map<String, Object> tokenData;

        try {
            tokenData = authTokenService.getDataFrom(accessToken);
        } catch (Exception e) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = (String) tokenData.get("username");
        List<String> authoritiesStringList = (List<String>) tokenData.get("authorities");
        List<SimpleGrantedAuthority> authorities = authoritiesStringList.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        User user = new User(username, "", authorities);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
