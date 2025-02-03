package com.shadowshiftstudio.databasebridge.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class IpFilter extends OncePerRequestFilter {

    private static final Set<String> ALLOWED_IPS = new HashSet<>();

    static {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("allowed_ips.json");
            AllowedIps allowedIps = mapper.readValue(resource.getInputStream(), AllowedIps.class);
            ALLOWED_IPS.addAll(allowedIps.getAllowedIps());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load allowed IPs", e);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String remoteIp = request.getRemoteAddr();
        logger.info(remoteIp);
        if (ALLOWED_IPS.contains(remoteIp)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Access Denied");
            response.getWriter().flush();
        }
    }

    @Setter
    @Getter
    public static class AllowedIps {
        @JsonProperty("allowed_ips")
        private Set<String> allowedIps;
    }
}
