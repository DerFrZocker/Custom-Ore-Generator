package de.derfrzocker.custom.ore.generator.api;

import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.regex.Pattern;

public final class OreSetting {

    private static final Map<String, OreSetting> ORE_SETTINGS = Collections.synchronizedMap(new HashMap<>());
    private static final Pattern NAME_PATTER = Pattern.compile("^[A-Z_]*$");

    /**
     * @param name of the OreSetting
     * @return the OreSetting with the given name or null if not present
     * @throws IllegalArgumentException if name is null
     */
    @Nullable
    public static OreSetting getOreSetting(@NotNull final String name) {
        Validate.notNull(name, "Name can't be null");

        return ORE_SETTINGS.get(name);
    }

    /**
     * Create a new OreSetting with the given name and save value
     * If a OreSetting with the same name already exist, it return's the already
     * existing one
     * The name of the OreSetting must match the following Regex: ^[A-Z_]*$
     * The name can be empty
     *
     * @param name      of the OreSetting
     * @param saveValue the minimum value which the OreSetting can have
     * @return a new OreSetting with the given name and save value, or an already existing one
     * @throws IllegalArgumentException if name is null
     * @throws IllegalArgumentException if name is empty
     * @throws IllegalArgumentException if the name doesn't match the following Regex: ^[A-Z_]*$
     */
    @NotNull
    public static OreSetting createOreSetting(@NotNull final String name, final int saveValue) {
        Validate.notNull(name, "Name can't be null");

        return ORE_SETTINGS.computeIfAbsent(name, name2 -> new OreSetting(name, saveValue));
    }

    /**
     * @return a new Set with all OreSettings
     */
    public static Set<OreSetting> getOreSettings() {
        return new HashSet<>(ORE_SETTINGS.values());
    }

    @NotNull
    private final String name;
    private final int saveValue;

    private OreSetting(@NotNull final String name, final int saveValue) {
        Validate.notNull(name, "Name can't be null");
        Validate.notEmpty(name, "Name can't be empty");
        Validate.isTrue(NAME_PATTER.matcher(name).matches(), "Name " + name + " doesn't match the regex: ^[A-Z_]*$");

        this.name = name;
        this.saveValue = saveValue;
    }

    /**
     * The name of this OreSetting is not empty,
     * and match the following Regex: ^[A-Z_]*$
     *
     * @return the name of this OreSetting
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * @return the save value of this OreSetting
     */
    public int getSaveValue() {
        return saveValue;
    }

    @Override
    public boolean equals(@Nullable final Object obj) {
        if (!(obj instanceof OreSetting))
            return false;

        if (this == obj)
            return true;

        final OreSetting other = (OreSetting) obj;

        return other.getName().equals(getName()) && other.getSaveValue() == getSaveValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, saveValue);
    }

}
