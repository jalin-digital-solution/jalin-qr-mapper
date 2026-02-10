package co.id.jalin.qrmapper.controller.advice;

import co.id.jalin.qrmapper.dto.error.DefaultErrorAttribute;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;


@Log4j2
@Order(10)
@RestControllerAdvice
public class GeneralExceptionAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({Exception.class})
    public DefaultErrorAttribute exception(Exception ex, HttpServletRequest request) {
        log.error("GeneralExceptionAdvice::exception message {}", ex.getMessage());
        log.error("GeneralExceptionAdvice::exception Error", ex);
        return DefaultErrorAttribute.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(request.getServletPath())
                .message(ex.getLocalizedMessage())
                .timestamp(LocalDateTime.now().toString()).build();
    }

}
