package domain.telecom.v2.util;

public class NotUniqueException extends RuntimeException {
    
    public NotUniqueException(String msg) {
        super(msg);
    }

    public NotUniqueException() {
        this("");
    }
}
