package com.example.rabbithell.infrastructure.security.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.rabbithell.domain.auth.domain.AuthUser;
import com.example.rabbithell.domain.auth.domain.MiniAuthUser;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

		String token = resolveToken(request);

		if (StringUtils.hasText(token) && jwtUtil.validateToken(token)) {
			Long userId = Long.parseLong(jwtUtil.extractSubject(token));
			String role = jwtUtil.extractRole(token);

			// 미니 토큰 구간
			if (!jwtUtil.hasCloverInfo(token)) {
				MiniAuthUser authUser = new MiniAuthUser(userId, role);
				UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);

			// 풀 토큰 구간
			} else {
				Long cloverId = jwtUtil.extractCloverId(token);
				String cloverName = jwtUtil.extractCloverName(token);
				AuthUser authUser = new AuthUser(userId, role, cloverId, cloverName);
				UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		log.info("토큰 있음, subject={}, role={}, cloverId={}, cloverName={}",
			jwtUtil.extractSubject(token),
			jwtUtil.extractRole(token),
			jwtUtil.extractCloverId(token),
			jwtUtil.extractCloverName(token)
		);

		filterChain.doFilter(request, response);
	}

    private String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

}
