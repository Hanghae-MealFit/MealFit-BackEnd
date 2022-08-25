package com.mealfit.admin;

import com.mealfit.admin.view.CommentView;
import com.mealfit.admin.view.LogView;
import com.mealfit.admin.view.PostView;
import com.mealfit.admin.view.UserView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

public class MenuLayout extends AppLayout {

    public MenuLayout() {
        addToNavbar(createMenuTitle(), createTabs());
    }

    private H1 createMenuTitle() {
        H1 title = new H1("MEAL-FIT Admin");
        title.getStyle()
              .set("font-size", "var(--lumo-font-size-l)")
              .set("left", "var(--lumo-space-l)")
              .set("margin", "0")
              .set("position", "absolute");
        return title;
    }

    private Tabs createTabs() {
        Tabs tabs = new Tabs();
        tabs.getStyle().set("margin", "auto");
        tabs.add(
                createTab(VaadinIcon.USER, "회원", UserView.class),
                createTab(VaadinIcon.PIN_POST, "게시글", PostView.class),
                createTab(VaadinIcon.COMMENT, "댓글", CommentView.class),
                createTab(VaadinIcon.INFO, "음식", CommentView.class),
                createTab(VaadinIcon.BUG, "로그", LogView.class)
        );
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class<? extends Component> navigateTarget) {
        Icon icon = viewIcon.create();
        icon.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");
        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setRoute(navigateTarget);
        link.setTabIndex(-1);
        return new Tab(link);
    }
}
