package com.mealfit.admin.service;

import com.mealfit.post.domain.Post;
import com.mealfit.post.repository.PostRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class VaddinPostService implements VaddinService<Post>{

    private final PostRepository postRepository;

    public VaddinPostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> findByKeyAndValue(String key, String value) {
        return null;
    }

    @Override
    public List<Post> findByKeyAndId(String key, Long id) {
        return null;
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> findByUserId(Long id) {
        return postRepository.findByUserIdOrderByCreatedAtDesc(id);
    }
}
