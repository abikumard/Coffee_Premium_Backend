package com.abikumar.coffee_reservation.security;

import com.abikumar.coffee_reservation.constant.AppConstants;
import com.abikumar.coffee_reservation.entity.AuthToken;
import com.abikumar.coffee_reservation.exception.UnauthorizedException;
import com.abikumar.coffee_reservation.service.AuthTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Validates the "Authorization: Bearer <token>" header on protected endpoints
 * and enforces role based access (CUSTOMER vs ADMIN).
 */
public class TokenAuthInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TokenAuthInterceptor.class);

    public static final String REQUEST_ATTR_EMAIL = "authEmail";
    public static final String REQUEST_ATTR_ROLE = "authRole";

    private final AuthTokenService authTokenService;
    private final String requiredRole;

    public TokenAuthInterceptor(AuthTokenService authTokenService, String requiredRole) {
        this.authTokenService = authTokenService;
        this.requiredRole = requiredRole;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Browsers send a CORS "preflight" OPTIONS request before the real GET/POST/PUT call
        // whenever a custom header (like Authorization) is used. That preflight never carries
        // an Authorization header, so it must be allowed through untouched — otherwise the
        // preflight itself gets rejected as 401 and the browser blocks the real request with
        // a generic "Failed to fetch" / CORS error, even though the real request would have
        // had a perfectly valid token.
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String header = request.getHeader(AppConstants.AUTHORIZATION_HEADER);

        if (header == null || !header.startsWith(AppConstants.BEARER_PREFIX)) {
            log.warn("Missing or malformed Authorization header for {}", request.getRequestURI());
            throw new UnauthorizedException("Please login to continue.");
        }

        String token = header.substring(AppConstants.BEARER_PREFIX.length());
        AuthToken authToken = authTokenService.validateToken(token);

        if (requiredRole != null && !requiredRole.equals(authToken.getRole())) {
            log.warn("Role mismatch for {}: required={} actual={}", request.getRequestURI(), requiredRole, authToken.getRole());
            throw new UnauthorizedException("You are not authorized to perform this action.");
        }

        request.setAttribute(REQUEST_ATTR_EMAIL, authToken.getEmail());
        request.setAttribute(REQUEST_ATTR_ROLE, authToken.getRole());
        return true;
    }
}
