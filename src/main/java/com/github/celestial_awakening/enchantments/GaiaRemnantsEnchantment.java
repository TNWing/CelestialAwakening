package com.github.celestial_awakening.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.extensions.IForgeEnchantment;

public class GaiaRemnantsEnchantment extends Enchantment implements IForgeEnchantment {
    public GaiaRemnantsEnchantment(Rarity p_44676_, EquipmentSlot... p_44678_) {
        super(p_44676_, EnchantmentCategory.BREAKABLE, p_44678_);
    }
    @Override
    public boolean canEnchant(ItemStack p_44642_) {
        return p_44642_.getItem() instanceof ShieldItem;
    }
    public int getMaxLevel() {
        return 3;
    }
    public boolean checkCompatibility(Enchantment p_44590_) {
        return p_44590_ instanceof GaiaLinkEnchantment ? false : super.checkCompatibility(p_44590_);
    }
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return stack.getItem() instanceof ShieldItem;
    }
}
