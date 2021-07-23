package id.example.testingSpring.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// class untuk konfigurasi method api yang diizinkan untuk di set ke header responnya.
@Component
public class CorsConfig extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.setHeader("Access-Controll-Allow-Origin", "*");
        response.setHeader("Access-Controll-Allow-Methods", "GET, POST, PUT, DELETE");
        response.setHeader("Access-Controll-Allow-Headers", "Authorization, Content-Type, Cache-Control");
        response.setHeader("Access-Controll-Expose-Headers", "xsrf-token");

        filterChain.doFilter(request, response);
    }

}