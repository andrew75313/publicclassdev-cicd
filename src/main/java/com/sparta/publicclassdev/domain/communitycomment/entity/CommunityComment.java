package com.sparta.publicclassdev.domain.communitycomment.entity;

import com.sparta.publicclassdev.domain.community.entity.Community;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.global.entity.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "communitycomments")
@NoArgsConstructor
public class CommunityComment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "communities_id")
    private Community community;

    @Builder
    public CommunityComment(String content){
        this.content = content;
    }
}
