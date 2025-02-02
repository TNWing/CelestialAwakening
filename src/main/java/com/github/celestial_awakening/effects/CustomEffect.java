package com.github.celestial_awakening.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.extensions.IForgeMobEffect;

import java.util.ArrayList;
import java.util.List;

public class CustomEffect extends MobEffect implements IForgeMobEffect {
    ArrayList<ItemStack> curativeItems= new ArrayList<ItemStack>();
    public CustomEffect(MobEffectCategory p_19451_, int color,boolean milk) {
        super(p_19451_, color);
        if (milk){
            curativeItems.add(new ItemStack(Items.MILK_BUCKET));
        }

    }
    @Override
    public List<ItemStack> getCurativeItems() {
        return curativeItems;
    }
}
