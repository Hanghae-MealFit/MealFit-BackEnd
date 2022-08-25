package com.mealfit.admin.view;

import com.mealfit.admin.MenuLayout;
import com.mealfit.admin.service.VaddinService;
import com.mealfit.user.domain.User;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.List;
import java.util.Set;
import javax.annotation.security.RolesAllowed;

@PageTitle("회원페이지")
@Route(value = "/admin/user", layout = MenuLayout.class)
@RolesAllowed("ADMIN")
public class UserView extends DomainView {

    private final VaddinService<User> vaddinUserService;
    private final static String RESOURCE_NAME = "회원";

    public UserView(VaddinService<User> vaddinUserService) {
        this.vaddinUserService = vaddinUserService;
        add(
              new H3("모든 " + RESOURCE_NAME),
              createMembersLayout(),
              new H3(RESOURCE_NAME + " 생성"),
              createSaveLayout(),
              new H3(RESOURCE_NAME + " 삭제"),
              createDeleteLayout(vaddinUserService),
              createFooterLayout()
        );
        arrangeComponents();
    }

    private Grid<User> createMembersLayout() {
        Grid<User> grid = new Grid<>();
        List<User> users = vaddinUserService.findAll();
        grid.setSelectionMode(SelectionMode.MULTI);
        grid.setItems(users);
        grid.addColumn(User::getId).setHeader("회원_ID").setSortable(true);
        grid.addColumn(User::getNickname).setHeader("닉네임").setSortable(true);
        grid.addColumn(User::getEmail).setHeader("이메일").setSortable(true);
        grid.addColumn(User::getProviderType).setHeader("회원가입 종류").setSortable(true);
        grid.addColumn(User::getUserStatus).setHeader("상태").setSortable(true);

        grid.addSelectionListener(event -> {
            Set<User> selected = event.getAllSelectedItems();
            Notification.show(selected.size() + " items selected");
        });



        return grid;
    }

    private HorizontalLayout createSaveLayout() {
        HorizontalLayout saveLayout = new HorizontalLayout();
        HorizontalLayout saveForm = new HorizontalLayout();
        TextField emailField = createTextField("이메일");
        TextField nicknameField = createTextField("닉네임");
        TextField providerTypeField = createTextField("회원가입 종류");
        TextField userStatusField = createTextField("상태");
        saveForm.add(emailField, nicknameField, providerTypeField, userStatusField);
        saveLayout.add(saveForm,
              createSaveButton(emailField, nicknameField, providerTypeField, userStatusField));
        return saveLayout;
    }

    private Button createSaveButton(TextField emailField, TextField nicknameField,
          TextField introductionField,
          TextField pictureField) {
        Button saveButton = new Button("생성");
        saveButton.addClickListener(event ->
              saveUser(emailField, nicknameField, introductionField, pictureField)
        );
        return saveButton;
    }

    private void saveUser(TextField nicknameField,
          TextField emailField,
          TextField providerTypeField,
          TextField userStatusField) {
        try {
            vaddinUserService.save(
                  new User(null, null, null)
            );
            UI.getCurrent().getPage().reload();
        } catch (Exception exception) {
            Notification.show(exception.getMessage(), 3000, Position.BOTTOM_END);
        }
    }
}
