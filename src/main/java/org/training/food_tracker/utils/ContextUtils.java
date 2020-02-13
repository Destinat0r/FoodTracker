package org.training.food_tracker.utils;

import org.training.food_tracker.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class ContextUtils {
    public static User getPrincipal() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
