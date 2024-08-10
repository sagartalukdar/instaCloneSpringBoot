package com.react.Config;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenGeneratorFilter extends OncePerRequestFilter{

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
		if(authentication!=null) {
			SecretKey key=Keys.hmacShaKeyFor(SecurityContext.JWT_KEY.getBytes());
			String jwt=Jwts.builder()
					   .setIssuer("custom_jwt_issuer")
					   .setIssuedAt(new Date())
					   .claim("authorities", populateAuthority(authentication.getAuthorities()))
					   .claim("username", authentication.getName())
					   .setExpiration(new Date(new Date().getTime()+172800000))
					   .signWith(key).compact();
			response.setHeader(SecurityContext.HEADER, jwt);
		}
		filterChain.doFilter(request, response);
		
	}
	
	
	public String populateAuthority(Collection<? extends GrantedAuthority> collection) {
		Set<String>authorities=new HashSet<>();
		
		for(GrantedAuthority e:collection) {
			authorities.add(e.getAuthority());
		}
		return String.join(",", authorities);
	}
	
	protected boolean shouldNotFilter(HttpServletRequest req)throws ServletException {
		return !req.getServletPath().equals("/signIn");
	}

}
