package com.sparta.publicclassdev.domain.winners.entity;

import com.sparta.publicclassdev.domain.codekatas.entity.CodeKatas;
import com.sparta.publicclassdev.domain.teams.entity.Teams;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "winners")
public class Winners {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private double responseTime;

    @ManyToOne
    @JoinColumn(name = "codekatas_id")
    private CodeKatas codeKatas;

    @ManyToOne
    @JoinColumn(name = "teams_id")
    private Teams teams;
}
