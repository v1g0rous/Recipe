package recipes.exception;

public class UserIsNotAuthorException extends RuntimeException {
    public UserIsNotAuthorException(String message) {
        super(message);
    }
}
