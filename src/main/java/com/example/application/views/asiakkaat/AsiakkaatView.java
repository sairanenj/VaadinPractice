package com.example.application.views.asiakkaat;

import com.example.application.data.Asiakas;
import com.example.application.data.Sijainti;
import com.example.application.services.AsiakasService;
import com.example.application.services.KotipelitService;
import com.example.application.services.PeliapuriService;
import com.example.application.services.SijaintiService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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

@PageTitle("Asiakkaat")
@Route(value = "asiakas/:asiakasID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
@Menu(order = 1, icon = LineAwesomeIconUrl.USER)
public class AsiakkaatView extends Div implements BeforeEnterObserver {

  private final String ASIAKAS_ID = "asiakasID";
  private final String ASIAKAS_EDIT_ROUTE_TEMPLATE = "asiakas/%s/edit";

  private final Grid<Asiakas> grid = new Grid<>(Asiakas.class, false);

  private TextField etunimi;
  private TextField sukunimi;
  private TextField puhnro;
  private TextField sposti;
  private ComboBox<Sijainti> sijainti;

  private final TextField etunimiFilter = new TextField();
  private final TextField sukunimiFilter = new TextField();
  private final TextField puhnroFilter = new TextField();
  private final TextField spostiFilter = new TextField();
  private final TextField sijaintiFilter = new TextField();

  private final Button cancel = new Button("Peruuta");
  private final Button save = new Button("Tallenna");

  private final BeanValidationBinder<Asiakas> binder;

  private Asiakas asiakas;

  private final AsiakasService asiakasService;
  private final SijaintiService sijaintiService;

  private HorizontalLayout filtersLayout;

  public AsiakkaatView(AsiakasService asiakasService, SijaintiService sijaintiService, KotipelitService kotipelitService, PeliapuriService peliapuriService) {
    this.asiakasService = asiakasService;
    this.sijaintiService = sijaintiService;
    addClassNames("asiakkaat-view");

    // Suodattimet
    addFilters();

    // Luodaan UI
    SplitLayout splitLayout = new SplitLayout();

    createGridLayout(splitLayout);
    createEditorLayout(splitLayout);

    add(splitLayout);

    // Säädetään grid näyttämään halutut tiedot
    grid.addColumn("etunimi").setHeader("Etunimi").setAutoWidth(true);
    grid.addColumn("sukunimi").setHeader("Sukunimi").setAutoWidth(true);
    grid.addColumn("puhnro").setHeader("Puhelinnumero").setAutoWidth(true);
    grid.addColumn("sposti").setHeader("Sähköposti").setAutoWidth(true);
    grid.addColumn(asiakas -> asiakas.getSijainti().getPaikka()).setHeader("Paikka").setAutoWidth(true);

    // Lisätään poistonappi gridin perään sekä notification kysely ennen poistamista (asiakkaan poisto liittyy niin moneen muuhun tietoon)
    grid.addColumn(new ComponentRenderer<>(asiakas -> {
      Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
      deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
      deleteButton.addClickListener(e -> {
        Dialog confirmDialog = new Dialog();
        confirmDialog.add("Poistat samalla kaikki asiakkaan peliapuri- ja kotipelitiedot. Poistetaanko?");
        Button confirmButton = new Button("Kyllä", event -> {
          try {
            kotipelitService.deleteByAsiakasId(asiakas.getId());
            peliapuriService.deleteByAsiakasId(asiakas.getId());
            asiakasService.deleteById(asiakas.getId());
            refreshGrid();
            Notification.show("Asiakastiedot poistettu");
          } catch (Exception ex) {
            Notification.show("Virhe poistettaessa asiakasta: " + ex.getMessage(), 3000, Position.MIDDLE);
          }
          confirmDialog.close();
        });
        Button cancelButton = new Button("Ei, palaa", event -> confirmDialog.close());
        confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
        confirmDialog.open();
      });
      return deleteButton;
    })).setHeader("Poista").setAutoWidth(true);

    grid.setItems(query -> asiakasService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
    grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

    // Kun rivi valitaan tai valinta poistetaan, päästään käsiksi editoriin
    grid.asSingleSelect().addValueChangeListener(event -> {
      if (event.getValue() != null) {
        UI.getCurrent().navigate(String.format(ASIAKAS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
      } else {
        clearForm();
        UI.getCurrent().navigate(AsiakkaatView.class);
      }
    });

    // Konfiguroidaan lomake
    binder = new BeanValidationBinder<>(Asiakas.class);

    // Bindataan kentät. Tässä määritellään esim. validointisäännöt
    binder.bindInstanceFields(this);

    cancel.addClickListener(e -> {
      clearForm();
      refreshGrid();
    });

    save.addClickListener(e -> {
      try {
        if (this.asiakas == null) {
          this.asiakas = new Asiakas();
        }
        binder.writeBean(this.asiakas);
        asiakasService.save(this.asiakas);
        clearForm();
        refreshGrid();
        Notification.show("Asiakastieto päivitetty");
        UI.getCurrent().navigate(AsiakkaatView.class);
      } catch (ObjectOptimisticLockingFailureException exception) {
        Notification n = Notification.show(
          "Virhe dataa päivitettäessä.");
        n.setPosition(Position.MIDDLE);
        n.addThemeVariants(NotificationVariant.LUMO_ERROR);
      } catch (ValidationException validationException) {
        Notification.show("Datan päivitys epäonnistui, varmista että kaikki arvot ovat oikein.");
      }
    });
  }

  // Lisätään suodattimet gridin päälle
  private void addFilters() {
    filtersLayout = new HorizontalLayout();
    filtersLayout.setWidthFull();

    Span filterHelp = new Span("Suodatin:");
    filterHelp.getStyle().set("margin-left", "1rem");
    filterHelp.getStyle().set("padding-top", "0.5rem");
    filterHelp.getStyle().set("color", "var(--lumo-primary-text-color)");

    etunimiFilter.setPlaceholder("Etunimi");
    etunimiFilter.setValueChangeMode(ValueChangeMode.EAGER);
    etunimiFilter.addValueChangeListener(e -> refreshGrid());

    sukunimiFilter.setPlaceholder("Sukunimi");
    sukunimiFilter.setValueChangeMode(ValueChangeMode.EAGER);
    sukunimiFilter.addValueChangeListener(e -> refreshGrid());

    puhnroFilter.setPlaceholder("Puhelinnumero");
    puhnroFilter.setValueChangeMode(ValueChangeMode.EAGER);
    puhnroFilter.addValueChangeListener(e -> refreshGrid());

    spostiFilter.setPlaceholder("Sähköposti");
    spostiFilter.setValueChangeMode(ValueChangeMode.EAGER);
    spostiFilter.addValueChangeListener(e -> refreshGrid());

    sijaintiFilter.setPlaceholder("Paikka");
    sijaintiFilter.setValueChangeMode(ValueChangeMode.EAGER);
    sijaintiFilter.addValueChangeListener(e -> refreshGrid());

    filtersLayout.add(filterHelp, etunimiFilter, sukunimiFilter, puhnroFilter, spostiFilter, sijaintiFilter);
  }

  // Luodaan gridin layout
  private void createGridLayout(SplitLayout splitLayout) {
    Div wrapper = new Div();
    wrapper.setClassName("grid-wrapper");

    VerticalLayout gridLayout = new VerticalLayout();
    gridLayout.setSizeFull();

    // Lisätään suodattimet grid layoutiin
    if (filtersLayout != null) {
      gridLayout.add(filtersLayout);
    }

    // Lisätään grid itse layoutiin
    gridLayout.add(grid);

    // Lisätään erikoinen footer toteutus grid layoutiin
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
    grid.setItems(query -> asiakasService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream()
      .filter(asiakas -> filterByField(asiakas.getEtunimi(), etunimiFilter.getValue()))
      .filter(asiakas -> filterByField(asiakas.getSukunimi(), sukunimiFilter.getValue()))
      .filter(asiakas -> filterByField(asiakas.getPuhnro(), puhnroFilter.getValue()))
      .filter(asiakas -> filterByField(asiakas.getSposti(), spostiFilter.getValue()))
      .filter(asiakas -> filterByField(asiakas.getSijainti().getPaikka(), sijaintiFilter.getValue())));
  }

  // Suodatus yksittäisen kentän perusteella
  private boolean filterByField(String fieldValue, String filterValue) {
    if (filterValue == null || filterValue.isEmpty()) {
      return true;
    }
    return fieldValue != null && fieldValue.toLowerCase().contains(filterValue.toLowerCase());
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    Optional<Long> asiakasId = event.getRouteParameters().get(ASIAKAS_ID).map(Long::parseLong);
    if (asiakasId.isPresent()) {
      Optional<Asiakas> asiakasFromBackend = asiakasService.get(asiakasId.get());
      if (asiakasFromBackend.isPresent()) {
        populateForm(asiakasFromBackend.get());
      } else {
        Notification.show(String.format("Haettua asiakasta ei löytynyt, ID = %s", asiakasId.get()), 3000,
          Notification.Position.BOTTOM_START);
        // Kun rivi on valittu mutta data ei ole enää saatavilla, päivitetään grid
        refreshGrid();
        event.forwardTo(AsiakkaatView.class);
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
    etunimi = new TextField("Etunimi");
    sukunimi = new TextField("Sukunimi");
    puhnro = new TextField("Puhnro");
    sposti = new TextField("Sposti");
    sijainti = new ComboBox<>("Paikka");
    sijainti.setItems(sijaintiService.findAlltoList());
    sijainti.setItemLabelGenerator(Sijainti::getPaikka);
    formLayout.add(etunimi, sukunimi, puhnro, sposti, sijainti);

    editorDiv.add(formLayout);

    Div vali = new Div();
    vali.getStyle().set("height", "20px");
    editorDiv.add(vali);

    Span infoSpan = new Span("HUOM! Kun poistat asiakkaan tiedot, poistuvat samalla kaikki asiakkaaseen liittyvät peliapuri- ja kotipelitiedot. Paikkatieto tulee lisäämistäsi sijainneista (eli uudet kohteet/sijainnit suotavaa lisätä aina ennen varsinaisia kohteen asiakkaita).");
    infoSpan.getElement().setAttribute("theme", "badge");
    editorDiv.add(infoSpan);

    createButtonLayout(editorLayoutDiv);

    splitLayout.addToSecondary(editorLayoutDiv);
  }

  // Napit editoriin
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
  private void populateForm(Asiakas value) {
    this.asiakas = value;
    binder.readBean(this.asiakas);
  }
}