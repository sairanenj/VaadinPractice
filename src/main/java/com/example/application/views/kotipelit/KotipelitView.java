package com.example.application.views.kotipelit;

import com.example.application.data.Asiakas;
import com.example.application.data.Kotipelit;
import com.example.application.services.AsiakasService;
import com.example.application.services.KotipelitService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
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

@PageTitle("Kotipelit")
@Route(value = "kotipelit/:kotipelitID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
@Menu(order = 5, icon = LineAwesomeIconUrl.LIST_SOLID)
public class KotipelitView extends Div implements BeforeEnterObserver {

  private final String KOTIPELIT_ID = "kotipelitID";
  private final String KOTIPELIT_EDIT_ROUTE_TEMPLATE = "kotipelit/%s/edit";

  private final Grid<Kotipelit> grid = new Grid<>(Kotipelit.class, false);

  private ComboBox<Asiakas> asiakas;
  private TextField pelitkotona;
  private TextField pelitpvkoti;
  private TextField tietoja;

  private final TextField asiakasFilter = new TextField();

  private final Button cancel = new Button("Cancel");
  private final Button save = new Button("Save");

  private final BeanValidationBinder<Kotipelit> binder;

  private Kotipelit kotipelit;

  private final KotipelitService kotipelitService;
  private final AsiakasService asiakasService;

  private HorizontalLayout filtersLayout;

  public KotipelitView(KotipelitService kotipelitService, AsiakasService asiakasService) {
    this.kotipelitService = kotipelitService;
    this.asiakasService = asiakasService;
    addClassNames("kotipelit-view");

    // Lisätään filtterit
    addFilters();

    // Luodaan UI
    SplitLayout splitLayout = new SplitLayout();

    createGridLayout(splitLayout);
    createEditorLayout(splitLayout);

    add(splitLayout);

    // Säädetään gridi näyttämään halutut tiedot
    grid.addColumn(kotipelit -> {
      Asiakas asiakas = kotipelit.getAsiakas();
      return asiakas != null ? asiakas.getEtunimi() + " " + asiakas.getSukunimi() : "";
    }).setHeader("Asiakas").setAutoWidth(true);
    grid.addColumn("pelitkotona").setAutoWidth(true);
    grid.addColumn("pelitpvkoti").setAutoWidth(true);
    grid.addColumn("tietoja").setAutoWidth(true);
    grid.setItems(query -> kotipelitService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

    // Kun rivi valitaan tai valinta poistetaan, päästään käsiksi editoriin kyseisellä tiedolla
    grid.asSingleSelect().addValueChangeListener(event -> {
      if (event.getValue() != null) {
        UI.getCurrent().navigate(String.format(KOTIPELIT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
      } else {
        clearForm();
        UI.getCurrent().navigate(KotipelitView.class);
      }
    });

    // Konfiguroidaan lomake
    binder = new BeanValidationBinder<>(Kotipelit.class);

    // Bindaataan kentät, ässä määritellään esim. validointisäännöt
    binder.forField(asiakas).bind(Kotipelit::getAsiakas, Kotipelit::setAsiakas);
    binder.bindInstanceFields(this);

    cancel.addClickListener(e -> {
      clearForm();
      refreshGrid();
    });

    save.addClickListener(e -> {
      try {
        if (this.kotipelit == null) {
          this.kotipelit = new Kotipelit();
        }
        binder.writeBean(this.kotipelit);
        kotipelitService.save(this.kotipelit);
        clearForm();
        refreshGrid();
        Notification.show("Data päivitetty");
        UI.getCurrent().navigate(KotipelitView.class);
      } catch (ObjectOptimisticLockingFailureException exception) {
        Notification n = Notification.show(
          "Ongelma dataa päivitettäessä.");
        n.setPosition(Position.MIDDLE);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
      } catch (ValidationException validationException) {
        Notification.show("Datan päivitys epäonnistui. Varmista tietojen oikeellisuus.");
      }
    });
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    Optional<Long> kotipelitId = event.getRouteParameters().get(KOTIPELIT_ID).map(Long::parseLong);
    if (kotipelitId.isPresent()) {
      Optional<Kotipelit> kotipelitFromBackend = kotipelitService.get(kotipelitId.get());
      if (kotipelitFromBackend.isPresent()) {
        populateForm(kotipelitFromBackend.get());
      } else {
        Notification.show(String.format("The requested kotipelit was not found, ID = %s", kotipelitId.get()),
          3000, Notification.Position.BOTTOM_START);
        // Kun rivi on valittu mutta data ei ole enää saatavilla, päivitetään gridi
        refreshGrid();
        event.forwardTo(KotipelitView.class);
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
    asiakas = new ComboBox<>("Asiakas");
    asiakas.setItemLabelGenerator(asiakas -> asiakas.getEtunimi() + " " + asiakas.getSukunimi());
    asiakas.setItems(asiakasService.findAlltoList());
    pelitkotona = new TextField("Pelitkotona");
    pelitpvkoti = new TextField("Pelitpvkoti");
    tietoja = new TextField("Tietoja");
    formLayout.add(asiakas, pelitkotona, pelitpvkoti, tietoja);

    editorDiv.add(formLayout);

    Div vali = new Div();
    vali.getStyle().set("height", "20px");
    editorDiv.add(vali);

    Span infoSpan = new Span("HUOM! Voit päivittää asiakkaan pelitietoja koska tahansa. Asiakas pelitietoineen poistuu automaattisesti, kun asiakas poistetaan.");
    infoSpan.getElement().setAttribute("theme", "badge");
    editorDiv.add(infoSpan);

    createButtonLayout(editorLayoutDiv);

    splitLayout.addToSecondary(editorLayoutDiv);
  }

  // Luodaan buttonien layoutti
  private void createButtonLayout(Div editorLayoutDiv) {
    HorizontalLayout buttonLayout = new HorizontalLayout();
    buttonLayout.setClassName("button-layout");
    cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    buttonLayout.add(save, cancel);
    editorLayoutDiv.add(buttonLayout);
  }

  // Lisätään suodatin
  private void addFilters() {
    filtersLayout = new HorizontalLayout();
    filtersLayout.setWidthFull();
    filtersLayout.setAlignItems(Alignment.CENTER); // Varmistetaan, että kohteet ovat pystysuunnassa keskitettyjä

    Span filterHelp = new Span("Suodatin:");
    filterHelp.getStyle().set("margin-left", "1rem");
    filterHelp.getStyle().set("padding-top", "0.5rem");
    filterHelp.getStyle().set("color", "var(--lumo-primary-text-color)");

    asiakasFilter.setPlaceholder("Asiakas");
    asiakasFilter.setValueChangeMode(ValueChangeMode.EAGER);
    asiakasFilter.addValueChangeListener(e -> refreshGrid());

    filtersLayout.add(filterHelp, asiakasFilter);
  }

  // Luodaan gridin layoutti
  private void createGridLayout(SplitLayout splitLayout) {
    Div wrapper = new Div();
    wrapper.setClassName("grid-wrapper");

    VerticalLayout gridLayout = new VerticalLayout();
    gridLayout.setSizeFull();

    // Lisätään suodattimet grid layoutiin
    if (filtersLayout != null) {
      gridLayout.add(filtersLayout);
    }

    // Lisää itse grid grid layoutiin
    gridLayout.add(grid);

    // Lisää kömpelö footer grid layoutiin
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
    grid.setItems(query -> kotipelitService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream()
      .filter(kotipelit -> filterByField(kotipelit.getAsiakas().getEtunimi() + " " + kotipelit.getAsiakas().getSukunimi(), asiakasFilter.getValue())));
  }

  // Suodatus kentän perusteella
  private boolean filterByField(String fieldValue, String filterValue) {
    if (filterValue == null || filterValue.isEmpty()) {
      return true;
    }
    return fieldValue != null && fieldValue.toLowerCase().contains(filterValue.toLowerCase());
  }

  // Tyhjennä lomake metodi
  private void clearForm() {
    populateForm(null);
  }

  // Täytä lomake metodi
  private void populateForm(Kotipelit value) {
    this.kotipelit = value;
    binder.readBean(this.kotipelit);
  }
}