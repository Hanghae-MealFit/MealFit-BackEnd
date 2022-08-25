package com.mealfit.admin.view;

import com.mealfit.admin.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import javax.annotation.security.PermitAll;

@PageTitle("MEAL-FIT Admin")
@Route(value = "/admin", layout = MainLayout.class)
@Theme(themeClass = Lumo.class, variant = Lumo.DARK)
@PermitAll
public class MainView extends VerticalLayout implements AppShellConfigurator {

    public MainView() {
        add(
              new H1("MEAL-FIT 관리자 페이지입니다.")
        );
        arrangeComponent();
    }

    private void arrangeComponent() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }
}