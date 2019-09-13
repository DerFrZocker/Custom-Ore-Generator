package de.derfrzocker.custom.ore.generator;

import de.derfrzocker.spigot.utils.message.MessageKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CustomOreGeneratorMessages {

    // general command
    public final static MessageKey COMMAND_WORLD_NOT_FOUND = new MessageKey(CustomOreGenerator.getInstance(), "command.world-not-found");
    public final static MessageKey COMMAND_ORE_GENERATOR_NOT_FOUND = new MessageKey(CustomOreGenerator.getInstance(), "command.ore-generator-not-found");
    public final static MessageKey COMMAND_MATERIAL_NOT_FOUND = new MessageKey(CustomOreGenerator.getInstance(), "command.material-not-found");
    public final static MessageKey COMMAND_MATERIAL_NO_BLOCK = new MessageKey(CustomOreGenerator.getInstance(), "command.material-no-block");
    public final static MessageKey COMMAND_ORE_CONFIG_NOT_FOUND = new MessageKey(CustomOreGenerator.getInstance(), "command.ore-config-not-found");
    public final static MessageKey COMMAND_BIOME_NOT_FOUND = new MessageKey(CustomOreGenerator.getInstance(), "command.biome-not-found");

    // create command
    public final static MessageKey COMMAND_CREATE_NOT_ENOUGH_ARGS = new MessageKey(CustomOreGenerator.getInstance(), "command.create.not-enough-args");
    public final static MessageKey COMMAND_CREATE_ALREADY_EXISTS = new MessageKey(CustomOreGenerator.getInstance(), "command.create.already-exists");
    public final static MessageKey COMMAND_CREATE_ORE_GENERATOR_NOT_SPECIFIED = new MessageKey(CustomOreGenerator.getInstance(), "command.create.ore-generator-not-specified");
    public final static MessageKey COMMAND_CREATE_SUCCESS = new MessageKey(CustomOreGenerator.getInstance(), "command.create.success");

    // reload command
    public final static MessageKey COMMAND_RELOAD_BEGIN = new MessageKey(CustomOreGenerator.getInstance(), "command.reload.begin");
    public final static MessageKey COMMAND_RELOAD_END = new MessageKey(CustomOreGenerator.getInstance(), "command.reload.end");

    // set value command
    public final static MessageKey COMMAND_SET_VALUE_NOT_ENOUGH_ARGS = new MessageKey(CustomOreGenerator.getInstance(), "command.set.value.not-enough-args");
    public final static MessageKey COMMAND_SET_VALUE_SETTING_NOT_FOUND = new MessageKey(CustomOreGenerator.getInstance(), "command.set.value.setting-not-found");
    public final static MessageKey COMMAND_SET_VALUE_SETTING_NOT_VALID = new MessageKey(CustomOreGenerator.getInstance(), "command.set.value.setting-not-valid");
    public final static MessageKey COMMAND_SET_VALUE_VALUE_NOT_VALID = new MessageKey(CustomOreGenerator.getInstance(), "command.set.value.value-not-valid");
    public final static MessageKey COMMAND_SET_VALUE_SUCCESS = new MessageKey(CustomOreGenerator.getInstance(), "command.set.value.success");

    // set customdata command
    public final static MessageKey COMMAND_SET_CUSTOMDATA_NOT_ENOUGH_ARGS = new MessageKey(CustomOreGenerator.getInstance(), "command.set.customdata.not-enough-args");
    public final static MessageKey COMMAND_SET_CUSTOMDATA_NOT_FOUND = new MessageKey(CustomOreGenerator.getInstance(), "command.set.customdata.not-found");
    public final static MessageKey COMMAND_SET_CUSTOMDATA_ORE_CONFIG_NOT_VALID = new MessageKey(CustomOreGenerator.getInstance(), "command.set.customdata.ore-config-not-valid");
    public final static MessageKey COMMAND_SET_CUSTOMDATA_VALUE_NOT_VALID = new MessageKey(CustomOreGenerator.getInstance(), "command.set.customdata.value-not-valid");
    public final static MessageKey COMMAND_SET_CUSTOMDATA_SUCCESS = new MessageKey(CustomOreGenerator.getInstance(), "command.set.customdata.success");

    // set biome command
    public final static MessageKey COMMAND_SET_BIOME_NOT_ENOUGH_ARGS = new MessageKey(CustomOreGenerator.getInstance(), "command.set.biome.not-enough-args");
    public final static MessageKey COMMAND_SET_BIOME_SUCCESS = new MessageKey(CustomOreGenerator.getInstance(), "command.set.biome.success");

    // help command
    public final static MessageKey HELP_HEADER = new MessageKey(CustomOreGenerator.getInstance(), "command.help.header");
    public final static MessageKey HELP_FOOTER = new MessageKey(CustomOreGenerator.getInstance(), "command.help.footer");
    public final static MessageKey HELP_SEPARATOR = new MessageKey(CustomOreGenerator.getInstance(), "command.help.separator");
    public final static MessageKey HELP_SET_COMMAND = new MessageKey(CustomOreGenerator.getInstance(), "command.help.set.command");
    public final static MessageKey HELP_SET_DESCRIPTION = new MessageKey(CustomOreGenerator.getInstance(), "command.help.set.description");
    public final static MessageKey HELP_SET_BIOME_COMMAND = new MessageKey(CustomOreGenerator.getInstance(), "command.help.set.biome.command");
    public final static MessageKey HELP_SET_BIOME_DESCRIPTION = new MessageKey(CustomOreGenerator.getInstance(), "command.help.set.biome.description");
    public final static MessageKey HELP_RELOAD_COMMAND = new MessageKey(CustomOreGenerator.getInstance(), "command.help.reload.command");
    public final static MessageKey HELP_RELOAD_DESCRIPTION = new MessageKey(CustomOreGenerator.getInstance(), "command.help.reload.description");
    public final static MessageKey HELP_COMMAND = new MessageKey(CustomOreGenerator.getInstance(), "command.help.help.command");
    public final static MessageKey HELP_DESCRIPTION = new MessageKey(CustomOreGenerator.getInstance(), "command.help.help.description");

}
