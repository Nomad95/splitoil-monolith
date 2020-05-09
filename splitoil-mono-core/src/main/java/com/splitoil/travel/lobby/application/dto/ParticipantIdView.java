package com.splitoil.travel.lobby.application.dto;

import lombok.NonNull;
import lombok.Value;

import java.util.UUID;

@Value
public class ParticipantIdView {
    @NonNull UUID id;
}
