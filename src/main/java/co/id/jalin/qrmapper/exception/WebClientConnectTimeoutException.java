package co.id.jalin.qrmapper.exception;

/**
 * Exception when connect timeout.
 * Default RC 91 - Link down
 */
public class WebClientConnectTimeoutException extends WebClientGeneralException {

    public WebClientConnectTimeoutException(String message){
        super(message);
    }

    public WebClientConnectTimeoutException(String message, Throwable throwable){
        super(message,throwable);
    }
}
