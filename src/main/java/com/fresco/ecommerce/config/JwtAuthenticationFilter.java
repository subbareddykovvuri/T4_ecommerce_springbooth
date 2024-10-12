package com.fresco.ecommerce.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fresco.ecommerce.service.UserAuthService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
    private JwtUtil jwtUtil;
	
	@Autowired
	private UserAuthService userDetails;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String header = request.getHeader("JWT");
		String token = null;
		String username = null;
		UserDetails user = null;
		
		if (header != null && header.startsWith("Bearer")) {
			token = header.substring(7);
			username = jwtUtil.extractUsername(token);
		}
		
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			user = userDetails.loadUserByUsername(username);
			
			if (user != null && jwtUtil.validateToken(token,user)) {
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
				auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		filterChain.doFilter(request, response);
		
//        if (token != null) {
//            try {
//                jwtUtil.validateToken(token);
//                // Optionally, set authentication context (if required by the system)
//            } catch (Exception e) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
//                return;
//            }
//        }
//        filterChain.doFilter(request, response);
	}
}
