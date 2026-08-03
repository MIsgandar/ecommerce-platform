package com.ecommerce.userservice.security;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.rmi.ServerException;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws java.io.IOException, IOException, ServletException {

        final String authHeader = request.getHeader(
                SecurityConstants.HEADER_NAME
        );

        final String jwt;

        final String username;

        /*
         * No Authorization header
         */
        if(authHeader == null ||
                    !authHeader.startsWith(
                            SecurityConstants.TOKEN_PREFIX
                    ))
        {

            filterChain.doFilter(request,response);
            return;

        }


        /*
         * Extract token
         */
        jwt = authHeader.substring(SecurityConstants.TOKEN_PREFIX.length());

        /*
         * Extract username from JWT
         */
        username = jwtService.extractUsername(jwt);

        /*
         * Authenticate only if user
         * is not already authenticated
         */
        if(username != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {

            UserDetails userDetails = userDetailsService
                    .loadUserByUsername(username);


            /*
             * Validate JWT
             */
            if (jwtService.isTokenValid(
                    jwt,
                    userDetails
            )){
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }
        }

        filterChain.doFilter(
                request,response
        );

    }
}
