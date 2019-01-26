package de.derfrzocker.custom.generator.ore;

import de.derfrzocker.custom.generator.ore.util.MessageKey;
import de.derfrzocker.custom.generator.ore.util.Messages;
import lombok.Getter;

public class CustomOreGeneratorMessages extends Messages {

    @Getter
    private static final CustomOreGeneratorMessages instance = new CustomOreGeneratorMessages();

    public final static MessageKey RELOAD_BEGIN = new MessageKey(getInstance(), "command.reload.begin");
    public final static MessageKey RELOAD_END = new MessageKey(getInstance(), "command.reload.end");
    public final static MessageKey SET_NOT_ENOUGH_ARGS = new MessageKey(getInstance(), "command.set.not_enough_args");
    public final static MessageKey SET_WORLD_NOT_FOUND = new MessageKey(getInstance(), "command.set.world_not_found");
    public final static MessageKey SET_NO_NUMBER = new MessageKey(getInstance(), "command.set.no_number");
    public final static MessageKey SET_NO_BLOCK_MATERIAL = new MessageKey(getInstance(), "command.set.no_block_material");
    public final static MessageKey SET_MATERIAL_NOT_FOUND = new MessageKey(getInstance(), "command.set.material_not_found");
    public final static MessageKey SET_SUCCESS = new MessageKey(getInstance(), "command.set.success");
    public final static MessageKey SET_SETTING_NOT_FOUND = new MessageKey(getInstance(), "command.set.setting_not_found");
    public final static MessageKey SET_SETTING_NOT_VALID = new MessageKey(getInstance(), "command.set.setting_not_valid");
    public final static MessageKey SET_BIOME_NOT_ENOUGH_ARGS = new MessageKey(getInstance(), "command.set.biome.not_enough_args");
    public final static MessageKey SET_BIOME_NOT_FOUND = new MessageKey(getInstance(), "command.set.biome.biome_not_found");
    public final static MessageKey HELP_HEADER = new MessageKey(getInstance(), "command.help.header");
    public final static MessageKey HELP_FOOTER = new MessageKey(getInstance(), "command.help.footer");
    public final static MessageKey HELP_SEPARATOR = new MessageKey(getInstance(), "command.help.separator");
    public final static MessageKey HELP_SET_COMMAND = new MessageKey(getInstance(), "command.help.set.command");
    public final static MessageKey HELP_SET_DESCRIPTION = new MessageKey(getInstance(), "command.help.set.description");
    public final static MessageKey HELP_SET_BIOME_COMMAND = new MessageKey(getInstance(), "command.help.set.biome.command");
    public final static MessageKey HELP_SET_BIOME_DESCRIPTION = new MessageKey(getInstance(), "command.help.set.biome.description");
    public final static MessageKey HELP_RELOAD_COMMAND = new MessageKey(getInstance(), "command.help.reload.command");
    public final static MessageKey HELP_RELOAD_DESCRIPTION = new MessageKey(getInstance(), "command.help.reload.description");
    public final static MessageKey HELP_COMMAND = new MessageKey(getInstance(), "command.help.help.command");
    public final static MessageKey HELP_DESCRIPTION = new MessageKey(getInstance(), "command.help.help.description");

}
