package all.that.matters.repo;

public class UserExistsException extends Exception {

    public UserExistsException() {
    }

    public UserExistsException(String message) {
        super(message);
    }
}
