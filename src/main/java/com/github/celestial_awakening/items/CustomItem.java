package com.github.celestial_awakening.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;

import javax.annotation.Nullable;
import java.util.List;

public class CustomItem extends Item implements IForgeItem {
    String[] toolTip;
    int[] rgb;
    public CustomItem(Properties p_41383_) {
        super(p_41383_);
        toolTip=null;
        rgb=new int[1];
        rgb[0]= 0xFFFFFF;
    }
    public CustomItem(Properties p_41383_,String text) {
        super(p_41383_);
        toolTip= new String[]{text};
        rgb=new int[1];
        rgb[0]= 0xFFFFFF;
    }
    public CustomItem(Properties p_41383_,String text,int c) {
        super(p_41383_);
        toolTip= new String[]{text};
        rgb=new int[1];
        rgb[0]= c;
    }
    public CustomItem(Properties p_41383_,String[] text,int[] c) {
        super(p_41383_);
        toolTip= text;
        rgb=c;
    }
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        if (toolTip!=null){
            for (String str:toolTip){
                Component tip=Component.translatable(str).setStyle(Style.EMPTY.withBold(false).withColor(TextColor.fromRgb(rgb[0])));
                components.add(tip);
            }

        }

        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }
}
