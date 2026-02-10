package co.id.jalin.qrmapper.exception;

/**
 * Exception when response timeout.
 * Default RC 68 - Suspend transaction
 */
public class WebClientResponseTimeoutException extends WebClientGeneralException {

    public WebClientResponseTimeoutException(String message){
        super(message);
    }

    public WebClientResponseTimeoutException(String message, Throwable throwable){
        super(message,throwable);
    }
}
