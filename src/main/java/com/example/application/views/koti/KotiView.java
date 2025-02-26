package com.example.application.views.koti;

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

import jakarta.annotation.security.PermitAll;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Koti")
@Route(value = "koti", layout = MainLayout.class)
@PermitAll
@Menu(order = 0, icon = LineAwesomeIconUrl.HOME_SOLID)
public class KotiView extends Composite<VerticalLayout> {

  public KotiView() {
    // Luodaan layoutit ja komponentit
    VerticalLayout layoutColumn2 = new VerticalLayout();
    Paragraph textMedium = new Paragraph();
    HorizontalLayout layoutRow = new HorizontalLayout();
    Paragraph textMedium2 = new Paragraph();

    getContent().setWidth("100%");
    getContent().getStyle().set("flex-grow", "1");

    layoutColumn2.setWidth("100%");
    layoutColumn2.getStyle().set("flex-grow", "1");

    // Tervetulotekstin asettelut
    textMedium.setText("Tervetuloa käyttämään peliapuriappia. Yläpuolelta pääset navigoimaan eri osioihin.");
    textMedium.setWidth("100%");
    textMedium.getStyle().set("font-size", "var(--lumo-font-size-m)");

    layoutRow.addClassName(Gap.MEDIUM);
    layoutRow.setWidth("100%");
    layoutRow.setHeight("min-content");

    // Kömpelö footer
    textMedium2.setText("© 2025 Jaska - VAADIN");
    textMedium2.setWidth("100%");
    textMedium2.getStyle().set("font-size", "var(--lumo-font-size-m)");

    // Luodaan ja asetellaan kotisivukova.png
    Image kotisivu = new Image("images/kotisivukuva.png", "Kotisivu");
    kotisivu.setWidth("80%");
    kotisivu.getStyle().set("max-width", "700px");

    // Lisätään komponentit layoutteihin
    getContent().add(layoutColumn2);
    layoutColumn2.add(textMedium, kotisivu);
    getContent().add(layoutRow);
    layoutRow.add(textMedium2);
  }
}