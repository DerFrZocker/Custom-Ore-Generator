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

package de.derfrzocker.custom.ore.generator.factory.listeners;

import de.derfrzocker.custom.ore.generator.api.CustomOreGeneratorService;
import de.derfrzocker.custom.ore.generator.factory.OreConfigBuilder;
import de.derfrzocker.spigot.utils.message.MessageKey;
import de.derfrzocker.spigot.utils.message.MessageValue;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class MainMaterialListener extends MaterialListener {


    public MainMaterialListener(@NotNull final JavaPlugin plugin, @NotNull final Supplier<CustomOreGeneratorService> serviceSupplier, @NotNull final Player player, @NotNull final OreConfigBuilder oreConfigBuilder, @NotNull final Conversation conversation) {
        super(plugin, serviceSupplier, player, oreConfigBuilder, conversation);
    }

    @Override
    public void onAirLeftClick() {
        getOreConfigBuilder().material(Material.AIR);
        new MessageKey(getPlugin(), "ore-config.factory.material.set").sendMessage(getPlayer(), new MessageValue("material", Material.AIR));
    }

    @Override
    public void onBlockLeftClick(@NotNull Block block) {
        getOreConfigBuilder().material(block.getType());
        getOreConfigBuilder().foundCustomDatas().clear();

        final BlockState blockState = block.getState();
        getServiceSupplier().get().getCustomData().stream().filter(customData -> customData.hasCustomData(blockState)).forEach(customData -> getOreConfigBuilder().setFoundCustomData(customData, customData.getCustomData(blockState)));

        new MessageKey(getPlugin(), "ore-config.factory.material.set").sendMessage(getPlayer(), new MessageValue("material", block.getType()));
    }
}
