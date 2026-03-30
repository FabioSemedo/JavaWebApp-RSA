package org.vaadin.example.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.rsa.Manager;
import org.vaadin.example.rsa.RideSharingAppException;
import org.vaadin.example.rsa.match.Location;
import org.vaadin.example.rsa.match.RideMatch;
import org.vaadin.example.rsa.ride.RideRole;
import org.vaadin.example.rsa.user.Car;
import org.vaadin.example.rsa.user.User;
import org.vaadin.example.rsa.user.Users;
import org.vaadin.example.session.UserSession;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;

import java.util.*;

@Route(value = "ride", layout = UserLayout.class)
@PageTitle("Solicitação de viagem")
public class UserRideView extends VerticalLayout {
    private final User user;
    private final Manager manager;

    public UserRideView() throws RideSharingAppException {
        this.user = UserSession.getCurrentUser();
        this.manager = Manager.getInstance();


        if (user == null) {
            add(new Paragraph("Nenhum utilizador cadastrado."));
            UI.getCurrent().navigate("usuário");
            return;
        }


        H2 titulo = new H2("Criar/Solicitar Ride");


        //Modo de viagem
        RadioButtonGroup  rideRole = new RadioButtonGroup();
        rideRole.setLabel("Solicitar viagem como: ");
        rideRole.setItems(List.of("Passageiro", "Motorista"));


        //Caso motorista escolha um carro
        ComboBox<Car> placa = new ComboBox<>("Carro");
        placa.setItemLabelGenerator(Car::getPlate);
        placa.setItems(user.getCars());

        //Quanto o usuario como motorista quer cobrar pela viagem
        NumberField custo = new NumberField("Custo");
        custo.setPrefixComponent(new Span("$"));
        custo.setStep(0.01); //parecer dinheiro
        placa.setVisible(false);
        custo.setVisible(false);

        rideRole.addValueChangeListener(event -> {
            String value = (String) event.getValue();


            if ("Motorista".equals(value)) {
               placa.setVisible(true);
               custo.setVisible(true);

            }
            else if ("Passageiro".equals(value)) {
                placa.setVisible(false);
                placa.clear();
                custo.setVisible(false);
                custo.clear();
            }
        });

        NumberField fromX = new NumberField("Ponto de partida em x:");
        NumberField fromY = new NumberField("Ponto de partida em y:");
        NumberField toX = new NumberField("Ponto de chegada em x:");
        NumberField toY = new NumberField("Ponto de chegada em y:");

        FormLayout locais = new FormLayout(fromX, fromY, toX, toY);

        Button save = new Button("Salvar");

        save.addClickListener(event -> {
            Long rideId;
            RideMatch match;
            try {
                rideId = manager.addRide(
                        user.getNick(),
                        user.getKey(),
                        new Location(fromX.getValue(), fromY.getValue()),
                        new Location(toX.getValue(), toY.getValue()),
                        placa.getValue().getPlate(),
                        custo.getValue().floatValue());
                Set<RideMatch> setRidesMatchers = manager.updateRide(rideId, new Location(fromX.getValue(), fromY.getValue()));
                match = setRidesMatchers.iterator().next();
                Long matchId;
                if (match.getLeft().getId() == rideId) {
                    matchId = match.getRight().getId();
                }
                else {
                    matchId = match.getLeft().getId();
                }
                manager.acceptMatch(rideId, matchId);

            } catch (RideSharingAppException e) {
                throw new RuntimeException(e);
            }
            UI.getCurrent().navigate("combinacao");


        });




        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);



        add(titulo, rideRole, placa, custo, locais,  save);
    }
}
