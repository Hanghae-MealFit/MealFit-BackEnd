package com.mealfit.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mealfit.bodyInfo.application.BodyInfoService;
import com.mealfit.bodyInfo.presentation.BodyInfoController;
import com.mealfit.comment.application.CommentService;
import com.mealfit.comment.presentation.CommentController;
import com.mealfit.common.storageService.StorageService;
import com.mealfit.diet.application.DietService;
import com.mealfit.diet.presentation.DietController;
import com.mealfit.food.application.FoodService;
import com.mealfit.food.presentation.FoodController;
import com.mealfit.post.application.PostReadService;
import com.mealfit.post.application.PostService;
import com.mealfit.post.presentation.PostController;
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
      BodyInfoController.class,
      UserController.class,
      PostController.class,
      PostReadController.class,
      CommentController.class,
      FoodController.class,
      DietController.class
}, excludeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
@ActiveProfiles("test")
public abstract class ControllerTest {

    @MockBean
    protected JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @MockBean
    protected EmailEventHandler emailEventHandler;

    @MockBean
    protected StorageService storageService;

    @MockBean
    protected UserService userService;

    @MockBean
    protected BodyInfoService bodyInfoService;

    @MockBean
    protected PostService postService;

    @MockBean
    protected PostReadService postReadService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected FoodService foodService;

    @MockBean
    protected DietService dietService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}

