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
import org.vaadin.example.rsa.Manager;
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



public class MatchPerfilView extends VerticalLayout {
    private final User user;
    private final Manager manager;
    public MatchPerfilView() throws RideSharingAppException {
        this.user = UserSession.getCurrentUser();
        this.manager = Manager.getInstance();


        if (user == null) {
            add(new Paragraph("Nenhum utilizador cadastrado."));
            UI.getCurrent().navigate("");
            return;
        }



        H2 titulo = new H2("Bem-vindo, " + user.getNick() + "!");
        Paragraph nome = new Paragraph("Nome: " + user.getName());
        Paragraph preferencia = new Paragraph("Preferrer Match: " + user.getPreferredMatch());
        Paragraph viagens = new Paragraph("Número de viagens: " + user.getNumberRides());
        Paragraph motorista = new Paragraph("Número de viagens como motorista: " + user.getNumberRidesDriver());
        Paragraph passageiro = new Paragraph("Número de viagens como passageiro: " + user.getNumberRidesPassager());
        Paragraph estrelasMotorista = new Paragraph("Classificação como motorista: " + user.getAverage(RideRole.DRIVER));
        Paragraph estrelasPassageiro = new Paragraph("Classificação como passageiro: " + user.getAverage(RideRole.PASSENGER));
        Paragraph numberCars = new Paragraph("Número de carros registrados: " + user.getNumberCars());

        //Car carroViagem



        FormLayout formLayout = new FormLayout(nome,  preferencia, viagens, motorista, estrelasMotorista, passageiro,  estrelasPassageiro, numberCars);

        Button voltar = new Button("Voltar");
        voltar.addClickListener(e -> {
            UI.getCurrent().navigate("combinacao");
        });

        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);

        add(titulo, formLayout, voltar);


    }
}
