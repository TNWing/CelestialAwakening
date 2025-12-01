package com.github.celestial_awakening.recipes;

import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class EnchantedSmithingRecipe extends SmithingTransformRecipe {
    HashMap<Enchantment,Integer> enchantMap;
    public EnchantedSmithingRecipe(ResourceLocation loc, Ingredient template, Ingredient base, Ingredient addition, ItemStack result, HashMap<Enchantment,Integer> enchants){
        super(loc, template, base,addition,  result);
        enchantMap=enchants;
    }
    /*
    public EnchantedSmithingRecipe(SmithingRecipe recipe, HashMap<Enchantment,Integer> enchants){
        //super(recipe.getIngredients());
        enchantMap=enchants;
    }

     */


    @Override
    public ItemStack assemble(Container p_44001_, RegistryAccess p_267165_) {
        ItemStack stack=super.assemble(p_44001_,p_267165_);
        for (Map.Entry<Enchantment,Integer> entry:enchantMap.entrySet()) {
            stack.enchant(entry.getKey(),entry.getValue());
        }
        return stack;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}
