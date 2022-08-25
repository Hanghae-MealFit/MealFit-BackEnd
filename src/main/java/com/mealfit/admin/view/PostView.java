package com.mealfit.admin.view;

import com.mealfit.admin.MenuLayout;
import com.mealfit.admin.service.VaddinService;
import com.mealfit.post.domain.Post;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;

@PageTitle("게시글페이지")
@Route(value = "/admin/post", layout = MenuLayout.class)
@RolesAllowed("ADMIN")
public class PostView extends DomainView {

    private final VaddinService<Post> vaddinPostService;
    private final static String RESOURCE_NAME = "게시글";

    public PostView(VaddinService<Post> vaddinPostService) {
        this.vaddinPostService = vaddinPostService;
        add(
              new H3("모든 " + RESOURCE_NAME),
              createPostsLayout(),
              new H3(RESOURCE_NAME + " 생성"),
              createSaveLayout(),
              new H3(RESOURCE_NAME + " 삭제"),
              createDeleteLayout(vaddinPostService),
              createFooterLayout()
        );
        arrangeComponents();
    }

    private Grid<Post> createPostsLayout() {
        Grid<Post> grid = new Grid<>();
        grid.setItems(vaddinPostService.findAll());
        FooterRow footerRow = grid.appendFooterRow();
        grid.addColumn(Post::getId).setHeader("게시글 ID").setSortable(true);
        grid.addColumn(Post::getUserId).setHeader("회원 ID").setSortable(true);
        grid.addColumn(Post::getContent).setHeader("본문").setSortable(true);
        grid.addColumn(Post::getView).setHeader("조회수").setSortable(true);
        grid.addColumn(Post::getLikeIt).setHeader("좋아요").setSortable(true);
        grid.addColumn(Post::getCreatedAt).setHeader("게시일").setSortable(true);
        grid.addColumn(Post::getCreatedBy).setHeader("게시자").setSortable(true);
        grid.addColumn(Post::getUpdatedAt).setHeader("갱신일").setSortable(true);
        grid.addColumn(Post::getUpdatedBy).setHeader("갱신자").setSortable(true);
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

    private Button createSaveButton(
          TextField emailField,
          TextField nicknameField,
          TextField introductionField,
          TextField pictureField) {
        Button saveButton = new Button("생성");
        saveButton.addClickListener(
              event -> savePost(emailField, nicknameField, introductionField, pictureField)
        );
        return saveButton;
    }

    private void savePost(
          TextField nicknameField,
          TextField emailField,
          TextField providerTypeField,
          TextField userStatusField) {
        try {
            vaddinPostService.save(new Post());
            UI.getCurrent().getPage().reload();
        } catch (Exception exception) {
            Notification.show(exception.getMessage(), 3000, Position.BOTTOM_END);
        }
    }
}
