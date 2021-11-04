package ${package_base}.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class RequestLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain
    ) throws ServletException, IOException {
        long start = System.nanoTime();
        filterChain.doFilter(request, response);
        long stop = System.nanoTime();

        log.info("\"{}:{}\" {} in {}",
                request.getMethod(), request.getRequestURI(),
                response.getStatus(), getTimeTakenStr(start, stop));
    }

    private static String getTimeTakenStr(long start, long stop) {
        Long taken = (stop - start)/1000;
        if(taken < 100000) {
            Double msecs = ((double)taken/1000);
            return msecs.toString()+" msecs";
        } else {
            Double secs = ((double)taken/1000000);
            return secs.toString()+" secs";
        }
    }
}
