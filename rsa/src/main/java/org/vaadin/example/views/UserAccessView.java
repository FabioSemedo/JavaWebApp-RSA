package org.vaadin.example.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.example.rsa.RideSharingAppException;
import org.vaadin.example.rsa.user.User;
import org.vaadin.example.rsa.user.Users;
import org.vaadin.example.session.UserSession;

@PageTitle("Entrada")
@Route("")
public class UserAccessView extends VerticalLayout {
    private final TextField userNick = new TextField("Nick");
    private final TextField userName = new TextField("Name");
    private final PasswordField userKey = new PasswordField("Chave de autenticação");
    private final Button save = new Button("Conecte-se");
    private final NativeLabel feedbackLabel = new NativeLabel();

    public UserAccessView() {

        userKey.setVisible(false); //não aparece se o utilizador nao existir

        save.addClickListener(e -> {
            String nick = userNick.getValue().trim();
            String name = userName.getValue().trim();
            String key = userKey.getValue().trim();

            Users users;
            try {
                users = Users.getInstance();
            } catch (RideSharingAppException ex) {
                feedbackLabel.setText("Erro ao carregar dados de usuário.");
                ex.printStackTrace();
                return;
            }

            User user = users.getUser(nick);
            if (user == null) {

                try {
                    user = users.register(nick, name);
                } catch (RideSharingAppException ex) {
                    feedbackLabel.setText("Erro ao registrar usuário.");
                    ex.printStackTrace();
                    return;
                }
                if (user != null) {
                    feedbackLabel.setText("Usuário " + nick + " criado com sucesso! Seja bem-vindo!");
                    userKey.setValue(user.getKey());
                    userKey.setVisible(true);
                    UserSession.setCurrentUser(user);
                    UI.getCurrent().navigate("conta");
                } else {
                    feedbackLabel.setText("Nick inválido ou já em uso.");
                }
            } else {
                // Usuário já existe
                if (!user.getName().equals(name)) {
                    feedbackLabel.setText("Nome não coincide com o nick existente.");
                } else {
                    userKey.setVisible(true);
                    if (!key.isEmpty()) {
                        if (user.authenticate(key)) {
                            feedbackLabel.setText("Autenticação bem-sucedida!");
                            UserSession.setCurrentUser(user);
                            UI.getCurrent().navigate("conta");
                        } else {
                            feedbackLabel.setText("Chave incorreta.");
                        }
                    } else {
                        feedbackLabel.setText("Introduza a sua chave para autenticar.");
                    }
                }
            }
        });

        FormLayout form = new FormLayout(userNick, userName, userKey, save);
        form.setWidth("300px");


        VerticalLayout centralContainer = new VerticalLayout(form, feedbackLabel);
        centralContainer.setAlignItems(Alignment.CENTER);
        centralContainer.setJustifyContentMode(JustifyContentMode.CENTER);
        centralContainer.setSpacing(true);
        centralContainer.setPadding(true);
        centralContainer.setWidth(null);


        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add(centralContainer);
    }
}
