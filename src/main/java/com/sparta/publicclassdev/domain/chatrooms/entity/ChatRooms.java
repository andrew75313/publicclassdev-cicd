package com.sparta.publicclassdev.domain.chatrooms.entity;

import com.sparta.publicclassdev.domain.teams.entity.Teams;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "chatrooms")
public class ChatRooms {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teams_id")
    private Teams teams;

    @OneToMany(mappedBy = "chatRooms")
    private List<ChatRoomUsers> chatRoomUsers;

    @OneToMany(mappedBy = "chatRooms")
    private List<Messages> messages;

    public void setTeams(Teams teams) {
    }
}
