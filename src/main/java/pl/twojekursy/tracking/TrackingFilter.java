package pl.twojekursy.tracking;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class TrackingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(prepareRequestLog(request));
        long start = System.currentTimeMillis();

        filterChain.doFilter(request, response);

        long end = System.currentTimeMillis();
        log.info("Stop request:  {} {},status: {}, took {} ms",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                (end - start));
    }

    private static String prepareRequestLog(HttpServletRequest request) {
        StringBuilder requestLog = new StringBuilder("Start request ");

        requestLog.append(request.getMethod())
                .append(" ")
                .append(request.getRequestURI());

        if(request.getQueryString()!=null){
            requestLog.append("?").append(request.getQueryString());
        }

        return requestLog.toString();
    }

}
