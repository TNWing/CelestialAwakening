package com.github.celestial_awakening.recipes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.level.Level;

public class BadgeSmithingUpgrades implements SmithingRecipe {
    private final ResourceLocation id;
    final Ingredient mat;
    final Ingredient base;
    final Ingredient badge;
    public BadgeSmithingUpgrades(ResourceLocation resourceLocation, Ingredient ingredient1, Ingredient ingredient2, Ingredient ingredient3) {
        this.id = resourceLocation;
        this.mat = ingredient1;
        this.base = ingredient2;
        this.badge = ingredient3;
    }

    @Override
    public boolean isTemplateIngredient(ItemStack p_266982_) {
        return false;
    }

    @Override
    public boolean isBaseIngredient(ItemStack p_266962_) {
        return false;
    }

    @Override
    public boolean isAdditionIngredient(ItemStack p_267132_) {
        return false;
    }

    @Override
    public boolean matches(Container p_44002_, Level p_44003_) {
        return false;
    }

    @Override
    public ItemStack assemble(Container p_44001_, RegistryAccess p_267165_) {
        return null;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return null;
    }

    @Override
    public ResourceLocation getId() {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}
