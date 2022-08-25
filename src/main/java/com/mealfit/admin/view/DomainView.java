package com.mealfit.admin.view;

import com.mealfit.admin.service.VaddinService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import java.util.List;

public abstract class DomainView extends VerticalLayout {

    protected <T> HorizontalLayout createSearchLayout(List<String> keyList, VaddinService<?> vaddinService) {
        HorizontalLayout searchLayout = new HorizontalLayout();
        Select<String> searchKeySelector = new Select<>();
        searchKeySelector.setItems(keyList);
        TextField searchTextField = createTextField("검색어를 입력하세요");
        Button searchButton = new Button("검색");
        searchButton.addClickListener(event -> searchResourceById(
              vaddinService, searchKeySelector, searchTextField));

        searchLayout.add(searchKeySelector, searchTextField, searchButton);

        return searchLayout;
    }

    protected <T> void searchResourceById(VaddinService<?> vaddinService, Select<String> select,
          TextField textField) {
        try {
            String key = String.valueOf(select.getValue());
            if (key.endsWith("ID")) {
                vaddinService.findByKeyAndId(key, Long.parseLong(textField.getValue()));
            }
            vaddinService.findByKeyAndValue(key, textField.getValue());
            UI.getCurrent().getPage().reload();
        }
        catch (Exception exception) {
            Notification.show(exception.getMessage(), 3000, Position.BOTTOM_END);
        }
    }

    protected <T> HorizontalLayout createDeleteLayout(VaddinService<?> vaddinService) {
        HorizontalLayout deleteLayout = new HorizontalLayout();
        TextField deleteTextField = createTextField("삭제할 id");
        Button deleteButton = new Button("삭제");
        deleteButton.addClickListener(event -> deleteResourceById(vaddinService, deleteTextField));
        deleteLayout.add(deleteTextField, deleteButton);
        return deleteLayout;
    }

    protected <T> void deleteResourceById(VaddinService<?> vaddinService, TextField deleteTextField) {
        try {
            vaddinService.deleteById(Long.parseLong(deleteTextField.getValue()));
            UI.getCurrent().getPage().reload();
        } catch (Exception exception) {
            Notification.show(exception.getMessage(), 3000, Position.BOTTOM_END);
        }
    }

    protected TextField createTextField(String value) {
        TextField emailField = new TextField();
        emailField.setPlaceholder(value);
        return emailField;
    }

    protected HorizontalLayout createFooterLayout() {
        HorizontalLayout footerLayout = new HorizontalLayout();
        Footer footer = new Footer(new Span("Copyright ⓒ 2022 - 2022 Team MealFit. All rights reserved."));
        footerLayout.add(footer);
        footerLayout.setWidthFull();
        footerLayout.setHeight(66, Unit.PIXELS);
        footerLayout.setAlignItems(Alignment.END);
        footerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        return footerLayout;
    }

    protected void arrangeComponents() {
        setMargin(true);
        setPadding(true);
        setSpacing(true);
        setWidth(97.9f, Unit.PERCENTAGE);
    }
}
