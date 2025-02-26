package com.example.application.views.kayttaja;

import com.example.application.data.Kayttaja;
import com.example.application.services.KayttajaService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import jakarta.annotation.security.PermitAll;

import java.util.Optional;

@PageTitle("Kayttajat")
@Route(value = "kayttaja/:kayttajaID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
public class KayttajaView extends Div implements BeforeEnterObserver {

  private final String KAYTTAJA_ID = "kayttajaID";
  private final String KAYTTAJA_EDIT_ROUTE_TEMPLATE = "kayttaja/%s/edit";

  private final Grid<Kayttaja> grid = new Grid<>(Kayttaja.class, false);

  private TextField kayttajanimi;
  private TextField rooli;
  private TextField tehtava;

  private final Button cancel = new Button("Peruuta");
  private final Button save = new Button("Tallenna");

  private final BeanValidationBinder<Kayttaja> binder;

  private Kayttaja kayttaja;

  private final KayttajaService kayttajaService;

  public KayttajaView(KayttajaService kayttajaService) {
    this.kayttajaService = kayttajaService;
    addClassNames("kayttaja-view");

    // Luodaan UI
    SplitLayout splitLayout = new SplitLayout();

    createGridLayout(splitLayout);
    createEditorLayout(splitLayout);

    add(splitLayout);

    // Lisätään sarakkeet gridiin
    grid.addColumn("kayttajanimi").setHeader("Kayttajanimi").setAutoWidth(true);
    grid.addColumn("rooli").setHeader("Rooli").setAutoWidth(true);
    grid.addColumn("tehtava").setHeader("Tehtava").setAutoWidth(true);

    // Datan poistonappi gridin perään ja jälleen kysely enne poistoa. Jos käyttäjä on ADMIN, ei ole roskapönttönappia vaan blokataan poistomahdollisuus
    grid.addColumn(new ComponentRenderer<>(kayttaja -> {
      if ("ADMIN".equals(kayttaja.getRooli())) {
        return new Span("ET VOI POISTAA");
      } else {
        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> {
          Dialog confirmDialog = new Dialog();
          confirmDialog.add("Poistetaanko kayttaja?");
          Button confirmButton = new Button("Kyllä", event -> {
            try {
              kayttajaService.delete(kayttaja.getId());
              refreshGrid();
              Notification.show("Kayttaja poistettu");
            } catch (Exception ex) {
              Notification.show("Virhe poistettaessa kayttajaa: " + ex.getMessage(), 3000, Position.MIDDLE);
            }
            confirmDialog.close();
          });
          Button cancelButton = new Button("Ei, palaa", event -> confirmDialog.close());
          confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
          confirmDialog.open();
        });
        return deleteButton;
      }
    })).setHeader("Poista").setAutoWidth(true);

    grid.setItems(query -> kayttajaService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

    // Kun rivi valitaan tai valinta poistetaan, päästään editoimaan dataa
    grid.asSingleSelect().addValueChangeListener(event -> {
      if (event.getValue() != null) {
        UI.getCurrent().navigate(String.format(KAYTTAJA_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
      } else {
        clearForm();
        UI.getCurrent().navigate(KayttajaView.class);
      }
    });

    // Konfiguroi lomake
    binder = new BeanValidationBinder<>(Kayttaja.class);

    // Bindataan kentät. Tässä määritellään esim. validointisäännöt
    binder.bindInstanceFields(this);

    // Lisätään clear ja refresh metodit cancel napukalle
    cancel.addClickListener(e -> {
      clearForm();
      refreshGrid();
    });

    save.addClickListener(e -> {
      try {
        if (this.kayttaja == null) {
          this.kayttaja = new Kayttaja();
          this.kayttaja.setRooli("USER"); // Asetetaan oletusrooli USER uudelle käyttäjälle, jottai ei luoda mitään ei-haluttua
          rooli.setValue("USER"); // Aseta rooli-kentän arvoksi USER
        } else if ("ADMIN".equals(this.kayttaja.getRooli())) { // Ja taas estetään ADMIN-käyttäjän päivitys
          Notification.show("ADMIN-käyttäjää ei voi päivittää.");
          return;
        }
        binder.writeBean(this.kayttaja);
        kayttajaService.save(this.kayttaja);
        clearForm();
        refreshGrid();
        Notification.show("Kayttajatieto päivitetty");
        UI.getCurrent().navigate(KayttajaView.class);
      } catch (ValidationException validationException) {
        Notification.show("Datan päivitys epäonnistui, varmista että kaikki arvot ovat oikein.");
      }
    });
  }

  // Luodaan gridin layout
  private void createGridLayout(SplitLayout splitLayout) {
    Div wrapper = new Div();
    wrapper.setClassName("grid-wrapper");

    VerticalLayout gridLayout = new VerticalLayout();
    gridLayout.setSizeFull();

    gridLayout.add(grid);

    // Ja taas tämä hieno footer
    Div footer = new Div();
    footer.setText("(c) Jaska 2025 - VAADIN");
    footer.getStyle().set("text-align", "left");
    footer.getStyle().set("padding-left", "1rem");
    gridLayout.add(footer);

    wrapper.add(gridLayout);
    splitLayout.addToPrimary(wrapper);
  }

  // Gridin päivitysmetodi
  private void refreshGrid() {
    grid.setItems(query -> kayttajaService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    Optional<Long> kayttajaId = event.getRouteParameters().get(KAYTTAJA_ID).map(Long::parseLong);
    if (kayttajaId.isPresent()) {
      Optional<Kayttaja> kayttajaFromBackend = kayttajaService.findById(kayttajaId.get());
      if (kayttajaFromBackend.isPresent()) {
        if ("ADMIN".equals(kayttajaFromBackend.get().getRooli())) {
          Notification.show("ADMIN-käyttäjää ei voi päivittää.");
          UI.getCurrent().navigate(KayttajaView.class);
        } else {
          populateForm(kayttajaFromBackend.get());
        }
      } else {
        Notification.show(String.format("Haettua kayttajaa ei löytynyt, ID = %s", kayttajaId.get()), 3000,
          Notification.Position.BOTTOM_START);
        // Kun rivi on valittu mutta data ei ole enää saatavilla, päivitetään gridi ja navigoidaan takaisin
        refreshGrid();
        event.forwardTo(KayttajaView.class);
      }
    }
  }

  // Luodaan editorin layoutti
  private void createEditorLayout(SplitLayout splitLayout) {
    Div editorLayoutDiv = new Div();
    editorLayoutDiv.setClassName("editor-layout");

    Div editorDiv = new Div();
    editorDiv.setClassName("editor");
    editorLayoutDiv.add(editorDiv);

    FormLayout formLayout = new FormLayout();
    kayttajanimi = new TextField("Kayttajanimi");
    rooli = new TextField("Rooli");
    rooli.setReadOnly(true); // Asetetaan rooli-kenttä vain luku -tilaan
    tehtava = new TextField("Tehtava");
    formLayout.add(kayttajanimi, rooli, tehtava);

    editorDiv.add(formLayout);

    createButtonLayout(editorLayoutDiv);

    splitLayout.addToSecondary(editorLayoutDiv);
  }

  // Luodaan editorin nappien layoutti
  private void createButtonLayout(Div editorLayoutDiv) {
    HorizontalLayout buttonLayout = new HorizontalLayout();
    buttonLayout.setClassName("button-layout");
    cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    buttonLayout.add(save, cancel);
    editorLayoutDiv.add(buttonLayout);
  }

  // Tyhjennä lomake metodi
  private void clearForm() {
    populateForm(null);
  }

  // Täytä lomake metodi
  private void populateForm(Kayttaja value) {
    this.kayttaja = value;
    binder.readBean(this.kayttaja);
  }
}