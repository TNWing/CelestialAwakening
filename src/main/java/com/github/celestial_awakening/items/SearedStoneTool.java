package com.github.celestial_awakening.items;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public interface SearedStoneTool {
    //
    int nameColor=0x756252;
    int descColor=0x3D3229;
    default void addSharedAbilities( List<Component> components){

        Component sturdy=Component.translatable("tooltip.celestial_awakening.seared_stone_tools.sturdy_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        components.add(sturdy);
        Component sturdyEffect=Component.translatable("tooltip.celestial_awakening.seared_stone_tools.sturdy_desc").setStyle(Style.EMPTY.withBold(false).withColor(TextColor.fromRgb(descColor)));
        components.add(sturdyEffect);
    }

    int getUpgradeTier();

    void setUpgradeTier();

}
