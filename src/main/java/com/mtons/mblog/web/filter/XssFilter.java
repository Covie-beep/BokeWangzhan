package com.mtons.mblog.web.filter;

import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * XSS 过滤器
 */
public class XssFilter implements Filter {
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private static final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/dist/**",
            "/theme/**",
            "/storage/**",
            "/post/submit",
            "/admin/post/update",
            "/comment/submit"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (shouldExclude(httpRequest.getServletPath())) {
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(new XssHttpServletRequestWrapper(httpRequest), response);
    }

    private boolean shouldExclude(String path) {
        for (String pattern : EXCLUDE_PATHS) {
            if (PATH_MATCHER.match(pattern, path)) {
                return true;
            }
        }
        return false;
    }
}
