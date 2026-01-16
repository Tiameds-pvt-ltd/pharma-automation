package com.pharma.filter;

import com.pharma.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    private static final Set<String> JWT_SKIP_PREFIXES = Set.of(
            "/auth/login",
            "/auth/loginOtp",        // ✅ covers /auth/loginOtp/**
            "/auth/register",
            "/auth/refresh"
    );

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws ServletException, IOException {

        String path = request.getServletPath();
//        System.out.println("🔍 JwtFilter HIT | Path = " + path);

        if (JWT_SKIP_PREFIXES.stream().anyMatch(path::startsWith)) {
            chain.doFilter(request, response);
            return;
        }

        String jwt = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

//        System.out.println("🍪 AccessToken cookie = " + (jwt != null));

        if (jwt == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            String username = jwtUtil.extractUsername(jwt);
            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            if (!jwtUtil.validateAccessToken(jwt, userDetails.getUsername())) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(authToken);

//            System.out.println("✅ Authentication SUCCESS for user: " + username);

        } catch (Exception e) {
//            System.out.println("⛔ Token invalid or expired → 401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        chain.doFilter(request, response);
    }
}


//@Component
//public class JwtFilter extends OncePerRequestFilter {
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain chain
//    ) throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.getWriter().write("Authorization header is not supported. Use cookies.");
//            return;
//        }
//
//        String jwt = null;
//
//        // 🔐 Read accessToken from cookie
//        if (request.getCookies() != null) {
//            for (Cookie cookie : request.getCookies()) {
//                if ("accessToken".equals(cookie.getName())) {
//                    jwt = cookie.getValue();
//                    break;
//                }
//            }
//        }
//
//        // No token → continue (public endpoints)
//        if (jwt == null) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        // Already authenticated → continue
//        if (SecurityContextHolder.getContext().getAuthentication() != null) {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        try {
//            String username = jwtUtil.extractUsername(jwt);
//
//            // 🔥 EXPIRED → 401 (frontend will refresh)
//            if (jwtUtil.isTokenExpired(jwt)) {
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                return;
//            }
//
//            UserDetails userDetails =
//                    userDetailsService.loadUserByUsername(username);
//
//            if (jwtUtil.validateAccessToken(jwt, userDetails.getUsername())) {
//
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(
//                                userDetails,
//                                null,
//                                userDetails.getAuthorities()
//                        );
//
//                authToken.setDetails(
//                        new WebAuthenticationDetailsSource()
//                                .buildDetails(request)
//                );
//
//                SecurityContextHolder.getContext()
//                        .setAuthentication(authToken);
//            }
//
//        } catch (Exception e) {
//            // 🔥 INVALID TOKEN → 401
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//
//        chain.doFilter(request, response);
//    }
//}

