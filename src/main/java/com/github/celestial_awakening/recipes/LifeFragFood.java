package com.github.celestial_awakening.recipes;

import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class LifeFragFood extends ShapedRecipe {
    public LifeFragFood(ResourceLocation p_273203_, String p_272759_, CraftingBookCategory p_273506_, int p_272952_, int p_272920_, NonNullList<Ingredient> p_273650_, ItemStack p_272852_, boolean p_273122_) {
        super(p_273203_, p_272759_, p_273506_, p_272952_, p_272920_, p_273650_, p_272852_, p_273122_);
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width*height>=4;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}
