package co.id.jalin.qrmapper.exception;

/**
 * JWT exception as exception for all error occur when calculate JWT.
 * Default XX - XX XX
 */
public class HttpHeaderException extends GeneralException {

    public HttpHeaderException(){
        super();
    }

    public HttpHeaderException(String message){
        super(message);
    }

    public HttpHeaderException(Throwable throwable){
        super(throwable);
    }

    public HttpHeaderException(String message, Throwable throwable){
        super(message,throwable);
    }
}
