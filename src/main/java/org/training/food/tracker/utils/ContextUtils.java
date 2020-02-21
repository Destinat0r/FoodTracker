package org.training.food.tracker.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.training.food.tracker.model.User;

public class ContextUtils {
    public static User getPrincipal() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
