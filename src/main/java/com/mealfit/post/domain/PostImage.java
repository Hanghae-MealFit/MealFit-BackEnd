package com.mealfit.post.domain;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@ToString(exclude = "post")
@Entity
@Getter
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @Setter
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private boolean isRepresentative;

    protected PostImage() {
    }

    public PostImage(String url) {
        this.url = url;
    }

}