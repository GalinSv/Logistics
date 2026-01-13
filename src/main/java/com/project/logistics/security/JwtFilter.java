//package com.project.logistics.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.List;
////TODO
//@Component
//@RequiredArgsConstructor
//public class JwtFilter extends OncePerRequestFilter {
//
//    private final JwtUtil jwtUtil;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        if (request.getServletPath().equals("/auth/login")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String header = request.getHeader("Authorization");
//
//        if (header == null || !header.startsWith("Bearer ")) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        String token = header.substring(7);
//
//        if (!jwtUtil.isValid(token)) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        Integer userId = jwtUtil.getUserId(token);
//        String role = jwtUtil.getRole(token);
//
//        var auth = new UsernamePasswordAuthenticationToken(
//                userId,
//                null,
//                List.of(new SimpleGrantedAuthority("ROLE_" + role))
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(auth);
//        filterChain.doFilter(request, response);
//    }}