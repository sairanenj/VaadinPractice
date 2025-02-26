package com.example.application.views.login;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Login")
@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

  public LoginView() {
    // Asetetaan näkymän koko ja asettelut
    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);

    // Kirjautumislomakkeen luonti
    var login = new LoginForm();
    login.setAction("login");

    // Lisätään tekstikomponentit näkymään (ohjeistus demotukseen)
    add(
      new H3("Peruskäyttäjä: user//user, ylläpito admin//admin"),
      new H4("(HUOM! Sivusto kehitysvaiheessa, nämä väliaikaisia)"),
      new Paragraph("user//user = ei näy adminin salasivua, admin//admin = näet adminin salasivun. Kirjaudu vain päälle jomminkummin."),
      login
    );
  }
}