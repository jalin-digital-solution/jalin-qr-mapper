package co.id.jalin.qrmapper.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * Web client general exception as parent for webclient exception.
 * Default RC 96 - System malfunction
 */
public class WebClientGeneralException extends NestedRuntimeException {

    public WebClientGeneralException(String message){
        super(message);
    }

    public WebClientGeneralException(String message, Throwable throwable){
        super(message,throwable);
    }
}
