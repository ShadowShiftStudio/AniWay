package com.shadowshiftstudio.aniwayauth.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class IpFilter implements Filter {

    private static final Set<String> ALLOWED_IPS = new HashSet<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("allowed_ips.json");
            AllowedIps allowedIps = mapper.readValue(resource.getInputStream(), AllowedIps.class);
            ALLOWED_IPS.addAll(allowedIps.getAllowedIps());
        } catch (IOException e) {
            throw new ServletException("Failed to load allowed IPs", e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String remoteIp = httpRequest.getRemoteAddr();

        if (ALLOWED_IPS.contains(remoteIp)) {
            chain.doFilter(request, response);
        } else {
            response.getWriter().write("Access Denied");
            response.getWriter().flush();
        }
    }

    @Override
    public void destroy() {
    }

    private static class AllowedIps {
        private Set<String> allowedIps;

        public Set<String> getAllowedIps() {
            return allowedIps;
        }

        public void setAllowedIps(Set<String> allowedIps) {
            this.allowedIps = allowedIps;
        }
    }
}