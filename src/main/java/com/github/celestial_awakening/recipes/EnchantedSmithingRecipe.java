package com.github.celestial_awakening.recipes;

import com.github.celestial_awakening.init.RecipeInit;
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
    public HashMap<Enchantment, Integer> getEnchantMap() {
        return enchantMap;
    }

    HashMap<Enchantment,Integer> enchantMap;
    final Ingredient template;

    public Ingredient getTemplate() {
        return template;
    }

    public Ingredient getBase() {
        return base;
    }

    public Ingredient getAddition() {
        return addition;
    }

    public ItemStack getResult() {
        return result;
    }

    final Ingredient base;
    final Ingredient addition;
    final ItemStack result;


    public EnchantedSmithingRecipe(ResourceLocation loc, Ingredient template, Ingredient base, Ingredient addition, ItemStack result, HashMap<Enchantment,Integer> enchants){
        super(loc, template, base,addition,  result);
        this.template=template;
        this.base=base;
        this.addition=addition;
        this.result=result;
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
        return RecipeInit.ENCHANT_SMITHING_SERIALIZER.get();
    }
}
