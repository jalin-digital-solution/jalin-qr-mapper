package co.id.jalin.qrmapper.filter;

import co.id.jalin.qrmapper.configuration.wrapper.BufferedResponseWrapper;
import co.id.jalin.qrmapper.configuration.wrapper.MultiReadHttpServletRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static co.id.jalin.qrmapper.util.constant.GeneralConstant.*;


@Log4j2
@Component
@RequiredArgsConstructor
public class LoggingFilter extends OncePerRequestFilter {

    private final Tracer tracer;
    private final ObjectMapper objectMapper;

    // Exclude paths that don't need logging (optional)
    private final List<String> EXCLUDE_PATHS = Arrays.asList(
            "/health", "/actuator", "/favicon.ico"
    );

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain)
            throws IOException, ServletException {

        String traceId = tracer.currentSpan() != null ? Objects.requireNonNull(tracer.currentSpan()).context().traceId() : DOUBLE_DASH;
        response.setHeader(X_TRACE_ID, traceId);

        String path = request.getRequestURI();
        // Skip logging for excluded paths
        if (shouldExclude(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Wrap request and response for content caching
        long startTime = System.currentTimeMillis();
        var requestWrapper = new MultiReadHttpServletRequest(request);
        var responseWrapper = new BufferedResponseWrapper(response);

        var queryString = request.getQueryString();
        var requestBody = handlePostMethod(requestWrapper);

        var mapHeaderRequest = Collections.list(requestWrapper.getHeaderNames())
                .stream().collect(Collectors.toMap(s -> s, requestWrapper::getHeader));
        log.info("RequestServlet with Remote Host {} {}", request.getRemoteHost(), request.getServletPath());
        log.info("RequestHeader {}", objectMapper.writeValueAsString(mapHeaderRequest));
        log.info("RequestParams {}", queryString != null ? queryString : "None");
        log.info("RequestBody {}",requestBody);
        chain.doFilter(requestWrapper, responseWrapper);
        var mapHeaderResponse = responseWrapper.getHeaderNames()
                .stream().collect(Collectors.toMap(s -> s, responseWrapper::getHeader, (exist, replace) -> replace));
        log.info("ResponseStatus {}", responseWrapper.getStatus());
        log.info("ResponseHeader {}", objectMapper.writeValueAsString(mapHeaderResponse));
        log.info("ResponseBody {}", responseWrapper.getContent());

        long duration = System.currentTimeMillis() - startTime;
        log.info("ResponseTime {}ms",duration);
    }

    private String handlePostMethod(HttpServletRequest requestWrapper) throws IOException {
        if (HttpMethod.POST.name().equals(requestWrapper.getMethod()) && Objects.nonNull(requestWrapper.getContentType())) {
            return requestWrapper.getReader().lines().collect(Collectors.joining(EMPTY_STRING));
        }
        return EMPTY_STRING;
    }

    private boolean shouldExclude(String path) {
        return EXCLUDE_PATHS.stream().anyMatch(path::contains);
    }
}