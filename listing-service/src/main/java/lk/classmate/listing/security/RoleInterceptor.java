package lk.classmate.listing.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

/**
 * Gatekeeper for /classes. Runs BEFORE the controller and before the request body is read.
 *
 *   GET  (browse, search, my-rating)      -> everyone
 *   POST /classes/{id}/ratings (rate)     -> STUDENT only
 *   POST / PUT / DELETE on classes        -> TEACHER only
 *
 * 401 = not logged in (authentication)
 * 403 = logged in, but wrong role (authorization)
 */
@Component
public class RoleInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public RoleInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {

        String method = request.getMethod();
        String path = request.getRequestURI();

        // Reading is open to everyone
        if ("GET".equalsIgnoreCase(method) || "OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        boolean isRating = path.endsWith("/ratings") || path.endsWith("/ratings/");
        String requiredRole = isRating ? "STUDENT" : "TEACHER";
        String action = isRating ? "rate classes" : "add, edit or delete classes";

        // 1. Authentication: who are you?
        Claims claims = jwtUtil.getClaims(request.getHeader("Authorization"));
        if (claims == null) {
            return reject(response, 401, "Please log in to " + action);
        }

        // 2. Authorization: are you allowed?
        String role = claims.get("role", String.class);
        if (!requiredRole.equalsIgnoreCase(role)) {
            String who = isRating ? "students" : "teachers";
            return reject(response, 403, "Only " + who + " can " + action);
        }

        return true;
    }

    private boolean reject(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\":\"" + message + "\"}");
        return false;
    }
}