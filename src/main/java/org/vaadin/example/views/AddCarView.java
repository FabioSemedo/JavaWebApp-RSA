package org.vaadin.example.views;



import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
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

import java.util.Set;

@Route(value = "adicionar-carros", layout = UserLayout.class)
@PageTitle("Adicionar Carros")
public class AddCarView extends VerticalLayout {

    private final User user;

    public AddCarView() {

        this.user = UserSession.getCurrentUser();

        if (user == null) {
            add(new Paragraph("Nenhum utilizador cadastrado."));
            UI.getCurrent().navigate("usuário");
            return;
        }

        H2 titulo = new H2("Adicionar Carro");

        //Carros do usuário
        Grid<Car> carros = new Grid<>(Car.class);
        carros.setItems(user.getCars());
        carros.setColumns("plate", "make", "model", "color");

        //adicionar carro
        TextField placa = new TextField("Matrícula");
        TextField marca = new TextField("Marca");
        TextField modelo = new TextField("Modelo");
        TextField cor = new TextField("Cor");
        placa.setVisible(false);
        marca.setVisible(false);
        modelo.setVisible(false);
        cor.setVisible(false);

        FormLayout carroInscricao = new FormLayout(placa, marca, modelo, cor);
        Button adicionarCarros = new Button("Adicionar carro");
        adicionarCarros.addClickListener(e -> {

            placa.setVisible(true);
            marca.setVisible(true);
            modelo.setVisible(true);
            cor.setVisible(true);
            String novaPlaca = placa.getValue().trim();
            String novaMarca = marca.getValue().trim();
            String novoModelo = modelo.getValue().trim();
            String novaCor = cor.getValue().trim();

            if (novaPlaca.isEmpty()) {
                Notification.show("Para adicionar um carro é obrigatório inserir a placa.");
                return;
            }
            else if (user.getCar(novaPlaca) != null) {
                Notification.show("Já existe um carro com esta placa.");
                return;
            }
            if (novaMarca.isEmpty()||novoModelo.isEmpty()||novaCor.isEmpty()) {
                Notification.show("Para adicionar um carro todos os campos devem ser preenchidos.");
                return;
            }
            Car car = new Car(novaPlaca, marca.getValue(), modelo.getValue(), cor.getValue());
            user.addCar(car);
            carros.setItems(user.getCars());
            placa.clear(); marca.clear(); modelo.clear(); cor.clear();

        });



        Button removerCarros = new Button("Remover carros");
        removerCarros.addClickListener(e -> {
            UI.getCurrent().navigate("remover-carros");
        });
        Button voltar = new Button("Voltar", event -> {
            UI.getCurrent().navigate("conta");
        });

        HorizontalLayout buttons = new HorizontalLayout(adicionarCarros,  removerCarros);
        add(titulo, carros, carroInscricao, buttons, voltar);





    }
}
