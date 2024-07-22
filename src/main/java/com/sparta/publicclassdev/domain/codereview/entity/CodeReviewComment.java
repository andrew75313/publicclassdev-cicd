package com.sparta.publicclassdev.domain.codereview.entity;

<<<<<<< HEAD
import com.sparta.publicclassdev.domain.user.entity.User;
=======
import com.sparta.publicclassdev.domain.users.entity.Users;
>>>>>>> bdf17a491cfadc0e8e226db4bc0c4e74d00e8b85
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "codereviewcomments")
public class CodeReviewComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne
    @JoinColumn(name = "code_review_id")
    private CodeReviews codeReviews;
}
