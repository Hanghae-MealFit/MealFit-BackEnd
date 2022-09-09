package com.mealfit.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealfit.bodyInfo.application.BodyInfoService;
import com.mealfit.comment.presentation.CommentController;
import com.mealfit.comment.application.CommentService;
import com.mealfit.common.storageService.StorageService;
import com.mealfit.post.application.PostReadService;
import com.mealfit.post.presentation.PostController;
import com.mealfit.post.application.PostService;
import com.mealfit.post.presentation.PostReadController;
import com.mealfit.user.application.EmailEventHandler;
import com.mealfit.user.application.UserService;
import com.mealfit.user.presentation.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(value = {
      UserController.class,
      PostController.class,
      PostReadController.class,
      CommentController.class,
}, excludeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@ActiveProfiles("test")
public abstract class ControllerTest {

    @MockBean
    protected JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockBean
    protected BodyInfoService bodyInfoService;

    @MockBean
    protected EmailEventHandler emailEventHandler;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected StorageService storageService;

    @MockBean
    protected PostService postService;

    @MockBean
    protected PostReadService postReadService;

    @MockBean
    protected UserService userService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}

