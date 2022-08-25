package com.mealfit.admin.view;

import com.mealfit.admin.MenuLayout;
import com.mealfit.admin.service.VaddinService;
import com.mealfit.comment.domain.Comment;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.List;
import javax.annotation.security.RolesAllowed;

@PageTitle("댓글 페이지")
@Route(value = "/admin/comment", layout = MenuLayout.class)
@RolesAllowed("ADMIN")
public class CommentView extends DomainView {

    private final VaddinService<Comment> vaddinCommentService;
    private final static String RESOURCE_NAME = "댓글";

    public CommentView(VaddinService<Comment> vaddinCommentService) {
        this.vaddinCommentService = vaddinCommentService;
        add(
              new H3("모든 " + RESOURCE_NAME),
              createSearchLayout(List.of("USER_ID", "POST_ID"), vaddinCommentService),
              createCommentsLayout(),
              new H3(RESOURCE_NAME + " 삭제"),
              createDeleteLayout(vaddinCommentService),
              createFooterLayout()
        );
        arrangeComponents();
    }

    private Grid<Comment> createCommentsLayout() {
        Grid<Comment> grid = new Grid<>();
        grid.setItems(vaddinCommentService.findAll());
        grid.addColumn(Comment::getId).setHeader("댓글 ID").setSortable(true);
        grid.addColumn(Comment::getUserId).setHeader("회원 ID").setSortable(true);
        grid.addColumn(Comment::getPostId).setHeader("게시글 ID").setSortable(true);
        grid.addColumn(Comment::getContent).setHeader("본문").setSortable(true);
        grid.addColumn(Comment::getLikeIt).setHeader("좋아요").setSortable(true);
        grid.addColumn(Comment::getCreatedAt).setHeader("게시일").setSortable(true);
        grid.addColumn(Comment::getCreatedBy).setHeader("게시자").setSortable(true);
        grid.addColumn(Comment::getUpdatedAt).setHeader("갱신일").setSortable(true);
        grid.addColumn(Comment::getUpdatedBy).setHeader("갱신자").setSortable(true);
        grid.setColumnOrder(grid.getColumns());
        return grid;
    }
}
