package com.github.celestial_awakening.items;

import com.github.celestial_awakening.Config;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class MidnightIronPickaxe extends PickaxeItem implements MidnightIronTool {
    boolean isNight=false;
    float spd;
    public MidnightIronPickaxe(Tier p_42961_, int p_42962_, float p_42963_, Properties p_42964_) {
        super(p_42961_, p_42962_, p_42963_, p_42964_);
        spd=p_42963_;
    }
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot p_40990_) {
        Multimap<Attribute,AttributeModifier> defaultModifiers=super.getDefaultAttributeModifiers(p_40990_);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> newBuilder= ImmutableMultimap.builder();
        Multimap<Attribute,AttributeModifier> modifiersToReturn;
        if (isNight){
            newBuilder.put(Attributes.ATTACK_DAMAGE,new AttributeModifier(BASE_ATTACK_DAMAGE_UUID,"Weapon modifier", getAttackDamage(), AttributeModifier.Operation.ADDITION));
            newBuilder.put(Attributes.ATTACK_SPEED,new AttributeModifier(BASE_ATTACK_SPEED_UUID,"Weapon modifier", spd* Config.midnightIronAtkSpdMult, AttributeModifier.Operation.ADDITION));
            modifiersToReturn=newBuilder.build();
        }
        else{
            modifiersToReturn=defaultModifiers;
        }
        return p_40990_ == EquipmentSlot.MAINHAND ? modifiersToReturn :  super.getDefaultAttributeModifiers(p_40990_);
    }
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        addSharedAbilities(components);
        super.appendHoverText(itemStack, level, components, tooltipFlag);
    }
    @Override
    public float getAttackDamage() {
        float dmg=super.getAttackDamage();
        return isNight ? (float) (dmg * Config.midnightIronDmgMult) : dmg;
    }
    @Override
    public boolean hurtEnemy(ItemStack p_43278_, LivingEntity p_43279_, LivingEntity p_43280_) {
        int breakAmt=2;
        if (isNight){
            breakAmt=0;
        }
        p_43278_.hurtAndBreak(breakAmt, p_43280_, (p_43296_) -> {
            p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
        });
        return true;
    }

    @Override
    public boolean mineBlock(ItemStack p_43282_, Level p_43283_, BlockState p_43284_, BlockPos p_43285_, LivingEntity p_43286_) {
        if (p_43284_.getDestroySpeed(p_43283_, p_43285_) != 0.0F) {
            int breakAmt=1;
            if (isNight) {
                breakAmt = 0;
            }
            p_43282_.hurtAndBreak(breakAmt, p_43286_, (p_43276_) -> {
                p_43276_.broadcastBreakEvent(EquipmentSlot.MAINHAND);
            });
        }
        return true;
    }
    @Override
    public float getDestroySpeed(ItemStack itemStack, BlockState blockState) {
        float spd=super.getDestroySpeed(itemStack, blockState);
        if (isNight){
            spd*=Config.midnightIronMiningSpdMult;
        }
        return spd;
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        if (!p_41405_.isClientSide){
            isNight =isValidTime(p_41405_);
        }

    }

}
