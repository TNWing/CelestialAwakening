package com.github.celestial_awakening.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.level.Level;

import java.util.List;

//maybe make this an interface
//especially since some code, such as mobs replacing weapons, hardcode the item types
public interface MidnightIronTool{
    int nameColor =0xa9a9a9;
    int descColor =0xE5E5E5;
    default boolean isValidTime(Level level){
        return !level.isDay();
    }
    default void addSharedAbilities( List<Component> components){
        Component midnightOilName=Component.translatable("tooltip.celestial_awakening.midnight_iron_gear.midnight_oil_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        components.add(midnightOilName);
        Component midnightOilEffect=Component.translatable("tooltip.celestial_awakening.midnight_iron_gear.midnight_oil_effect").setStyle(Style.EMPTY.withBold(false).withColor(TextColor.fromRgb(descColor)));
        components.add(midnightOilEffect);
    }


}
