package com.mealfit.post.domain;


import com.mealfit.common.baseEntity.BaseEntity;
import com.mealfit.exception.user.UserNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Generated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.annotations.DynamicUpdate;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column
    private String content;

    //조회수
    @Column
    private int view;

    @Column
    private int likeIt;

    @Exclude
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY,
          cascade = CascadeType.PERSIST,
          orphanRemoval = true)
    private List<PostImage> images = new ArrayList<>();

    public Post(String content) {
        this.content = content;
        this.likeIt = 0;
        this.view = 0;
    }

    public void settingUserInfo(Long userId) {
        if (userId == null) {
            throw new UserNotFoundException("회원이 없습니다.");
        }

        this.userId = userId;
    }

    public void addPostImages(List<PostImage> images) {

        for (PostImage postImage : images) {
            addPostImage(postImage);
        }
    }

    public void replacePostImages(List<PostImage> images) {
        this.images.clear();

        for (PostImage postImage : images) {
            addPostImage(postImage);
        }
    }

    private void addPostImage(PostImage postImage) {
        postImage.setPost(this);
        this.images.add(postImage);
    }

    public void updateContent(String content) {
        this.content = content;
    }

    @Generated
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Generated
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Builder
    public Post(Long id, Long userId, String content, int view,
          int likeIt, List<PostImage> images) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.view = view;
        this.likeIt = likeIt;
        this.images = images;
    }

    public List<String> getImageUrls() {
        List<String> imageUrls = new ArrayList<>();

        for (PostImage image : this.images) {
            imageUrls.add(image.getUrl());
        }

        return imageUrls;
    }

    public void like() {
        this.likeIt += 1;
    }

    public void unLike() {
        this.likeIt -= 1;
    }

    public void plusView() {
        this.view += 1;
    }
}