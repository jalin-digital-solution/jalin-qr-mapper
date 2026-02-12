package co.id.jalin.qrmapper.filter;

import co.id.jalin.qrmapper.configuration.wrapper.BufferedResponseWrapper;
import co.id.jalin.qrmapper.configuration.wrapper.MultiReadHttpServletRequest;
import co.id.jalin.qrmapper.context.RequestContext;
import co.id.jalin.qrmapper.entity.TransactionLog;
import co.id.jalin.qrmapper.service.transaction.TransactionLogService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Order(4)
@Log4j2
@Component
@RequiredArgsConstructor
public class TransactionLogFilter extends OncePerRequestFilter {

    private final RequestContext requestContext;
    private final TransactionLogService transactionLogService;

    @Value("${api.path.esb.qr}")
    private String transactionBasePathEsb;
    @Value("${api.path.alto.qr}")
    private String transactionBasePathAlto;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain)
            throws ServletException, IOException {

        if (request.getRequestURI().contains(transactionBasePathEsb) || request.getRequestURI().contains(transactionBasePathAlto)) {
            var requestWrapper = new MultiReadHttpServletRequest(request);
            var responseWrapper = new BufferedResponseWrapper(response);

            // Init trx log only then continue
            requestContext.setTransactionLog(TransactionLog.builder().build());
            chain.doFilter(requestWrapper,responseWrapper);

            
            requestContext.getTransactionLog().setTraceId(requestContext.getTraceId());
            requestContext.getTransactionLog().setApiService(request.getServletPath());

            transactionLogService.saveTransactionLogAsync(requestContext.getTransactionLog());
            return;
        }

        chain.doFilter(request,response);
    }

}
