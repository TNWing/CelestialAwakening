package com.github.celestial_awakening.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class LunaTomeItem extends BookItem {
    String toolTip;
    int rgb;
    public LunaTomeItem(Properties p_40643_,String t,int c) {
        super(p_40643_);
        this.toolTip=t;
        this.rgb=c;
    }

    @Override
    public int getEnchantmentValue() {
        return 12;
    }
    @Override
    public boolean isEnchantable(ItemStack p_40646_) {
        return p_40646_.getCount() == 1;
    }
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment)
    {
        return enchantment.isAllowedOnBooks();

    }
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if (toolTip!=null){
            Component tip=Component.translatable(toolTip).setStyle(Style.EMPTY.withBold(false).withColor(TextColor.fromRgb(rgb)));
            components.add(tip);
        }

        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }

}
