package pl.twojekursy.tracking;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class SeparatorFilter extends OncePerRequestFilter {
    private static final String SEPARATOR_HEADER_NAME = "separator";

    //zad dom. dodac filtr ktory bedzie przed naszym i jak w headerze znajdzie sie slowo 'separator' to ma wypisac linie
    // ------------------------------

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getHeader(SEPARATOR_HEADER_NAME)!=null) {
            log.info("------------------------------");
        }

        filterChain.doFilter(request, response);
    }
}
