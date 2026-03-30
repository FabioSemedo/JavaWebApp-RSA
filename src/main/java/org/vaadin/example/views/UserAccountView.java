package org.vaadin.example.views;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.rsa.RideSharingAppException;
import org.vaadin.example.rsa.ride.RideRole;
import org.vaadin.example.rsa.user.Car;
import org.vaadin.example.rsa.user.User;
import org.vaadin.example.rsa.user.Users;
import org.vaadin.example.session.UserSession;
import com.vaadin.flow.component.button.Button;
import org.vaadin.example.views.UserLayout;


import javax.swing.*;
import java.awt.*;


@Route(value = "conta", layout = UserLayout.class)
@PageTitle("Conta do Utilizador")
public class UserAccountView extends VerticalLayout {
    private final User user;
    public UserAccountView() throws RideSharingAppException {
        this.user = UserSession.getCurrentUser();

        if (user == null) {
            add(new Paragraph("Nenhum utilizador cadastrado."));
            UI.getCurrent().navigate("");
            return;
        }



        H2 titulo = new H2("Bem-vindo, " + user.getNick() + "!");
        Paragraph nome = new Paragraph("Nome: " + user.getName());
        Paragraph key = new Paragraph("Chave: " + user.getKey());
        Paragraph preferencia = new Paragraph("Preferrer Match: " + user.getPreferredMatch());
        Paragraph viagens = new Paragraph("Número de viagens: " + user.getNumberRides());
        Paragraph motorista = new Paragraph("Número de viagens como motorista: " + user.getNumberRidesDriver());
        Paragraph passageiro = new Paragraph("Número de viagens como passageiro: " + user.getNumberRidesPassager());
        Paragraph estrelasMotorista = new Paragraph("Classificação como motorista: " + user.getAverage(RideRole.DRIVER));
        Paragraph estrelasPassageiro = new Paragraph("Classificação como passageiro: " + user.getAverage(RideRole.PASSENGER));
        Paragraph numberCars = new Paragraph("Número de carros registrados: " + user.getNumberCars());

        Grid<Car> carros = new Grid<>(Car.class);
        carros.setItems(user.getCars());
        carros.setColumns("plate", "make", "model", "color");

        Button logout = new Button("Logout");
        logout.addClickListener(event -> {
                    UserSession.clear();
                    getUI().ifPresent(ui -> ui.navigate("usuário"));
                });

        Button editar = new Button("Editar Conta", event -> {
            UI.getCurrent().navigate("editar-conta");
        });

        Button adicionarCarros = new Button("Adicionar Carros", event -> {
            UI.getCurrent().navigate("adicionar-carros");
        });

        Button removerCarros = new Button("Remover carros");
        removerCarros.addClickListener(e -> {
            UI.getCurrent().navigate("remover-carros");
        });

        FormLayout formLayout = new FormLayout(nome, key, preferencia, viagens, motorista, estrelasMotorista, passageiro,  estrelasPassageiro, numberCars);

        HorizontalLayout buttons = new HorizontalLayout(editar, adicionarCarros, removerCarros);

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        add(titulo, formLayout, carros, buttons, logout);


    }
}
