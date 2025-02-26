package com.example.application.views.pelit;

import com.example.application.data.Pelit;
import com.example.application.repositories.PelitRepository;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.MaxWidth;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;

import jakarta.annotation.security.PermitAll;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.List;

@PageTitle("Pelit")
@Route(value = "pelit", layout = MainLayout.class)  
@PermitAll
@Menu(order = 3, icon = LineAwesomeIconUrl.TH_LIST_SOLID)
public class PelitView extends Main {

  private OrderedList imageContainer;

  public PelitView(PelitRepository pelitRepository) {
    // Käyttöliittymän rakennusmetodi
    constructUI();

    // Haetaan pelit tietokannasta ja lisätään ne viewiin
    List<Pelit> pelitList = pelitRepository.findAll();
    for (Pelit peli : pelitList) {
      imageContainer.add(new PelitViewCard(peli, pelitRepository));
    }

    // Sama tuttu "footer"
    Div footer = new Div();
    footer.setText("(c) Jaska 2025 - VAADIN");
    footer.getStyle().set("text-align", "left");
    footer.getStyle().set("padding-left", "1rem");
    add(footer);
  }

  // Rakennetaan käyttöliittymä
  private void constructUI() {
    addClassNames("pelit-view");
    addClassNames(MaxWidth.SCREEN_LARGE, Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

    // Luodaan pääkontaineri
    HorizontalLayout container = new HorizontalLayout();
    container.addClassNames(AlignItems.CENTER, JustifyContent.BETWEEN);

    // Luodaan header-kontaineri
    VerticalLayout headerContainer = new VerticalLayout();
    H2 header = new H2("LAUTAPELIT");
    header.addClassNames(Margin.Bottom.NONE, Margin.Top.XLARGE, FontSize.XXXLARGE);
    Paragraph description = new Paragraph("Yrityksen käytössä olevat viralliset pelit oppimiseen.");
    description.addClassNames(Margin.Bottom.XLARGE, Margin.Top.NONE, TextColor.SECONDARY);
    headerContainer.add(header, description);

    // Luodaan kuva-kontaineri
    imageContainer = new OrderedList();
    imageContainer.addClassNames(Gap.MEDIUM, Display.GRID, ListStyleType.NONE, Margin.NONE, Padding.NONE);

    // Lisätään kontainerit näkymään
    container.add(headerContainer);
    add(container, imageContainer);
  }
}
