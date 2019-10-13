package de.derfrzocker.custom.ore.generator;

import de.derfrzocker.spigot.utils.message.MessageKey;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class CustomOreGeneratorMessages {

    // general command
    public final MessageKey COMMAND_WORLD_NOT_FOUND;
    public final MessageKey COMMAND_ORE_GENERATOR_NOT_FOUND;
    public final MessageKey COMMAND_BLOCK_SELECTOR_NOT_FOUND;
    public final MessageKey COMMAND_MATERIAL_NOT_FOUND;
    public final MessageKey COMMAND_MATERIAL_NO_BLOCK;
    public final MessageKey COMMAND_ORE_CONFIG_NOT_FOUND;
    public final MessageKey COMMAND_BIOME_NOT_FOUND;

    // create command
    public final MessageKey COMMAND_CREATE_USAGE;
    public final MessageKey COMMAND_CREATE_DESCRIPTION;
    public final MessageKey COMMAND_CREATE_NOT_ENOUGH_ARGS;
    public final MessageKey COMMAND_CREATE_ALREADY_EXISTS;
    public final MessageKey COMMAND_CREATE_NAME_NOT_VALID;
    public final MessageKey COMMAND_CREATE_ORE_GENERATOR_NOT_SPECIFIED;
    public final MessageKey COMMAND_CREATE_BLOCK_SELECTOR_NOT_SPECIFIED;
    public final MessageKey COMMAND_CREATE_SUCCESS;

    // reload command
    public final MessageKey COMMAND_RELOAD_USAGE;
    public final MessageKey COMMAND_RELOAD_DESCRIPTION;
    public final MessageKey COMMAND_RELOAD_BEGIN;
    public final MessageKey COMMAND_RELOAD_END;

    // set command
    public final MessageKey COMMAND_SET_USAGE;
    public final MessageKey COMMAND_SET_DESCRIPTION;

    // set value command
    public final MessageKey COMMAND_SET_VALUE_USAGE;
    public final MessageKey COMMAND_SET_VALUE_DESCRIPTION;
    public final MessageKey COMMAND_SET_VALUE_NOT_ENOUGH_ARGS;
    public final MessageKey COMMAND_SET_VALUE_SETTING_NOT_FOUND;
    public final MessageKey COMMAND_SET_VALUE_SETTING_NOT_VALID;
    public final MessageKey COMMAND_SET_VALUE_VALUE_NOT_VALID;
    public final MessageKey COMMAND_SET_VALUE_SUCCESS;

    // set customdata command
    public final MessageKey COMMAND_SET_CUSTOMDATA_USAGE;
    public final MessageKey COMMAND_SET_CUSTOMDATA_DESCRIPTION;
    public final MessageKey COMMAND_SET_CUSTOMDATA_NOT_ENOUGH_ARGS;
    public final MessageKey COMMAND_SET_CUSTOMDATA_NOT_FOUND;
    public final MessageKey COMMAND_SET_CUSTOMDATA_ORE_CONFIG_NOT_VALID;
    public final MessageKey COMMAND_SET_CUSTOMDATA_VALUE_NOT_VALID;
    public final MessageKey COMMAND_SET_CUSTOMDATA_SUCCESS;

    // set biome command
    public final MessageKey COMMAND_SET_BIOME_USAGE;
    public final MessageKey COMMAND_SET_BIOME_DESCRIPTION;
    public final MessageKey COMMAND_SET_BIOME_NOT_ENOUGH_ARGS;
    public final MessageKey COMMAND_SET_BIOME_SUCCESS;

    // set replacematerial command
    public final MessageKey COMMAND_SET_REPLACEMATERIAL_USAGE;
    public final MessageKey COMMAND_SET_REPLACEMATERIAL_DESCRIPTION;
    public final MessageKey COMMAND_SET_REPLACEMATERIAL_NOT_ENOUGH_ARGS;
    public final MessageKey COMMAND_SET_REPLACEMATERIAL_SUCCESS;

    // help command
    public final MessageKey COMMAND_HELP_USAGE;
    public final MessageKey COMMAND_SET_HELP_USAGE;
    public final MessageKey COMMAND_HELP_DESCRIPTION;
    public final MessageKey COMMAND_HELP_HEADER_FORMAT;
    public final MessageKey COMMAND_HELP_FOOTER_FORMAT;
    public final MessageKey COMMAND_HELP_SEPARATOR_FORMAT;
    public final MessageKey COMMAND_HELP_PERMISSION_FORMAT;
    public final MessageKey COMMAND_HELP_USAGE_FORMAT;
    public final MessageKey COMMAND_HELP_DESCRIPTION_FORMAT;
    public final MessageKey COMMAND_HELP_SHORT_FORMAT;


    CustomOreGeneratorMessages(@NotNull final JavaPlugin javaPlugin) {
        Validate.notNull(javaPlugin, "JavaPlugin can not be null");

        COMMAND_WORLD_NOT_FOUND = new MessageKey(javaPlugin, "command.world-not-found");
        COMMAND_ORE_GENERATOR_NOT_FOUND = new MessageKey(javaPlugin, "command.ore-generator-not-found");
        COMMAND_BLOCK_SELECTOR_NOT_FOUND = new MessageKey(javaPlugin, "command.block-selector-not-found");
        COMMAND_MATERIAL_NOT_FOUND = new MessageKey(javaPlugin, "command.material-not-found");
        COMMAND_MATERIAL_NO_BLOCK = new MessageKey(javaPlugin, "command.material-no-block");
        COMMAND_ORE_CONFIG_NOT_FOUND = new MessageKey(javaPlugin, "command.ore-config-not-found");
        COMMAND_BIOME_NOT_FOUND = new MessageKey(javaPlugin, "command.biome-not-found");

        // create command
        COMMAND_CREATE_USAGE = new MessageKey(javaPlugin, "command.create.usage");
        COMMAND_CREATE_DESCRIPTION = new MessageKey(javaPlugin, "command.create.description");
        COMMAND_CREATE_NOT_ENOUGH_ARGS = new MessageKey(javaPlugin, "command.create.not-enough-args");
        COMMAND_CREATE_ALREADY_EXISTS = new MessageKey(javaPlugin, "command.create.already-exists");
        COMMAND_CREATE_NAME_NOT_VALID = new MessageKey(javaPlugin, "command.create.name-not-valid");
        COMMAND_CREATE_ORE_GENERATOR_NOT_SPECIFIED = new MessageKey(javaPlugin, "command.create.ore-generator-not-specified");
        COMMAND_CREATE_BLOCK_SELECTOR_NOT_SPECIFIED = new MessageKey(javaPlugin, "command.create.block-selector-not-specified");
        COMMAND_CREATE_SUCCESS = new MessageKey(javaPlugin, "command.create.success");

        // reload command
        COMMAND_RELOAD_USAGE = new MessageKey(javaPlugin, "command.reload.usage");
        COMMAND_RELOAD_DESCRIPTION = new MessageKey(javaPlugin, "command.reload.description");
        COMMAND_RELOAD_BEGIN = new MessageKey(javaPlugin, "command.reload.begin");
        COMMAND_RELOAD_END = new MessageKey(javaPlugin, "command.reload.end");

        // set command
        COMMAND_SET_USAGE = new MessageKey(javaPlugin, "command.set.usage");
        COMMAND_SET_DESCRIPTION = new MessageKey(javaPlugin, "command.set.description");

        // set value command
        COMMAND_SET_VALUE_USAGE = new MessageKey(javaPlugin, "command.set.value.usage");
        COMMAND_SET_VALUE_DESCRIPTION = new MessageKey(javaPlugin, "command.set.value.description");
        COMMAND_SET_VALUE_NOT_ENOUGH_ARGS = new MessageKey(javaPlugin, "command.set.value.not-enough-args");
        COMMAND_SET_VALUE_SETTING_NOT_FOUND = new MessageKey(javaPlugin, "command.set.value.setting-not-found");
        COMMAND_SET_VALUE_SETTING_NOT_VALID = new MessageKey(javaPlugin, "command.set.value.setting-not-valid");
        COMMAND_SET_VALUE_VALUE_NOT_VALID = new MessageKey(javaPlugin, "command.set.value.value-not-valid");
        COMMAND_SET_VALUE_SUCCESS = new MessageKey(javaPlugin, "command.set.value.success");

        // set customdata command
        COMMAND_SET_CUSTOMDATA_USAGE = new MessageKey(javaPlugin, "command.set.customdata.usage");
        COMMAND_SET_CUSTOMDATA_DESCRIPTION = new MessageKey(javaPlugin, "command.set.customdata.description");
        COMMAND_SET_CUSTOMDATA_NOT_ENOUGH_ARGS = new MessageKey(javaPlugin, "command.set.customdata.not-enough-args");
        COMMAND_SET_CUSTOMDATA_NOT_FOUND = new MessageKey(javaPlugin, "command.set.customdata.not-found");
        COMMAND_SET_CUSTOMDATA_ORE_CONFIG_NOT_VALID = new MessageKey(javaPlugin, "command.set.customdata.ore-config-not-valid");
        COMMAND_SET_CUSTOMDATA_VALUE_NOT_VALID = new MessageKey(javaPlugin, "command.set.customdata.value-not-valid");
        COMMAND_SET_CUSTOMDATA_SUCCESS = new MessageKey(javaPlugin, "command.set.customdata.success");

        // set biome command
        COMMAND_SET_BIOME_USAGE = new MessageKey(javaPlugin, "command.set.biome.usage");
        COMMAND_SET_BIOME_DESCRIPTION = new MessageKey(javaPlugin, "command.set.biome.description");
        COMMAND_SET_BIOME_NOT_ENOUGH_ARGS = new MessageKey(javaPlugin, "command.set.biome.not-enough-args");
        COMMAND_SET_BIOME_SUCCESS = new MessageKey(javaPlugin, "command.set.biome.success");

        // set replacematerial command
        COMMAND_SET_REPLACEMATERIAL_USAGE = new MessageKey(javaPlugin, "command.set.replacematerial.usage");
        COMMAND_SET_REPLACEMATERIAL_DESCRIPTION = new MessageKey(javaPlugin, "command.set.replacematerial.description");
        COMMAND_SET_REPLACEMATERIAL_NOT_ENOUGH_ARGS = new MessageKey(javaPlugin, "command.set.replacematerial.not-enough-args");
        COMMAND_SET_REPLACEMATERIAL_SUCCESS = new MessageKey(javaPlugin, "command.set.replacematerial.success");

        // help command
        COMMAND_HELP_USAGE = new MessageKey(javaPlugin, "command.help.usage");
        COMMAND_SET_HELP_USAGE = new MessageKey(javaPlugin, "command.set.help.usage");
        COMMAND_HELP_DESCRIPTION = new MessageKey(javaPlugin, "command.help.description");
        COMMAND_HELP_HEADER_FORMAT = new MessageKey(javaPlugin, "command.help.header-format");
        COMMAND_HELP_FOOTER_FORMAT = new MessageKey(javaPlugin, "command.help.footer-format");
        COMMAND_HELP_SEPARATOR_FORMAT = new MessageKey(javaPlugin, "command.help.separator-format");
        COMMAND_HELP_PERMISSION_FORMAT = new MessageKey(javaPlugin, "command.help.permission-format");
        COMMAND_HELP_USAGE_FORMAT = new MessageKey(javaPlugin, "command.help.usage-format");
        COMMAND_HELP_DESCRIPTION_FORMAT = new MessageKey(javaPlugin, "command.help.description-format");
        COMMAND_HELP_SHORT_FORMAT = new MessageKey(javaPlugin, "command.help.short-format");
    }

}
