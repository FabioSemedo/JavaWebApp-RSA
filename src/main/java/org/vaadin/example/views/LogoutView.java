package org.vaadin.example.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.vaadin.example.session.UserSession;

@Route(value = "logout", layout = UserLayout.class)
public class LogoutView extends VerticalLayout {
    public LogoutView() {
        UserSession.clear();
        UI.getCurrent().navigate("");
    }
}

