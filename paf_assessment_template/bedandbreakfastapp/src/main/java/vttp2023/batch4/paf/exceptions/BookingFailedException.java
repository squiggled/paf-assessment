package vttp2023.batch4.paf.exceptions;

public class BookingFailedException extends RuntimeException{
    public BookingFailedException() {
        super("error");
    }

    public BookingFailedException(String message) {
        super(message);
    }
    
}

