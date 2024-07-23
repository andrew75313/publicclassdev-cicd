package com.sparta.publicclassdev.domain.teams.entity;

import com.sparta.publicclassdev.domain.chatrooms.entity.ChatRooms;
import com.sparta.publicclassdev.domain.users.entity.Users;
import com.sparta.publicclassdev.domain.winners.entity.Winners;
import com.sparta.publicclassdev.global.entity.Timestamped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "teams")
public class Teams extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "teams")
    private List<TeamUsers> teamUsers;

    @OneToMany(mappedBy = "teams")
    private List<ChatRooms> chatRooms;

    @OneToMany(mappedBy = "teams")
    private List<Winners> winners;
}
