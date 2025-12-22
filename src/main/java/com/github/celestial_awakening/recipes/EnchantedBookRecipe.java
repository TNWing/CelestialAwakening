package com.github.celestial_awakening.recipes;

import com.github.celestial_awakening.init.EnchantmentInit;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class EnchantedBookRecipe extends ShapedRecipe {
    public int getEnchantLvl() {
        return enchantLvl;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    int enchantLvl=1;
    Enchantment enchantment;
    public EnchantedBookRecipe(ResourceLocation p_273203_, String p_272759_,
                               CraftingBookCategory p_273506_, int p_272952_, int p_272920_,
                               NonNullList<Ingredient> p_273650_, ItemStack p_272852_, boolean p_273122_, Enchantment enchant,int lvl) {
        super(p_273203_, p_272759_, p_273506_, p_272952_, p_272920_, p_273650_, p_272852_, p_273122_);
        enchantment=enchant;
        enchantLvl=lvl;
    }
    public EnchantedBookRecipe(ResourceLocation p_273203_, String p_272759_,
                               CraftingBookCategory p_273506_, int p_272952_, int p_272920_,
                               NonNullList<Ingredient> p_273650_, ItemStack p_272852_, boolean p_273122_, Enchantment enchant) {
        super(p_273203_, p_272759_, p_273506_, p_272952_, p_272920_, p_273650_, p_272852_, p_273122_);
        enchantment=enchant;
        enchantLvl=1;
    }
    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level){
        return super.matches(craftingContainer,level);
    }
    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        ItemStack itemStack=super.assemble(craftingContainer, registryAccess);
        itemStack.enchant(getEnchantment(), getEnchantLvl());
        return itemStack;
    }
}
