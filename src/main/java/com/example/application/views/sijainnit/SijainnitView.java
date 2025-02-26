package com.example.application.views.sijainnit;

import com.example.application.data.Sijainti;
import com.example.application.services.SijaintiService;
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
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import jakarta.annotation.security.PermitAll;

import java.util.Optional;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Sijainnit")
@Route(value = "sijainti/:sijaintiID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
@Menu(order = 2, icon = LineAwesomeIconUrl.MAP_MARKER_SOLID)
public class SijainnitView extends Div implements BeforeEnterObserver {

  private final String SIJAINTI_ID = "sijaintiID";
  private final String SIJAINTI_EDIT_ROUTE_TEMPLATE = "sijainti/%s/edit";

  private final Grid<Sijainti> grid = new Grid<>(Sijainti.class, false);

  private TextField paikka;
  private TextField postitoimipaikka;
  private TextField postinumero;
  private TextField lisatiedot;

  private final TextField paikkaFilter = new TextField();
  private final TextField postitoimipaikkaFilter = new TextField();
  private final TextField postinumeroFilter = new TextField();

  private final Button cancel = new Button("Cancel");
  private final Button save = new Button("Save");

  private final BeanValidationBinder<Sijainti> binder;

  private Sijainti sijainti;

  private final SijaintiService sijaintiService;

  private HorizontalLayout filtersLayout;

  public SijainnitView(SijaintiService sijaintiService) {
    this.sijaintiService = sijaintiService;
    addClassNames("sijainnit-view");

    // Lisätään suodattimet
    addFilters();

    // Luodaan käyttöliittymä
    SplitLayout splitLayout = new SplitLayout();

    createGridLayout(splitLayout);
    createEditorLayout(splitLayout);

    add(splitLayout);

    // Säädetään gridiin halutut sarakkeet
    grid.addColumn("paikka").setAutoWidth(true);
    grid.addColumn("postitoimipaikka").setAutoWidth(true);
    grid.addColumn("postinumero").setAutoWidth(true);
    grid.addColumn("lisatiedot").setAutoWidth(true);

    // Lisätään poistonappi ja siihen varmistusviesti, koska poistoon liittyy niin paljon dataa
    grid.addColumn(new ComponentRenderer<>(sijainti -> {
      Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
      deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
      deleteButton.addClickListener(e -> {
        Dialog confirmDialog = new Dialog();
        confirmDialog.add("Poistat samalla kaikki sijainnin asiakastiedot. Poistetaanko?");
        Button confirmButton = new Button("Kyllä", event -> {
          try {
            sijaintiService.delete(sijainti);
            refreshGrid();
            Notification.show("Sijainti poistettu");
          } catch (Exception ex) {
            Notification.show("Virhe poistettaessa sijaintia: " + ex.getMessage(), 3000, Position.MIDDLE);
          }
          confirmDialog.close();
        });
        Button cancelButton = new Button("Ei, palaa", event -> confirmDialog.close());
        confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
        confirmDialog.open();
      });
      return deleteButton;
    })).setHeader("Poista").setAutoWidth(true);

    grid.setItems(query -> sijaintiService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

    // Kun rivi valitaan tai valinta poistetaan, päästään käsittelemään editorilla
    grid.asSingleSelect().addValueChangeListener(event -> {
      if (event.getValue() != null) {
        UI.getCurrent().navigate(String.format(SIJAINTI_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
      } else {
        clearForm();
        UI.getCurrent().navigate(SijainnitView.class);
      }
    });

    // Konfiguroi lomake
    binder = new BeanValidationBinder<>(Sijainti.class);

    // Bindataan kentät, määritellään esim. validointisäännöt
    binder.bindInstanceFields(this);

    cancel.addClickListener(e -> {
      clearForm();
      refreshGrid();
    });

    save.addClickListener(e -> {
      try {
        if (this.sijainti == null) {
          this.sijainti = new Sijainti();
        }
        binder.writeBean(this.sijainti);
        sijaintiService.save(this.sijainti);
        clearForm();
        refreshGrid();
        Notification.show("Tiedot päivitetty");
        UI.getCurrent().navigate(SijainnitView.class);
      } catch (ObjectOptimisticLockingFailureException exception) {
        Notification n = Notification.show(
          "Virhe dataa päivitettäessä.");
        n.setPosition(Position.MIDDLE);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
      } catch (ValidationException validationException) {
        Notification.show("Virheellinen data, tarkista kentät.");
      }
    });
  }

  // Lisätään suodattimet
  private void addFilters() {
    filtersLayout = new HorizontalLayout();
    filtersLayout.setWidthFull();

    Span filterHelp = new Span("Suodatin:");
    filterHelp.getStyle().set("margin-left", "1rem");
    filterHelp.getStyle().set("padding-top", "0.5rem");
    filterHelp.getStyle().set("color", "var(--lumo-primary-text-color)");

    paikkaFilter.setPlaceholder("Paikka");
    paikkaFilter.setValueChangeMode(ValueChangeMode.EAGER);
    paikkaFilter.addValueChangeListener(e -> refreshGrid());

    postitoimipaikkaFilter.setPlaceholder("Postitoimipaikka");
    postitoimipaikkaFilter.setValueChangeMode(ValueChangeMode.EAGER);
    postitoimipaikkaFilter.addValueChangeListener(e -> refreshGrid());

    postinumeroFilter.setPlaceholder("Postinumero");
    postinumeroFilter.setValueChangeMode(ValueChangeMode.EAGER);
    postinumeroFilter.addValueChangeListener(e -> refreshGrid());

    filtersLayout.add(filterHelp, paikkaFilter, postitoimipaikkaFilter, postinumeroFilter);
  }

  // Luodaan gridin layoutti
  private void createGridLayout(SplitLayout splitLayout) {
    Div wrapper = new Div();
    wrapper.setClassName("grid-wrapper");

    VerticalLayout gridLayout = new VerticalLayout();
    gridLayout.setSizeFull();

    // Lisätään suodattimet gridin layouttiin
    if (filtersLayout != null) {
      gridLayout.add(filtersLayout);
    }

    // Lisätään itse grid gridin layouttiin
    gridLayout.add(grid);

    // Ja sama simppeli footteri
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
    grid.setItems(query -> sijaintiService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream()
      .filter(sijainti -> filterByField(sijainti.getPaikka(), paikkaFilter.getValue()))
      .filter(sijainti -> filterByField(sijainti.getPostitoimipaikka(), postitoimipaikkaFilter.getValue()))
      .filter(sijainti -> filterByField(sijainti.getPostinumero(), postinumeroFilter.getValue())));
  }

  // Suodatus kentän mukaan
  private boolean filterByField(String fieldValue, String filterValue) {
    if (filterValue == null || filterValue.isEmpty()) {
      return true;
    }
    return fieldValue != null && fieldValue.toLowerCase().contains(filterValue.toLowerCase());
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    Optional<Long> sijaintiId = event.getRouteParameters().get(SIJAINTI_ID).map(Long::parseLong);
    if (sijaintiId.isPresent()) {
      Optional<Sijainti> sijaintiFromBackend = sijaintiService.get(sijaintiId.get());
      if (sijaintiFromBackend.isPresent()) {
        populateForm(sijaintiFromBackend.get());
      } else {
        Notification.show(String.format("Haluttua sijaintia ei löytynyt, ID = %s", sijaintiId.get()),
          3000, Notification.Position.BOTTOM_START);
        // Kun rivi on valittu mutta data ei ole enää saatavilla, päivitetään gridi
        refreshGrid();
        event.forwardTo(SijainnitView.class);
      }
    }
  }

  // Gridin editorin layoutti
  private void createEditorLayout(SplitLayout splitLayout) {
    Div editorLayoutDiv = new Div();
    editorLayoutDiv.setClassName("editor-layout");

    Div editorDiv = new Div();
    editorDiv.setClassName("editor");
    editorLayoutDiv.add(editorDiv);

    FormLayout formLayout = new FormLayout();
    paikka = new TextField("Paikka");
    postitoimipaikka = new TextField("Postitoimipaikka");
    postinumero = new TextField("Postinumero");
    lisatiedot = new TextField("Lisatiedot");
    formLayout.add(paikka, postitoimipaikka, postinumero, lisatiedot);

    editorDiv.add(formLayout);

    Div vali = new Div();
    vali.getStyle().set("height", "20px");
    editorDiv.add(vali);

    Span infoSpan = new Span("HUOM! Huomioi aina asiakastiedot ensin! Muuttamalla paikan tietoja, muuttuvat myös asiakkailla olevat sijaintitiedot. Jos lähdet poistamaan koko paikkatietoa, poistat myös kaikki paikkaan liittyvät asiakastiedot.");
    infoSpan.getElement().setAttribute("theme", "badge");
    editorDiv.add(infoSpan);

    createButtonLayout(editorLayoutDiv);

    splitLayout.addToSecondary(editorLayoutDiv);
  }

  // Nappien layoutti
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
  private void populateForm(Sijainti value) {
    this.sijainti = value;
    binder.readBean(this.sijainti);
  }
}