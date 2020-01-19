package com.splitoil.shared;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class NullObjectDto {

    public static final NullObjectDto INSTANCE = new NullObjectDto();
}
