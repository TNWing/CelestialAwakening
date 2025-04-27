package com.github.celestial_awakening.items;

import net.minecraft.world.item.BookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class LunaTomeItem extends BookItem {
    public LunaTomeItem(Properties p_40643_) {
        super(p_40643_);
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

}
