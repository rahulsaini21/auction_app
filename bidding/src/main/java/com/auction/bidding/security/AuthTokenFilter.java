// package com.auction.bidding.security;

// import jakarta.servlet.FilterChain;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Component;
// import org.springframework.web.filter.OncePerRequestFilter;

// import com.auction.bidding.client.AuthServiceClient;

// import java.io.IOException;

// @Component
// public class AuthTokenFilter extends OncePerRequestFilter {

//     private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    
//     @Autowired
//     private AuthServiceClient authServiceClient;

//     @Override
//     protected void doFilterInternal(HttpServletRequest request, 
//                                   HttpServletResponse response, 
//                                   FilterChain filterChain) throws ServletException, IOException {
//         try {
//             String authHeader = request.getHeader("Authorization");
            
//             if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                 String token = authHeader.substring(7);
                
//                 // Validate token with auth service
//                 if (!authServiceClient.validateToken(token)) {
//                     logger.warn("Invalid JWT token");
//                     response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
//                     return;
//                 }
//             }
//         } catch (Exception e) {
//             logger.error("Cannot set user authentication", e);
//         }

//         filterChain.doFilter(request, response);
//     }
// }