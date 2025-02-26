package com.example.application.views;

import com.example.application.views.admin.AdminView;
import com.example.application.views.asiakkaat.AsiakkaatView;
import com.example.application.views.koti.KotiView;
import com.example.application.views.kotipelit.KotipelitView;
import com.example.application.views.peliapuri.PeliapuriView;
import com.example.application.views.pelit.PelitView;
import com.example.application.views.sijainnit.SijainnitView;
import com.example.application.views.kayttaja.KayttajaView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems;
import com.vaadin.flow.theme.lumo.LumoUtility.BoxSizing;
import com.vaadin.flow.theme.lumo.LumoUtility.Display;
import com.vaadin.flow.theme.lumo.LumoUtility.FlexDirection;
import com.vaadin.flow.theme.lumo.LumoUtility.FontSize;
import com.vaadin.flow.theme.lumo.LumoUtility.FontWeight;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Height;
import com.vaadin.flow.theme.lumo.LumoUtility.ListStyleType;
import com.vaadin.flow.theme.lumo.LumoUtility.Margin;
import com.vaadin.flow.theme.lumo.LumoUtility.Overflow;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.theme.lumo.LumoUtility.TextColor;
import com.vaadin.flow.theme.lumo.LumoUtility.Whitespace;
import com.vaadin.flow.theme.lumo.LumoUtility.Width;

import jakarta.annotation.security.PermitAll;

import org.vaadin.lineawesome.LineAwesomeIcon;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;


// Päänäkymä, joka toimii muille näkymille pohjana/pääasetteluna
@Layout
@PermitAll
@AnonymousAllowed
public class MainLayout extends AppLayout {

  // Simppeli kohteiden navigointikomponentti, joka perustuu ListItem-elementtiin
  public static class MenuItemInfo extends ListItem {

    private final Class<? extends Component> view;

    public MenuItemInfo(String menuTitle, Component icon, Class<? extends Component> view) {
      this.view = view;
      RouterLink link = new RouterLink();
      // Tyylittely LUMO-luokilla
      link.addClassNames(Display.FLEX, Gap.XSMALL, Height.MEDIUM, AlignItems.CENTER, Padding.Horizontal.SMALL,
        TextColor.BODY);
      link.setRoute(view);

      Span text = new Span(menuTitle);
      text.addClassNames(FontWeight.MEDIUM, FontSize.MEDIUM, Whitespace.NOWRAP);

      if (icon != null) {
        link.add(icon);
      }
      link.add(text);
      add(link);
    }

    public Class<?> getView() {
      return view;
    }
  }

  public MainLayout() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {
      addToNavbar(createHeaderContent());
    }
  }

  // Luodaan headerin sisältö
  private Component createHeaderContent() {
    Header header = new Header();
    header.addClassNames(BoxSizing.BORDER, Display.FLEX, FlexDirection.COLUMN, Width.FULL);

    Div layout = new Div();
    layout.addClassNames(Display.FLEX, AlignItems.CENTER, Padding.Horizontal.LARGE);

    Image logo = new Image("images/PAAlogo.png", "Peliapuriapp logo");
    logo.addClassNames(Margin.Vertical.MEDIUM, Margin.End.SMALL, Height.AUTO);
    layout.add(logo);

    Nav nav = new Nav();
    nav.addClassNames(Display.FLEX, Overflow.AUTO, Padding.Horizontal.MEDIUM, Padding.Vertical.XSMALL);

    UnorderedList list = new UnorderedList();
    list.addClassNames(Display.FLEX, Gap.SMALL, ListStyleType.NONE, Margin.NONE, Padding.NONE);
    nav.add(list);

    for (MenuItemInfo menuItem : createMenuItems()) {
      list.add(menuItem);
    }

    Button vaihdaButton = new Button("Vaihda käyttäjää", event -> {
      getUI().ifPresent(ui -> ui.getPage().setLocation("/login"));
    });
    layout.add(vaihdaButton);

    header.add(layout, nav);
    return header;
  }

  // Luodaan valikkokohteet ja huomioidaan oletko kirjautunut peruskäyttäjänä vai adminina
  // Vain ADMIN-rooli näkee Admin-valikon
  private MenuItemInfo[] createMenuItems() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    boolean isAdmin = authentication.getAuthorities().stream()
      .map(GrantedAuthority::getAuthority)
      .anyMatch(role -> role.equals("ROLE_ADMIN"));

    return isAdmin ? new MenuItemInfo[]{
      new MenuItemInfo("Koti", LineAwesomeIcon.HOME_SOLID.create(), KotiView.class),
      new MenuItemInfo("Asiakkaat", LineAwesomeIcon.USER.create(), AsiakkaatView.class),
      new MenuItemInfo("Sijainnit", LineAwesomeIcon.MAP_MARKER_SOLID.create(), SijainnitView.class),
      new MenuItemInfo("Pelit", LineAwesomeIcon.TH_LIST_SOLID.create(), PelitView.class),
      new MenuItemInfo("Peliapuri", LineAwesomeIcon.LIST_ALT_SOLID.create(), PeliapuriView.class),
      new MenuItemInfo("Kotipelit", LineAwesomeIcon.LIST_SOLID.create(), KotipelitView.class),
      new MenuItemInfo("Käyttäjät", LineAwesomeIcon.LIST_SOLID.create(), KayttajaView.class),
      new MenuItemInfo("Admin", LineAwesomeIcon.SKULL_CROSSBONES_SOLID.create(), AdminView.class),
    } : new MenuItemInfo[]{
      new MenuItemInfo("Koti", LineAwesomeIcon.HOME_SOLID.create(), KotiView.class),
      new MenuItemInfo("Asiakkaat", LineAwesomeIcon.USER.create(), AsiakkaatView.class),
      new MenuItemInfo("Sijainnit", LineAwesomeIcon.MAP_MARKER_SOLID.create(), SijainnitView.class),
      new MenuItemInfo("Pelit", LineAwesomeIcon.TH_LIST_SOLID.create(), PelitView.class),
      new MenuItemInfo("Peliapuri", LineAwesomeIcon.LIST_ALT_SOLID.create(), PeliapuriView.class),
      new MenuItemInfo("Kotipelit", LineAwesomeIcon.LIST_SOLID.create(), KotipelitView.class),
      new MenuItemInfo("Käyttäjät", LineAwesomeIcon.LIST_SOLID.create(), KayttajaView.class),
    };
  }
}