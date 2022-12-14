package com.mealfit.common.factory;

import com.mealfit.post.domain.Post;
import com.mealfit.post.domain.PostImage;
import com.mealfit.user.domain.User;
import java.util.ArrayList;
import java.util.List;

public class PostFactory {

    private PostFactory() {
    }

    public static class Builder {

    }

    public static Post simplePost(long postId, User user, String content) {
        return Post.builder()
              .id(postId)
              .user(user)
              .content(content)
              .images(new ArrayList<>())
              .build();
    }

    public static Post imagePost(long postId, User user, String content, List<PostImage> images) {
        return Post.builder()
              .id(postId)
              .user(user)
              .content(content)
              .images(images)
              .likeIt(0)
              .view(0)
              .build();
    }
}
