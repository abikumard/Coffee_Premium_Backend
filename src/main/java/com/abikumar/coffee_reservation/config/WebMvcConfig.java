package com.abikumar.coffee_reservation.config;
 
import com.abikumar.coffee_reservation.constant.AppConstants;
import com.abikumar.coffee_reservation.security.TokenAuthInterceptor;
import com.abikumar.coffee_reservation.service.AuthTokenService;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
 
    private final AuthTokenService authTokenService;
 
    public WebMvcConfig(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }
 
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Any authenticated user (customer) can create a reservation or view their own bookings.
        registry.addInterceptor(new TokenAuthInterceptor(authTokenService, null))
                .addPathPatterns("/api/reservations", "/api/reservations/my")
                .order(1);
 
        // Only the admin role can view/approve/reject reservations.
        registry.addInterceptor(new TokenAuthInterceptor(authTokenService, AppConstants.ROLE_ADMIN))
                .addPathPatterns(
                        "/api/reservations/pending",
                        "/api/reservations/approved",
                        "/api/reservations/history",
                        "/api/reservations/*/approve",
                        "/api/reservations/*/reject")
                .order(1);
    }
 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns(
                        "http://localhost:*",
                        "https://coffee-premium-frontend.vercel.app",
                        "https://*.vercel.app")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}