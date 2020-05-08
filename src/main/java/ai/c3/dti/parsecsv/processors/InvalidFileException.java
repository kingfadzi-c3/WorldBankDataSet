package ai.c3.dti.parsecsv.processors;

public class InvalidFileException extends RuntimeException {

    public InvalidFileException(String message) {
        super(message);
    }
    
    public InvalidFileException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public InvalidFileException(Throwable cause) {
        super(cause);
    }
    
}
