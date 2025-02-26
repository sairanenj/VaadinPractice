package com.example.application.views.pelit;

import org.springframework.orm.ObjectOptimisticLockingFailureException;

import com.example.application.data.Pelit;
import com.example.application.repositories.PelitRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.Background;
import com.vaadin.flow.theme.lumo.LumoUtility.BorderRadius;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.JustifyContent;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;

public class PelitViewCard extends ListItem {

  public PelitViewCard(Pelit peli, PelitRepository pelitRepository) {
    // Asetetaan kortille tyylit
    addClassNames(Background.CONTRAST_5, Display.FLEX, FlexDirection.COLUMN, AlignItems.START,
      Padding.MEDIUM, BorderRadius.LARGE);

    // Luodaan div elementti kuvalle
    Div div = new Div();
    div.addClassNames(Background.CONTRAST, Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER,
      Margin.Bottom.MEDIUM, Overflow.HIDDEN, BorderRadius.MEDIUM, Width.FULL);
    div.setHeight("100%");

    // Luodaan ja asetetaan kuva
    Image image = new Image();
    image.setWidth("100%");
    image.setSrc(peli.getPeliurl());
    image.setAlt(peli.getPelititle());

    div.add(image);

    // Luodaan ja asetetaan otsikot/tekstit
    Span header = new Span();
    header.addClassNames(FontSize.XLARGE, FontWeight.SEMIBOLD);
    header.setText(peli.getPelititle());

    Span subtitleSpan = new Span();
    subtitleSpan.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
    subtitleSpan.setText(peli.getPelisubtitle());

    Paragraph description = new Paragraph(peli.getPelidesctext());
    description.addClassName(Margin.Vertical.MEDIUM);

    Span badge = new Span();
    badge.getElement().setAttribute("theme", "badge");
    badge.setText(peli.getPelibadgetext());

    // Luodaan muokkauspainike
    Button editButton = new Button(new Icon(VaadinIcon.EDIT));
    editButton.addClickListener(e -> {
      // Luodaan dialogi muokkausta varten
      Dialog dialog = new Dialog();
      TextField textField = new TextField("Muistiinpanojen muokkaus:");
      textField.setValue(badge.getText());
      Button saveButton = new Button("Tallenna", event -> {
        try {
          badge.setText(textField.getValue());
          peli.setPelibadgetext(textField.getValue());
          pelitRepository.save(peli);
          dialog.close();
        } catch (ObjectOptimisticLockingFailureException ex) {
          Notification.show(
            "Virhe dataa muokatessa. Kokeile vaikka päivittää tai yritä uudestaan.",
            3000, Notification.Position.MIDDLE)
            .addThemeVariants(NotificationVariant.LUMO_ERROR);
          dialog.close();
        }
      });
      Button clearButton = new Button("Tyhjennä", event -> {
        textField.clear();
      });
      Button closeButton = new Button(new Icon(VaadinIcon.CLOSE), event -> {
        dialog.close();
      });
      HorizontalLayout buttonsLayout = new HorizontalLayout(saveButton, clearButton, closeButton);
      buttonsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
      buttonsLayout.setWidthFull();

      VerticalLayout dialogLayout = new VerticalLayout(textField, buttonsLayout);
      dialogLayout.setPadding(false);
      dialogLayout.setSpacing(false);
      dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

      dialog.add(dialogLayout);
      dialog.open();
    });

    // Lisätään komponentit korttiin
    add(div, header, subtitleSpan, description, badge, editButton);
  }
}