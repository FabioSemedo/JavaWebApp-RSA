package org.vaadin.example.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.charts.model.Label;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.example.rsa.Manager;
import org.vaadin.example.rsa.RideSharingAppException;
import org.vaadin.example.rsa.match.Matcher;
import org.vaadin.example.rsa.match.RideMatch;
import org.vaadin.example.rsa.ride.Ride;
import org.vaadin.example.rsa.user.User;
import org.vaadin.example.rsa.user.UserStars;
import org.vaadin.example.session.UserSession;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;

import java.util.List;

/**
 * Interface que permite User selecionar uma Ride.
 */
@Route(value = "combinacao")
@PageTitle(value = "Combinados")
public class UserMatchView extends VerticalLayout  {
    private final User user;
    private final Manager manager;
    private Long rideId;



    public UserMatchView() throws RideSharingAppException {
        this.user = UserSession.getCurrentUser();
        this.manager = Manager.getInstance();

        if (user == null) {
            add(new Paragraph("Nenhum utilizador cadastrado."));
            UI.getCurrent().navigate("");
            return;
        }



        H2 titulo = new H2("Conclução da viagem");

        /*
        //Manter as informações alinhadas
        HorizontalLayout linhas = new HorizontalLayout();
        linhas.setWidthFull();
        linhas.setHeightFull();

        // Perfil do utilizador atual
        VerticalLayout Esquerda = new VerticalLayout();
        Esquerda.setWidth("50%");
        Esquerda.setSpacing(true);

        // Perfil do utilizador combinado
        VerticalLayout Direita = new VerticalLayout();
        Direita.setWidth("50%");
        Direita.setSpacing(true);

        RideMatch myMatch = manager.matcher.matches.get(matchId);


        Esquerda.add(
                new H2( "Perfil do usuário"),
                new Paragraph("Nick: " + user.getNick() ),
                new Paragraph(("Nome: " + user.getName())),
                new Paragraph("Modo de viagem: " + rideId.getRideRole()), //QUERO O MEU RIDEROLE
                new Paragraph("ID:" + rideId),
                new Paragraph ("Número de estrelas: " + user.getAverage( rideId.getRideRole() ))
        );

        //AQUI USER DO COMPANHEIRO
        //AQUILO RIDEMATCH
        //AQUELE RIDE DO COMPANHEIRO
        //AQUI USER DO COMPANHEIRO
        //AQUILO RIDEMATCH
        //AQUELE RIDE DO COMPANHEIRO
        Ride otherUserRide = manager.updateRide(rideID, ).getFirst().getRide(rideId.getRideRole().other());
        User otherUser = otherUserRide.getUser();

        Direita.add(
                new H2( "Perfil do usuário"),
                new Paragraph("Nick: " + otherUser.getNick() ),
                new Paragraph(("Nome: " + otherUser.getName())),
                new Paragraph("Modo de viagem: " + otherUserRide.getRideRole() ),
                new Paragraph("ID:" + otherUserRide.getId()),
                new Paragraph ("Número de estrelas: " +
                        manager.getAverage(otherUser.getNick(), otherUserRide.getRideRole()) )

        );




        linhas.add(Esquerda, Direita);

        */


        //concluir corrida

        Button concluirCorrida = new Button("Concluir viagem");
        concluirCorrida.addClickListener(e -> {

            Dialog classificarCorrida = new Dialog();
            //Obrigar o usuário a classificar o companheiro
            classificarCorrida.setCloseOnEsc(false);
            classificarCorrida.setCloseOnOutsideClick(false);

            Label instrucao = new Label(
                    "Classifique o seu companheiro de viagem:");

            //Avaliar
            ComboBox<UserStars> estrelas = new ComboBox<>("Estrelas");
            estrelas.setItems(UserStars.values());
            estrelas.setPlaceholder("Escolha uma classificação");

            //Submição
            Button avaliar = new Button("Submeter avaliação");
            avaliar.addClickListener(event -> {
                manager.concludeRide(rideId, estrelas.getValue());
                classificarCorrida.close();

            } );
        });

        Button voltar = new Button("Voltar");
        voltar.addClickListener(e -> {

        });

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.add(concluirCorrida, voltar);


        add(titulo, buttons);




    }


}