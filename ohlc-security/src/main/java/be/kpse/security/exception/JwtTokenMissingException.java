package be.kpse.security.exception;

public class JwtTokenMissingException extends Exception{

    public JwtTokenMissingException(String msg) {
        super(msg);
    }
}
