package com.example.application.views.admin;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import jakarta.annotation.security.RolesAllowed;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Admin")
@Route(value = "admin", layout = MainLayout.class) // Viewin reitti ja varmistetaan että mainlayout käytössä
@RolesAllowed("ADMIN")
@Menu(order = 6, icon = LineAwesomeIconUrl.SKULL_CROSSBONES_SOLID)
public class AdminView extends Composite<VerticalLayout> {

  public AdminView() {
    // Viewin asettelu ja komponentit
    VerticalLayout layoutColumn2 = new VerticalLayout();
    Paragraph textMedium = new Paragraph();
    HorizontalLayout layoutRow = new HorizontalLayout();
    Paragraph textMedium2 = new Paragraph();

    // Asetetaan pääasettelun tyyli
    getContent().setWidth("100%");
    getContent().getStyle().set("flex-grow", "1");

    // LayoutColumn2:n tyyli ja lisätään komponentit
    layoutColumn2.setWidth("100%");
    layoutColumn2.getStyle().set("flex-grow", "1");
    textMedium.setText("Vain admin näkee tämän! Jos et ole admin, olet pahoilla teillä.");
    textMedium.setWidth("100%");
    textMedium.getStyle().set("font-size", "var(--lumo-font-size-m)");
    layoutColumn2.add(textMedium);

    // Kuvan lisäys
    Image kotisivu = new Image("images/adminpage.png", "Kotisivu");
    kotisivu.setWidth("80%");
    kotisivu.getStyle().set("max-width", "400px");
    layoutColumn2.add(kotisivu);

    layoutRow.addClassName(Gap.MEDIUM);
    layoutRow.setWidth("100%");
    layoutRow.setHeight("min-content");
    textMedium2.setText("© 2025 Jaska - VAADIN");
    textMedium2.setWidth("100%");
    textMedium2.getStyle().set("font-size", "var(--lumo-font-size-m)");
    layoutRow.add(textMedium2);

    // Lisätään asettelut pääasetteluun
    getContent().add(layoutColumn2);
    getContent().add(layoutRow);
  }
}