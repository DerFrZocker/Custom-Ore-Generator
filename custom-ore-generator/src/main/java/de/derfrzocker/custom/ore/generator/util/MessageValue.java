package de.derfrzocker.custom.ore.generator.util;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class MessageValue {

    @NonNull
    private final String key;

    @NonNull
    private final String value;
}