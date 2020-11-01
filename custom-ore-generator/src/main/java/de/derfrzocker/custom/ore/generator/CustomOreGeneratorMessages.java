/*
 * MIT License
 *
 * Copyright (c) 2019 - 2020 Marvin (DerFrZocker)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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

    // set value ore-generator command
    public final MessageKey COMMAND_SET_VALUE_ORE_GENERATOR_USAGE;
    public final MessageKey COMMAND_SET_VALUE_ORE_GENERATOR_DESCRIPTION;
    public final MessageKey COMMAND_SET_VALUE_ORE_GENERATOR_NOT_ENOUGH_ARGS;
    public final MessageKey COMMAND_SET_VALUE_ORE_GENERATOR_SETTING_NOT_FOUND;
    public final MessageKey COMMAND_SET_VALUE_ORE_GENERATOR_SETTING_NOT_VALID;
    public final MessageKey COMMAND_SET_VALUE_ORE_GENERATOR_VALUE_NOT_VALID;
    public final MessageKey COMMAND_SET_VALUE_ORE_GENERATOR_SUCCESS;

    // set value block-selector command
    public final MessageKey COMMAND_SET_VALUE_BLOCK_SELECTOR_USAGE;
    public final MessageKey COMMAND_SET_VALUE_BLOCK_SELECTOR_DESCRIPTION;
    public final MessageKey COMMAND_SET_VALUE_BLOCK_SELECTOR_NOT_ENOUGH_ARGS;
    public final MessageKey COMMAND_SET_VALUE_BLOCK_SELECTOR_SETTING_NOT_FOUND;
    public final MessageKey COMMAND_SET_VALUE_BLOCK_SELECTOR_SETTING_NOT_VALID;
    public final MessageKey COMMAND_SET_VALUE_BLOCK_SELECTOR_VALUE_NOT_VALID;
    public final MessageKey COMMAND_SET_VALUE_BLOCK_SELECTOR_SUCCESS;

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
    public final MessageKey COMMAND_SET_REPLACE_MATERIAL_USAGE;
    public final MessageKey COMMAND_SET_REPLACE_MATERIAL_DESCRIPTION;
    public final MessageKey COMMAND_SET_REPLACE_MATERIAL_NOT_ENOUGH_ARGS;
    public final MessageKey COMMAND_SET_REPLACE_MATERIAL_SUCCESS;

    // set selectmaterial command
    public final MessageKey COMMAND_SET_SELECT_MATERIAL_USAGE;
    public final MessageKey COMMAND_SET_SELECT_MATERIAL_DESCRIPTION;
    public final MessageKey COMMAND_SET_SELECT_MATERIAL_NOT_ENOUGH_ARGS;
    public final MessageKey COMMAND_SET_SELECT_MATERIAL_SUCCESS;

    // set position command
    public final MessageKey COMMAND_SET_POSITION_USAGE;
    public final MessageKey COMMAND_SET_POSITION_DESCRIPTION;
    public final MessageKey COMMAND_SET_POSITION_NOT_ENOUGH_ARGS;
    public final MessageKey COMMAND_SET_POSITION_VALUE_NOT_VALID;
    public final MessageKey COMMAND_SET_POSITION_SUCCESS;

    // add command
    public final MessageKey COMMAND_ADD_USAGE;
    public final MessageKey COMMAND_ADD_DESCRIPTION;

    // add ore-config command
    public final MessageKey COMMAND_ADD_ORE_CONFIG_USAGE;
    public final MessageKey COMMAND_ADD_ORE_CONFIG_DESCRIPTION;
    public final MessageKey COMMAND_ADD_ORE_CONFIG_NOT_ENOUGH_ARGS;
    public final MessageKey COMMAND_ADD_ORE_CONFIG_VALUE_NOT_VALID;
    public final MessageKey COMMAND_ADD_ORE_CONFIG_PRESENT;
    public final MessageKey COMMAND_ADD_ORE_CONFIG_SUCCESS;

    // info messages
    public final MessageKey COMMAND_INFO_USAGE;
    public final MessageKey COMMAND_INFO_DESCRIPTION;

    // help command
    public final MessageKey COMMAND_HELP_USAGE;
    public final MessageKey COMMAND_SET_HELP_USAGE;
    public final MessageKey COMMAND_SET_VALUE_HELP_USAGE;
    public final MessageKey COMMAND_ADD_HELP_USAGE;
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

        // set value
        COMMAND_SET_VALUE_USAGE = new MessageKey(javaPlugin, "command.set.value..usage");
        COMMAND_SET_VALUE_DESCRIPTION = new MessageKey(javaPlugin, "command.set.value.description");

        // set value ore-generator command
        COMMAND_SET_VALUE_ORE_GENERATOR_USAGE = new MessageKey(javaPlugin, "command.set.value.ore-generator.usage");
        COMMAND_SET_VALUE_ORE_GENERATOR_DESCRIPTION = new MessageKey(javaPlugin, "command.set.value.ore-generator.description");
        COMMAND_SET_VALUE_ORE_GENERATOR_NOT_ENOUGH_ARGS = new MessageKey(javaPlugin, "command.set.value.ore-generator.not-enough-args");
        COMMAND_SET_VALUE_ORE_GENERATOR_SETTING_NOT_FOUND = new MessageKey(javaPlugin, "command.set.value.ore-generator.setting-not-found");
        COMMAND_SET_VALUE_ORE_GENERATOR_SETTING_NOT_VALID = new MessageKey(javaPlugin, "command.set.value.ore-generator.setting-not-valid");
        COMMAND_SET_VALUE_ORE_GENERATOR_VALUE_NOT_VALID = new MessageKey(javaPlugin, "command.set.value.ore-generator.value-not-valid");
        COMMAND_SET_VALUE_ORE_GENERATOR_SUCCESS = new MessageKey(javaPlugin, "command.set.value.ore-generator.success");

        // set value block-selector command
        COMMAND_SET_VALUE_BLOCK_SELECTOR_USAGE = new MessageKey(javaPlugin, "command.set.value.block-selector.usage");
        COMMAND_SET_VALUE_BLOCK_SELECTOR_DESCRIPTION = new MessageKey(javaPlugin, "command.set.value.block-selector.description");
        COMMAND_SET_VALUE_BLOCK_SELECTOR_NOT_ENOUGH_ARGS = new MessageKey(javaPlugin, "command.set.value.block-selector.not-enough-args");
        COMMAND_SET_VALUE_BLOCK_SELECTOR_SETTING_NOT_FOUND = new MessageKey(javaPlugin, "command.set.value.block-selector.setting-not-found");
        COMMAND_SET_VALUE_BLOCK_SELECTOR_SETTING_NOT_VALID = new MessageKey(javaPlugin, "command.set.value.block-selector.setting-not-valid");
        COMMAND_SET_VALUE_BLOCK_SELECTOR_VALUE_NOT_VALID = new MessageKey(javaPlugin, "command.set.value.block-selector.value-not-valid");
        COMMAND_SET_VALUE_BLOCK_SELECTOR_SUCCESS = new MessageKey(javaPlugin, "command.set.value.block-selector.success");

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
        COMMAND_SET_REPLACE_MATERIAL_USAGE = new MessageKey(javaPlugin, "command.set.replace-material.usage");
        COMMAND_SET_REPLACE_MATERIAL_DESCRIPTION = new MessageKey(javaPlugin, "command.set.replace-material.description");
        COMMAND_SET_REPLACE_MATERIAL_NOT_ENOUGH_ARGS = new MessageKey(javaPlugin, "command.set.replace-material.not-enough-args");
        COMMAND_SET_REPLACE_MATERIAL_SUCCESS = new MessageKey(javaPlugin, "command.set.replace-material.success");

        // set selectmaterial command
        COMMAND_SET_SELECT_MATERIAL_USAGE = new MessageKey(javaPlugin, "command.set.select-material.usage");
        COMMAND_SET_SELECT_MATERIAL_DESCRIPTION = new MessageKey(javaPlugin, "command.set.select-material.description");
        COMMAND_SET_SELECT_MATERIAL_NOT_ENOUGH_ARGS = new MessageKey(javaPlugin, "command.set.select-material.not-enough-args");
        COMMAND_SET_SELECT_MATERIAL_SUCCESS = new MessageKey(javaPlugin, "command.set.select-material.success");

        // set position command
        COMMAND_SET_POSITION_USAGE = new MessageKey(javaPlugin, "command.set.position.usage");
        COMMAND_SET_POSITION_DESCRIPTION = new MessageKey(javaPlugin, "command.set.position.description");
        COMMAND_SET_POSITION_NOT_ENOUGH_ARGS = new MessageKey(javaPlugin, "command.set.position.not-enough-args");
        COMMAND_SET_POSITION_VALUE_NOT_VALID = new MessageKey(javaPlugin, "command.set.position.value-not-valid");
        COMMAND_SET_POSITION_SUCCESS = new MessageKey(javaPlugin, "command.set.position.success");

        // add command
        COMMAND_ADD_USAGE = new MessageKey(javaPlugin, "command.add.usage");
        COMMAND_ADD_DESCRIPTION = new MessageKey(javaPlugin, "command.add.description");

        // add ore-config command
        COMMAND_ADD_ORE_CONFIG_USAGE = new MessageKey(javaPlugin, "command.add.ore-config.usage");
        COMMAND_ADD_ORE_CONFIG_DESCRIPTION = new MessageKey(javaPlugin, "command.add.ore-config.description");
        COMMAND_ADD_ORE_CONFIG_NOT_ENOUGH_ARGS = new MessageKey(javaPlugin, "command.add.ore-config.not-enough-args");
        COMMAND_ADD_ORE_CONFIG_VALUE_NOT_VALID = new MessageKey(javaPlugin, "command.add.ore-config.value-not-valid");
        COMMAND_ADD_ORE_CONFIG_PRESENT = new MessageKey(javaPlugin, "command.add.ore-config.present");
        COMMAND_ADD_ORE_CONFIG_SUCCESS = new MessageKey(javaPlugin, "command.add.ore-config.success");

        // info messages
        COMMAND_INFO_USAGE = new MessageKey(javaPlugin, "command.info.usage");
        COMMAND_INFO_DESCRIPTION = new MessageKey(javaPlugin, "command.info.description");

        // help command
        COMMAND_HELP_USAGE = new MessageKey(javaPlugin, "command.help.usage");
        COMMAND_SET_HELP_USAGE = new MessageKey(javaPlugin, "command.set.help.usage");
        COMMAND_SET_VALUE_HELP_USAGE = new MessageKey(javaPlugin, "command.set.value.help.usage");
        COMMAND_ADD_HELP_USAGE = new MessageKey(javaPlugin, "command.add.help.usage");
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
