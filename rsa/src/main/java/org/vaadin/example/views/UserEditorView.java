package org.vaadin.example.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.tomcat.util.modeler.NotificationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.rsa.RideSharingAppException;
import org.vaadin.example.rsa.match.PreferredMatch;
import org.vaadin.example.rsa.ride.RideRole;
import org.vaadin.example.rsa.user.Car;
import org.vaadin.example.rsa.user.User;
import org.vaadin.example.rsa.user.Users;
import org.vaadin.example.session.UserSession;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.notification.Notification;

import java.util.Set;


@Route(value = "editar-conta", layout = UserLayout.class)
@PageTitle("Editar conta")
public class UserEditorView extends VerticalLayout {
    private final User user;
    public UserEditorView(){
        this.user = UserSession.getCurrentUser();

        if (user == null) {
            add(new Paragraph("Nenhum utilizador cadastrado."));
            UI.getCurrent().navigate("");
            return;
        }

        H2 conta = new H2("Editar conta do utilizador " + user.getNick());

        //nick do utilizador nao pode ser mudado
        TextField nick = new TextField("Nick");
        nick.setValue(user.getNick());
        nick.setReadOnly(true);

        //o nome do utilizador não pode ser mudado
        TextField nome = new TextField("Nome");
        nome.setValue(user.getName());
        nome.setReadOnly(true);


        //Chave do usuario
        PasswordField key = new PasswordField("Chave");
        key.setValue(user.getKey());
        key.setReadOnly(true);


        //Preferencias do usuário
        ComboBox<PreferredMatch> preferencia = new ComboBox<>("Preferência de Correspondência");
        preferencia.setItems(PreferredMatch.values());
        preferencia.setValue(user.getPreferredMatch());

        //Número de viagens
        NumberField viagens = new NumberField("Número de viagens");
        viagens.setValue((double)user.getNumberRides());
        viagens.setReadOnly(true);
        NumberField motorista = new NumberField("Número de viagens como motorista");
        motorista.setValue((double)user.getNumberRidesDriver());
        motorista.setReadOnly(true);
        NumberField passageiro = new NumberField("Número de viagens como passageiro");
        passageiro.setValue((double)user.getNumberRidesPassager());
        passageiro.setReadOnly(true);

        //Classificação do usuario
        NumberField estrelasMotorista = new NumberField("Classificação como motorista");
        estrelasMotorista.setValue((double)user.getAverage(RideRole.DRIVER));
        estrelasMotorista.setReadOnly(true);
        NumberField estrelasPassageiro = new NumberField("Classificação como passageiro");
        estrelasPassageiro.setValue((double)user.getAverage(RideRole.PASSENGER));
        estrelasPassageiro.setReadOnly(true);

        //Carros do usuario
        NumberField numberCars = new NumberField("Número de carros registrados");
        numberCars.setValue((double)user.getNumberCars());
        numberCars.setReadOnly(true);

        Grid<Car> carros = new Grid<>(Car.class);
        carros.setItems(user.getCars());
        carros.setColumns("plate", "make", "model", "color");



        Button adicionarCarros = new Button("Adicionar carros");
        adicionarCarros.addClickListener(e -> {
            UI.getCurrent().navigate("adicionar-carros");

        });

        Button removerCarros = new Button("Remover carros");
        removerCarros.addClickListener(e -> {
            UI.getCurrent().navigate("remover-carros");
        });


        //Botões
        Button salvar = new Button("Salvar alterações");
        Button cancelar = new Button("Cancelar alterações");

        salvar.addClickListener(e -> {
            PreferredMatch novaPreferencia = preferencia.getValue();
            if ((novaPreferencia != null) && (novaPreferencia != user.getPreferredMatch())) {
                user.setPreferredMatch(novaPreferencia);

            }


            Notification.show("Alterações guardadas.");
            UI.getCurrent().navigate("conta");

        });
        cancelar.addClickListener(e -> {
            Notification.show("Alterações canceladas.");
            UI.getCurrent().navigate("conta");
        });


        FormLayout formLayout = new FormLayout(nick, nome, key, preferencia, viagens,
                motorista, passageiro, estrelasMotorista,
                estrelasPassageiro, numberCars);

        HorizontalLayout carButtons = new HorizontalLayout(adicionarCarros, removerCarros);
        HorizontalLayout botoes = new HorizontalLayout( salvar, cancelar);

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(conta, formLayout, carros, carButtons, botoes);



    }
}
