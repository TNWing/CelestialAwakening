package com.github.celestial_awakening.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class LunarRestoration extends MobEffect {
    protected LunarRestoration(MobEffectCategory p_19451_, int p_19452_) {
        super(p_19451_, p_19452_);
    }

    @Override
    public boolean isDurationEffectTick(int ticks, int amplifier) {
        int k=40;
        if (k>0){
            return ticks%k==1;
        }
        else{
            return true;
        }
    }

    @Override
    public void applyEffectTick(LivingEntity target, int amplifier) {
        Iterable<ItemStack> armorSlots=target.getArmorSlots();
        ItemStack mainHand=target.getMainHandItem();
        ItemStack offHand=target.getOffhandItem();
        int repairAmt=2+amplifier;

        armorSlots.forEach(itemStack -> {
            int newDura=Math.min(itemStack.getMaxDamage(),
                    itemStack.getMaxDamage()-itemStack.getDamageValue() + repairAmt);
            itemStack.setDamageValue(itemStack.getMaxDamage()-newDura);
        });
        int mainDura=Math.min(mainHand.getMaxDamage(),
                mainHand.getMaxDamage()-mainHand.getDamageValue() + repairAmt);
        mainHand.setDamageValue(mainHand.getMaxDamage()-mainDura);
        int offDura=Math.min(offHand.getMaxDamage(),
                offHand.getMaxDamage()-offHand.getDamageValue() + repairAmt);
        offHand.setDamageValue(offHand.getMaxDamage()-offDura);
    }
}
