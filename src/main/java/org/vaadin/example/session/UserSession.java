package org.vaadin.example.session;

import com.vaadin.flow.server.VaadinSession;
import org.vaadin.example.rsa.user.User;

public class UserSession {

    private static final String CURRENT_USER = "currentUser";

    public static void setCurrentUser(User user) {
        VaadinSession.getCurrent().setAttribute(CURRENT_USER, user);
    }

    public static User getCurrentUser() {
        return (User) VaadinSession.getCurrent().getAttribute(CURRENT_USER);
    }

    public static void clear() {
        VaadinSession.getCurrent().setAttribute(CURRENT_USER, null);
    }
}
