package com.github.celestial_awakening.util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class ToolTipBuilder {
    public static void addShiftInfo(ItemTooltipEvent event){
        Component component=Component.literal("Hold Shift for Details").setStyle(Style.EMPTY.withBold(true).withColor(ChatFormatting.WHITE));
        event.getToolTip().add(component);
    }
    public static void addFullSetName(ItemTooltipEvent event, String effectName, int nameColor){
        String desc="Full Set Effect: " + effectName;
        Component component=Component.literal(desc).setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        event.getToolTip().add(component);
    }

    public static void addPieceBonusName(ItemTooltipEvent event, String effectName, int nameColor){
        String desc="Piece Effect: " + effectName;
        Component component=Component.literal(desc).setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        event.getToolTip().add(component);
    }
    public static void addFullArmorSetComponent(ItemTooltipEvent event, String effectName, int nameColor, String effectDesc, int descColor){
        String desc="Full Set Effect: " + effectName;
        Component component=Component.literal(desc).setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        event.getToolTip().add(component);
        component=Component.literal(effectDesc).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(descColor)));
        event.getToolTip().add(component);

    }
    public static void addArmorPieceComponent(ItemTooltipEvent event, String effectName, int nameColor, String effectDesc, int descColor){

        String desc="Piece Effect: " + effectName;
        Component component=Component.literal(desc).setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        event.getToolTip().add(component);
        component=Component.literal(effectDesc).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(descColor)));
        event.getToolTip().add(component);

    }
    public static void addWeaponComponent(ItemTooltipEvent event, String effectName, int nameColor, String effectDesc, int descColor,int cnt){

        String desc=effectName;
        Component component=Component.literal(desc).setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        event.getToolTip().add(component);
        component=Component.literal(effectDesc).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(descColor)));
        event.getToolTip().add(component);

    }
}
