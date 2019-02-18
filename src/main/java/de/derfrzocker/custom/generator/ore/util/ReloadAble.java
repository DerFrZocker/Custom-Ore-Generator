package de.derfrzocker.custom.generator.ore.util;

import java.util.HashSet;
import java.util.Set;

public interface ReloadAble {

    Set<ReloadAble> RELOAD_ABLES = new HashSet<>();

    void reload();

}
