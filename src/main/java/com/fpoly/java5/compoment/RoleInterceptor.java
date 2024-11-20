package com.fpoly.java5.compoment;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fpoly.java5.utils.Contants;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RoleInterceptor implements HandlerInterceptor {
    @SuppressWarnings("null")
    @Override
    public boolean preHandle(HttpServletRequest request,HttpServletResponse response, Object handler)
            throws Exception {
                Cookie[] cookies = request.getCookies();
                String userRole = null;
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals(Contants.COOKIE_USER_ROLE)) {
                            userRole = cookie.getValue();
                            break;
                        }
                    }
                }
        
                String requestURI = request.getRequestURI();
                if (requestURI.contains("/admin/")) {
                    if (userRole == null || !userRole.equals(Contants.ADMIN_ROLES.toString())) {
                        response.sendRedirect("/login?error=accessDenied");
                        return false;
                    }
                }
                if (requestURI.contains("/prodcuct-detail/add-to-cart")) {
                    if (userRole.equals(Contants.ADMIN_ROLES.toString())) {
                        response.sendRedirect("/login?error=accessDenied");
                        return false;
                    }
                }
                
                if (requestURI.contains("/user/")) {
                    if (userRole == null || userRole.equals(Contants.ADMIN_ROLES.toString())) {
                        response.sendRedirect("/login?error=accessDenied");
                        return false;
                    }
                }
    
                if (userRole != null) {
                    return true; 
                }
                response.sendRedirect("/login?error=accessDenied");
                return false;
    }
}