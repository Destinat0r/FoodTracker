package all.that.matters.repo;

import all.that.matters.model.User;

import java.util.function.Supplier;

public class UserNotFoundException extends RepoException implements Supplier<User> {

    public UserNotFoundException() {
    }

    public UserNotFoundException(String message) {
        super(message);
    }

    @Override public User get() {
        return null;
    }
}
