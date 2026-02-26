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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static co.id.jalin.qrmapper.util.LoggingUtil.logResponseTime;
import static co.id.jalin.qrmapper.util.constant.GeneralConstant.START_IDX_ALTO_TRX_PATH;

@Order(4)
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
        var startTime = System.currentTimeMillis();

        if (request.getRequestURI().startsWith(transactionBasePathEsb)
                || request.getRequestURI().startsWith(transactionBasePathAlto,START_IDX_ALTO_TRX_PATH)
        ) {
            // Already wrapped at logging filter
            var requestWrapper = (MultiReadHttpServletRequest) request;
            var responseWrapper = (BufferedResponseWrapper) response;

            // Init trx log only then continue
            requestContext.setTransactionLog(TransactionLog.builder().build());
            chain.doFilter(requestWrapper,responseWrapper);

            requestContext.getTransactionLog().setTraceId(requestContext.getTraceId());
            requestContext.getTransactionLog().setApiService(request.getServletPath());
            requestContext.getTransactionLog().setLeg1(requestContext.getRequestBody());
            requestContext.getTransactionLog().setLeg4(responseWrapper.getContent());

            transactionLogService.saveTransactionLogAsync(requestContext.getTransactionLog());
            logResponseTime(startTime,this.getClass().getSimpleName(),"doFilterInternal()");
            return;
        }

        chain.doFilter(request,response);
        logResponseTime(startTime,this.getClass().getSimpleName(),"doFilterInternal()");
    }

}
