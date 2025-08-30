package com.github.celestial_awakening.effects;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class LunarRenewal extends InstantenousMobEffect {
    public LunarRenewal(MobEffectCategory p_19440_, int p_19441_) {
        super(p_19440_, p_19441_);
    }


    @Override
    public void applyInstantenousEffect(@Nullable Entity p_19462_, @Nullable Entity owner, LivingEntity target, int amplifier, double dist) {
        Iterable<ItemStack> armorSlots=target.getArmorSlots();
        ItemStack mainHand=target.getMainHandItem();
        ItemStack offHand=target.getOffhandItem();
        int repairAmt= (int) Math.ceil(18+4.5f*Math.pow(amplifier,1.2));

        armorSlots.forEach(itemStack -> {
            if (itemStack.isDamageableItem()){
                int newDura=Math.min(itemStack.getMaxDamage(),
                        itemStack.getMaxDamage()-itemStack.getDamageValue() + repairAmt);
                itemStack.setDamageValue(itemStack.getMaxDamage()-newDura);
            }

        });
        if (mainHand.isDamageableItem()){
            int mainDura=Math.min(mainHand.getMaxDamage(),
                    mainHand.getMaxDamage()-mainHand.getDamageValue() + repairAmt);
            mainHand.setDamageValue(mainHand.getMaxDamage()-mainDura);
        }
        if (offHand.isDamageableItem()){
            int offDura=Math.min(offHand.getMaxDamage(),
                    offHand.getMaxDamage()-offHand.getDamageValue() + repairAmt);
            offHand.setDamageValue(offHand.getMaxDamage()-offDura);
        }

    }
}
