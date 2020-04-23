package com.splitoil.travel.travelplan.domain.model;

import com.splitoil.shared.AbstractEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Travel extends AbstractEntity {

    @AttributeOverride(name = "id", column = @Column(name = "lobby_id"))
    private LobbyId lobbyId;
}
