package org.training.food.tracker.utils;

import org.training.food.tracker.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class ContextUtils {
    public static User getPrincipal() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
