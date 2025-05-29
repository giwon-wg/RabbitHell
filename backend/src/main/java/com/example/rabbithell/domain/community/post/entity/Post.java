package com.example.rabbithell.domain.community.post.entity;

import com.example.rabbithell.common.audit.BaseEntity;
import com.example.rabbithell.domain.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    //todo 코멘트 작성 후 만들예정
    @Column(nullable = false)
    private Integer commentCount;

    @Column(nullable = false)
    private Boolean isDeleted;

    public void markAsDelete() {
        this.isDeleted = true;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        if (this.commentCount <= 0) {
            throw new IllegalArgumentException("댓글 수 음수 불가, 커스텀 예외 설계후 변경 예정");
        }
        this.commentCount--;
    }
}
