package all.that.matters.dao;

public class FoodNotFoundException extends RepoException {

    public FoodNotFoundException() {
    }

    public FoodNotFoundException(String message) {
        super(message);
    }
}
