package com.github.celestial_awakening.recipes;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.HashMap;
import java.util.Map;

public interface EnchantedSmithing {

    default void applyEnchants(ItemStack stack, HashMap<Enchantment,Integer> enchantMap){
        for (Map.Entry<Enchantment,Integer> entry:enchantMap.entrySet()) {
            stack.enchant(entry.getKey(),entry.getValue());
        }
    }
}
