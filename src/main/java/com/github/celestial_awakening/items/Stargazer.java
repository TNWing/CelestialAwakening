package com.github.celestial_awakening.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class Stargazer extends FluorescentBow{

    public Stargazer(Properties p_40660_) {
        super(p_40660_);
        dmgMult=0.9f;
    }
    @Override
    public int getEnchantmentValue() {
        return 4;
    }
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        Component dangerSenseName=Component.translatable("tooltip.celestial_awakening.fluorescent_bow.dangersense_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        components.add(dangerSenseName);
        components.add(Component.translatable("tooltip.celestial_awakening.fluorescent_bow.dangersense_desc"));
        Component revealShotName=Component.translatable("tooltip.celestial_awakening.fluorescent_bow.revealing_shot_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        components.add(revealShotName);
        components.add(Component.translatable("tooltip.celestial_awakening.fluorescent_bow.revealing_shot_desc"));
        Component burningLightName=Component.translatable("tooltip.celestial_awakening.star_gazer.burning_light_name").setStyle(Style.EMPTY.withBold(true).withColor(TextColor.fromRgb(nameColor)));
        components.add(burningLightName);
        components.add(Component.translatable("tooltip.celestial_awakening.star_gazer.burning_light_desc"));
        superAppendHoverText(itemStack, level, components, tooltipFlag);
    }
}
