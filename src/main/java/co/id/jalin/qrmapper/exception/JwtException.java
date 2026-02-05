package co.id.jalin.qrmapper.exception;

/**
 * JWT exception as exception for all error occur when calculate JWT.
 * Default XX - XX XX
 */
public class JwtException extends GeneralException {

    public JwtException(){
        super();
    }

    public JwtException(String message){
        super(message);
    }

    public JwtException(Throwable throwable){
        super(throwable);
    }

    public JwtException(String message, Throwable throwable){
        super(message,throwable);
    }
}
