package com.mealfit.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealfit.comment.controller.CommentController;
import com.mealfit.comment.service.CommentService;
import com.mealfit.post.controller.PostController;
import com.mealfit.post.service.PostService;
import com.mealfit.user.controller.UserController;
import com.mealfit.user.controller.UserInfoController;
import com.mealfit.user.service.UserInfoService;
import com.mealfit.user.service.UserSignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest({
      UserController.class,
      UserInfoController.class,
      PostController.class,
      CommentController.class,
})
@ActiveProfiles("test")
public abstract class ControllerTest {

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected PostService postService;

    @MockBean
    protected UserInfoService userInfoService;

    @MockBean
    protected UserSignUpService userSignUpService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
