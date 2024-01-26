package vttp2023.batch4.paf.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("error");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
    
}

