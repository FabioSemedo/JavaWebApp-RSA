package org.vaadin.example.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.example.views.AddCarView;
import org.vaadin.example.views.RemoveCarsView;
import org.vaadin.example.views.UserAccountView;
import org.vaadin.example.views.UserRideView;


public class UserLayout extends AppLayout {

    public UserLayout() {
        createDrawerMenu();
    }

    private void createDrawerMenu() {
        VerticalLayout menu = new VerticalLayout();

        menu.setSizeFull();  // ocupa toda a altura do drawer

        // Define o menu como flexbox vertical e alinha os itens no fundo
        menu.getStyle().set("display", "flex");
        menu.getStyle().set("flex-direction", "column");
        menu.getStyle().set("justify-content", "flex-end");

        menu.add(
                new RouterLink("Login", UserAccessView.class),
                new RouterLink("Conta do Utilizador", UserAccountView.class),
                new RouterLink("Editar conta", UserEditorView.class),
                new RouterLink("Adicionar Carro", AddCarView.class),
                new RouterLink("Remover Carro", RemoveCarsView.class),
                //new RouterLink("Histórico de Rides", UserRideView.class),
                new RouterLink("Criar/Solicitar Ride", UserRideView.class),
                //new RouterLink("Parcerias de Ride", EmparelhamentoView.class),
                new RouterLink("Logout", LogoutView.class)

        );

        addToDrawer(menu);
    }
}
