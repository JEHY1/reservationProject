package com.example.travel.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomRequestCacheFilter extends OncePerRequestFilter {

    private final RequestCache requestCache = new HttpSessionRequestCache();

    //url 호출을 기록(로그인후 바로 이전페이지로 가기 위함)
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        // history 저장에 포함하지 않을 요청들
        if (!requestURI.equals("/login") && !requestURI.startsWith("/login/") && !requestURI.equals("/checkLogin") && !requestURI.startsWith("/css") && !requestURI.startsWith("/js") && !requestURI.startsWith("/images") && !requestURI.startsWith("/join") && !requestURI.startsWith("/favicon.ico") && !requestURI.startsWith("/find")) {
            requestCache.saveRequest(request, response);
        }

        filterChain.doFilter(request, response);
    }
}
