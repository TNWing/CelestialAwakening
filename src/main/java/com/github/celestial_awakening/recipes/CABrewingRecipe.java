package com.github.celestial_awakening.recipes;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import org.jetbrains.annotations.NotNull;

public class CABrewingRecipe implements IBrewingRecipe {
    @NotNull
    private final ItemStack input;
    @NotNull private final Ingredient ingredient;
    @NotNull private final ItemStack output;

    public CABrewingRecipe(@NotNull ItemStack input, @NotNull Ingredient ingredient, @NotNull ItemStack output) {
        this.input = input;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public boolean isInput(ItemStack inputStack) {
        return inputStack.getItem() == input.getItem()
                && PotionUtils.getPotion(inputStack) == PotionUtils.getPotion(input);
    }

    @Override
    public boolean isIngredient(ItemStack ingredientItemStack) {
        return ingredient.test(ingredientItemStack);
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        return isInput(input) && isIngredient(ingredient) ? output.copy() : ItemStack.EMPTY;
    }
}
