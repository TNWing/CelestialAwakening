package com.github.celestial_awakening.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.UUID;

//maybe make this an interface
//especially since some code, such as mobs replacing weapons, hardcode the item types
public interface MidnightIronTool{
    UUID OIL_ATTACK_DAMAGE_UUID = UUID.fromString("BC3F55D3-645C-4F38-A497-9C13A33DB5CF");
    UUID OIL_ATTACK_SPEED_UUID = UUID.fromString("AF233E1C-4180-4865-B01B-BCCE9785ACA3");
    int nameColor =0xa9a9a9;
    int descColor =0xE5E5E5;
    default boolean isValidTime(Level level){
        return !level.isDay();
    }
    default void addSharedAbilities( List<Component> components){
        Component midnightOilName=Component.translatable("tooltip.celestial_awakening.midnight_iron_gear.midnight_oil_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        components.add(midnightOilName);
        Component midnightOilEffect=Component.translatable("tooltip.celestial_awakening.midnight_iron_gear.midnight_oil_effect").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(descColor)));
        components.add(midnightOilEffect);
    }


}
