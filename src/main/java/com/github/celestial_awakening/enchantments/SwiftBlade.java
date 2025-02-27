package com.github.celestial_awakening.enchantments;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.extensions.IForgeEnchantment;

public class SwiftBlade extends Enchantment implements IForgeEnchantment {
    static EnchantmentCategory ANY_MELEE_WEAPON = EnchantmentCategory.create("melee_weapon", item -> item instanceof SwordItem || item instanceof AxeItem || item instanceof TridentItem);
    public SwiftBlade() {

        super(Rarity.RARE, ANY_MELEE_WEAPON,EquipmentSlot.values());

    }
    public int getMaxLevel() {
        return 3;
    }
    @Override
    public boolean isTradeable() {
        return false;
    }
    @Override
    public boolean isDiscoverable() {
        return false;
    }
    public boolean isTreasureOnly() {
        return true;
    }

}
