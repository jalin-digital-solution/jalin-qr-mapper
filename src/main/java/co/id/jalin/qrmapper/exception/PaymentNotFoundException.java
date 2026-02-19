package co.id.jalin.qrmapper.exception;

/**
 * JWT exception as exception for all error occur when calculate JWT.
 * Default XX - XX XX
 */
public class PaymentNotFoundException extends GeneralException {

    public PaymentNotFoundException(){
        super();
    }

    public PaymentNotFoundException(String message){
        super(message);
    }

    public PaymentNotFoundException(Throwable throwable){
        super(throwable);
    }

    public PaymentNotFoundException(String message, Throwable throwable){
        super(message,throwable);
    }
}
