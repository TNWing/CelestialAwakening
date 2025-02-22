package com.github.celestial_awakening.util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class ToolTipBuilder {
    public static final String FULL_SET_EFFECT = "tooltip.celestial_awakening.armor.full_set_effect";
    public static final String PIECE_EFFECT = "tooltip.celestial_awakening.armor.piece_effect";
    public static final String SHIFT_TIP = "tooltip.celestial_awakening.armor.shift_tip";
    public static void addShiftInfo(ItemTooltipEvent event){
        /*
        i can probably put my text before mc's standard text (such as armor stats) by grabbing the tooltip list, and appending mine to the front of the thing.
         */
        Component component=Component.literal(SHIFT_TIP).setStyle(Style.EMPTY.withBold(true).withColor(ChatFormatting.WHITE));
        event.getToolTip().add(component);
    }
    public static void addFullSetName(ItemTooltipEvent event, String effectName, int nameColor){
        Component component=Component.translatable(FULL_SET_EFFECT).append(Component.translatable(effectName)).setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        event.getToolTip().add(component);
    }

    public static void addPieceBonusName(ItemTooltipEvent event, String effectName, int nameColor){
        Component component=Component.translatable(PIECE_EFFECT).append(Component.translatable(effectName)).setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        event.getToolTip().add(component);
    }
    public static void addFullArmorSetComponent(ItemTooltipEvent event, String effectName, int nameColor, String effectDesc, int descColor){
        Component component=Component.translatable(FULL_SET_EFFECT).append(Component.translatable(effectName)).setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        event.getToolTip().add(component);
        component=Component.translatable(effectDesc).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(descColor)));
        event.getToolTip().add(component);

    }
    public static void addArmorPieceComponent(ItemTooltipEvent event, String effectName, int nameColor, String effectDesc, int descColor){
        Component component=Component.translatable(PIECE_EFFECT).append(Component.translatable(effectName)).setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        event.getToolTip().add(component);
        component=Component.translatable(effectDesc).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(descColor)));
        event.getToolTip().add(component);
    }
    public static void addArmorPieceComponent(ItemTooltipEvent event, String effectName, int nameColor, String effectDesc, Object[] descFormat,int descColor){
        Component component=Component.translatable(PIECE_EFFECT).append(Component.translatable(effectName)).setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        event.getToolTip().add(component);
        component=Component.translatable(effectDesc,descFormat).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(descColor)));
        event.getToolTip().add(component);

    }
}
