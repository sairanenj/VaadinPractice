package com.example.application.views.peliapuri;

import com.example.application.data.Asiakas;
import com.example.application.data.Peliapuri;
import com.example.application.data.Pelit;
import com.example.application.services.AsiakasService;
import com.example.application.services.PeliapuriService;
import com.example.application.services.PelitService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
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
import com.vaadin.flow.data.renderer.LitRenderer;
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

@PageTitle("Peliapuri")
@Route(value = "peliapuri/:peliapuriID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
@Menu(order = 4, icon = LineAwesomeIconUrl.LIST_ALT_SOLID)
@Uses(Icon.class)
public class PeliapuriView extends Div implements BeforeEnterObserver {

  private final String PELIAPURI_ID = "peliapuriID";
  private final String PELIAPURI_EDIT_ROUTE_TEMPLATE = "peliapuri/%s/edit";

  private final Grid<Peliapuri> grid = new Grid<>(Peliapuri.class, false);

  private ComboBox<Asiakas> asiakas;
  private ComboBox<Pelit> pelinnimi;
  private Checkbox kaytossa;
  private Checkbox jatkoon;
  private Checkbox eijatkoon;
  private TextField huomiot;

  private final TextField asiakasFilter = new TextField();

  private final Button cancel = new Button("Cancel");
  private final Button save = new Button("Save");

  private final BeanValidationBinder<Peliapuri> binder;

  private Peliapuri peliapuri;

  private final PeliapuriService peliapuriService;
  private final AsiakasService asiakasService;
  private final PelitService pelitService;

  private HorizontalLayout filtersLayout;

  public PeliapuriView(PeliapuriService peliapuriService, AsiakasService asiakasService, PelitService pelitService) {
    this.peliapuriService = peliapuriService;
    this.asiakasService = asiakasService;
    this.pelitService = pelitService;
    addClassNames("peliapuri-view");

    // Lisätään suodattimet
    addFilters();

    // Luodaan UI
    SplitLayout splitLayout = new SplitLayout();

    createGridLayout(splitLayout);
    createEditorLayout(splitLayout);

    add(splitLayout);

    // Säädetään gridiin halutut sarakkeet toiminallisuuksineen
    grid.addColumn(peliapuri -> {
      Asiakas asiakas = peliapuri.getAsiakas();
      return asiakas != null ? asiakas.getEtunimi() + " " + asiakas.getSukunimi() : "";
    }).setHeader("Asiakas").setAutoWidth(true);

    grid.addColumn(peliapuri -> {
      Pelit pelit = peliapuri.getPelit();
      return pelit != null ? pelit.getPelititle() : "";
    }).setHeader("Pelin nimi").setAutoWidth(true);

    LitRenderer<Peliapuri> kaytossaRenderer = LitRenderer.<Peliapuri>of(
      "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
      .withProperty("icon", kaytossa -> kaytossa.isKaytossa() ? "check" : "minus")
      .withProperty("color", kaytossa -> kaytossa.isKaytossa()
        ? "var(--lumo-primary-text-color)"
        : "var(--lumo-disabled-text-color)");

    grid.addColumn(kaytossaRenderer).setHeader("Käytössä").setAutoWidth(true);

    LitRenderer<Peliapuri> jatkoonRenderer = LitRenderer.<Peliapuri>of(
      "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
      .withProperty("icon", jatkoon -> jatkoon.isJatkoon() ? "check" : "minus")
      .withProperty("color", jatkoon -> jatkoon.isJatkoon()
        ? "var(--lumo-primary-text-color)"
        : "var(--lumo-disabled-text-color)");

    grid.addColumn(jatkoonRenderer).setHeader("Jatkoon").setAutoWidth(true);

    LitRenderer<Peliapuri> eijatkoonRenderer = LitRenderer.<Peliapuri>of(
      "<vaadin-icon icon='vaadin:${item.icon}' style='width: var(--lumo-icon-size-s); height: var(--lumo-icon-size-s); color: ${item.color};'></vaadin-icon>")
      .withProperty("icon", eijatkoon -> eijatkoon.isEijatkoon() ? "check" : "minus")
      .withProperty("color", eijatkoon -> eijatkoon.isEijatkoon()
        ? "var(--lumo-primary-text-color)"
        : "var(--lumo-disabled-text-color)");

    grid.addColumn(eijatkoonRenderer).setHeader("Ei jatkoon").setAutoWidth(true);

    grid.addColumn("huomiot").setAutoWidth(true);
    grid.setItems(query -> peliapuriService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

    // Kun rivi valitaan tai valinta poistetaan, päästään dataa käsittelemään editoriin
    grid.asSingleSelect().addValueChangeListener(event -> {
      if (event.getValue() != null) {
        UI.getCurrent().navigate(String.format(PELIAPURI_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
      } else {
        clearForm();
        UI.getCurrent().navigate(PeliapuriView.class);
      }
    });

    // Konfiguroidaan lomake
    binder = new BeanValidationBinder<>(Peliapuri.class);

    // Bindataan kentät, määritellään esim. validointisäännöt
    binder.forField(asiakas).bind(Peliapuri::getAsiakas, Peliapuri::setAsiakas);
    binder.forField(pelinnimi).bind(Peliapuri::getPelit, Peliapuri::setPelit);
    binder.bindInstanceFields(this);

    cancel.addClickListener(e -> {
      clearForm();
      refreshGrid();
    });

    save.addClickListener(e -> {
      try {
        if (this.peliapuri == null) {
          this.peliapuri = new Peliapuri();
        }
        binder.writeBean(this.peliapuri);
        peliapuriService.save(this.peliapuri);
        clearForm();
        refreshGrid();
        Notification.show("Tiedot päivitetty");
        UI.getCurrent().navigate(PeliapuriView.class);
      } catch (ObjectOptimisticLockingFailureException exception) {
        Notification n = Notification.show(
          "Ongelma tietojen päivityksessä.");
        n.setPosition(Position.MIDDLE);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
      } catch (ValidationException validationException) {
        Notification.show("Tietojen päivitys epäonnistui. Varmista että kaikki arvot ovat oikein.");
      }
    });
  }

  // Lisää suodattimet
  private void addFilters() {
    filtersLayout = new HorizontalLayout();
    filtersLayout.setWidthFull();
    filtersLayout.setAlignItems(Alignment.CENTER); // Varmista, että kohteet ovat pystysuunnassa keskitettyjä

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

    gridLayout.add(grid);

    // Lisätään footer gridin layoutiin
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
    grid.setItems(query -> peliapuriService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream()
      .filter(peliapuri -> filterByField(peliapuri.getAsiakas().getEtunimi() + " " + peliapuri.getAsiakas().getSukunimi(), asiakasFilter.getValue())));
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
    Optional<Long> peliapuriId = event.getRouteParameters().get(PELIAPURI_ID).map(Long::parseLong);
    if (peliapuriId.isPresent()) {
      Optional<Peliapuri> peliapuriFromBackend = peliapuriService.get(peliapuriId.get());
      if (peliapuriFromBackend.isPresent()) {
        populateForm(peliapuriFromBackend.get());
      } else {
        Notification.show(String.format("Haluttu peliapuri ei löytynyt, ID = %s", peliapuriId.get()),
          3000, Notification.Position.BOTTOM_START);
        // Kun rivi on valittu mutta data ei ole enää saatavilla, päivitetään gridi
        refreshGrid();
        event.forwardTo(PeliapuriView.class);
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
    pelinnimi = new ComboBox<>("Pelinnimi");
    pelinnimi.setItemLabelGenerator(Pelit::getPelititle);
    pelinnimi.setItems(pelitService.findAlltoList());
    kaytossa = new Checkbox("Kaytossa");
    jatkoon = new Checkbox("Jatkoon");
    eijatkoon = new Checkbox("Eijatkoon");
    huomiot = new TextField("Huomiot");
    formLayout.add(asiakas, pelinnimi, kaytossa, jatkoon, eijatkoon, huomiot);

    editorDiv.add(formLayout);

    Div vali = new Div();
    vali.getStyle().set("height", "20px");
    editorDiv.add(vali);

    Span infoSpan = new Span("HUOM! Voit päivittää asiakkaan pelitietoja koska tahansa. Asiakas peliapuritietoineen poistuu automaattisesti, kun asiakas poistetaan.");
    infoSpan.getElement().setAttribute("theme", "badge");
    editorDiv.add(infoSpan);

    createButtonLayout(editorLayoutDiv);

    splitLayout.addToSecondary(editorLayoutDiv);
  }

  // Luodaan nappien layoutti
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
  private void populateForm(Peliapuri value) {
    this.peliapuri = value;
    binder.readBean(this.peliapuri);
  }
}