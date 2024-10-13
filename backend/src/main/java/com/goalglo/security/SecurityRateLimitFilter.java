package com.goalglo.security;

import com.github.benmanes.caffeine.cache.LoadingCache;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class SecurityRateLimitFilter extends OncePerRequestFilter {

   private static final Logger logger = LoggerFactory.getLogger(SecurityRateLimitFilter.class);
   private static final int MAX_REQUESTS_PER_MINUTE = 60;

   private final LoadingCache<String, Integer> requestCountsPerIpAddress;

   public SecurityRateLimitFilter(LoadingCache<String, Integer> requestCountsPerIpAddress) {
      this.requestCountsPerIpAddress = requestCountsPerIpAddress;
   }

   @Override
   protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain
   ) throws ServletException, IOException {
      String clientIpAddress = getClientIP(request);
      int requests = requestCountsPerIpAddress.get(clientIpAddress);
      if (requests > MAX_REQUESTS_PER_MINUTE) {
         logger.warn("Rate limit exceeded for IP: {}", clientIpAddress);
         response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
         response.getWriter().write("Too many requests");
         return;
      }
      requestCountsPerIpAddress.put(clientIpAddress, requests + 1);
      filterChain.doFilter(request, response);
   }

   @NonNull
   private String getClientIP(@NonNull HttpServletRequest request) {
      String xfHeader = request.getHeader("X-Forwarded-For");
      if (xfHeader == null) {
         return request.getRemoteAddr();
      }
      return xfHeader.split(",")[0];
   }
}