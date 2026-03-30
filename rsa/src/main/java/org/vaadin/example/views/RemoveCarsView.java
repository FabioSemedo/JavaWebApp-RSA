package org.vaadin.example.views;



import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dialog.Dialog;
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

@Route(value = "remover-carros", layout = UserLayout.class)
@PageTitle("Remover Carros")
public class RemoveCarsView extends VerticalLayout {

    private final User user;

    public RemoveCarsView() {

        this.user = UserSession.getCurrentUser();

        if (user == null) {
            add(new Paragraph("Nenhum utilizador cadastrado."));
            UI.getCurrent().navigate("usuário");
            return;
        }

        H2 titulo = new H2("Retirar carros registrados");

        //Carros do usuário
        Grid<Car> carros = new Grid<>(Car.class);
        carros.setItems(user.getCars());
        carros.setColumns("plate", "make", "model", "color");



        carros.setSelectionMode(Grid.SelectionMode.MULTI); // Muito importante!

        Button removerCarros = new Button("Remover carros selecionados");
        removerCarros.addClickListener(e -> {
            Set<Car> selectedCars = carros.asMultiSelect().getSelectedItems();

            if (selectedCars.isEmpty()) {
                Notification.show("Nenhum carro foi selecionado.");
                return;
            }

            Dialog confirmDialog = new Dialog();
            confirmDialog.setHeaderTitle("Confirmar remoção");

            VerticalLayout dialogLayout = new VerticalLayout();
            dialogLayout.add(new Paragraph("Tens a certeza que queres remover " + selectedCars.size() + " carro(s)?"));

            Button confirmar = new Button("Remover", event -> {
                selectedCars.forEach(car -> user.deleteCar(car.getPlate()));
                carros.setItems(user.getCars());
                Notification.show("Carro(s) removido(s).");
                confirmDialog.close();
            });

            Button cancelar = new Button("Cancelar", event -> confirmDialog.close());

            HorizontalLayout buttons = new HorizontalLayout(confirmar, cancelar);
            dialogLayout.add(buttons);
            confirmDialog.add(dialogLayout);
            confirmDialog.open();
        });

        Button voltar = new Button("Voltar", event -> {
            UI.getCurrent().navigate("conta");
        });

        HorizontalLayout buttons = new HorizontalLayout(removerCarros, voltar);
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(titulo, carros, buttons);





    }
}
