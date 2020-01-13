package all.that.matters.repo;

public class FoodNotFoundException extends RepoException {

    public FoodNotFoundException() {
    }

    public FoodNotFoundException(String message) {
        super(message);
    }
}
