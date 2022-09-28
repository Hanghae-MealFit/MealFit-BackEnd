package com.mealfit.post.query.dao;

import com.mealfit.post.query.dto.PostDetailView;
import org.springframework.data.repository.Repository;

public interface PostViewDao extends Repository<PostDetailView, Long> {

}
