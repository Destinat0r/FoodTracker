package all.that.matters.utils;

import all.that.matters.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class ContextUtils {
    public static User getPrincipal() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
