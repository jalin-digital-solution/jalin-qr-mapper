package co.id.jalin.qrmapper.exception;

/**
 * JWT exception as exception for all error occur when calculate JWT.
 * Default XX - XX XX
 */
public class SignatureException extends HttpHeaderException {

    public SignatureException(){
        super();
    }

    public SignatureException(String message){
        super(message);
    }

    public SignatureException(Throwable throwable){
        super(throwable);
    }

    public SignatureException(String message, Throwable throwable){
        super(message,throwable);
    }
}
