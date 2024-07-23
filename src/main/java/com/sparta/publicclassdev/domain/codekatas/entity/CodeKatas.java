package com.sparta.publicclassdev.domain.codekatas.entity;

import com.sparta.publicclassdev.domain.winners.entity.Winners;
import com.sparta.publicclassdev.global.entity.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.cglib.core.Local;

@Entity
@Table(name = "codeKatas")
public class CodeKatas extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contents;

    @OneToMany(mappedBy = "codeKatas")
    private List<Winners> winners;
}
