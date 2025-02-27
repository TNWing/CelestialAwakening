package com.github.celestial_awakening.items;

import com.github.celestial_awakening.entity.projectile.ArrowType;
import com.github.celestial_awakening.entity.projectile.CA_ArrowProjectile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CustomArrowItem extends ArrowItem {

    private ArrowType type;
    public CustomArrowItem(Properties p_40512_, ArrowType at) {
        super(p_40512_);
        this.type=at;
    }
    @Override
    public AbstractArrow createArrow(Level level, ItemStack p_40514_, LivingEntity owner) {
        CA_ArrowProjectile arrow = CA_ArrowProjectile.create(level, owner,this.type);
        return arrow;
    }
    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, net.minecraft.world.entity.player.Player player) {
        return false;
    }
    public ArrowType getType(){
        return this.type;
    }
    public void setType(ArrowType t){
        this.type=t;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        Component effect=Component.empty();
        switch(type){
            case LUNAR -> {
                effect=Component.translatable("tooltip.celestial_awakening.lunar_arrow.desc").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xd8d7d5)));
                break;
            }
            case SOLAR -> {
                effect=Component.translatable("tooltip.celestial_awakening.solar_arrow.desc").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xe7e82c)));
                break;
            }
            case SINGULARITY -> {
                break;
            }
        }
        components.add(effect);
        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }
}
