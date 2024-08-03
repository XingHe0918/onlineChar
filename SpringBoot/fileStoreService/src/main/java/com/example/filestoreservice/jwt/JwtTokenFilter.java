package com.example.filestoreservice.jwt;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import org.springframework.web.filter.OncePerRequestFilter;

public class JwtTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");
        String requestURI = request.getRequestURI();

        // 跳过不需要验证的路径
        if (requestURI.startsWith("/files/preview") || requestURI.startsWith("/files/download")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (token != null && validateToken(token)) {
            filterChain.doFilter(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is invalid or expired");
        }
    }



    private boolean validateToken(String token) {
        // 示例Token解析逻辑，可以根据实际情况修改
        try {
            String[] tokenParts = token.split("\\.");
            if (tokenParts.length != 3) {
                return false;
            }

            String payload = new String(java.util.Base64.getDecoder().decode(tokenParts[1]));
            long exp = Long.parseLong(payload.replaceAll("^.*\"exp\":([0-9]+).*$", "$1"));

            return exp > (new Date().getTime() / 1000);
        } catch (Exception e) {
            return false;
        }
    }
}
