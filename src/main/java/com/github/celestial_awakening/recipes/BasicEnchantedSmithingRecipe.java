package com.github.celestial_awakening.recipes;
import com.github.celestial_awakening.init.RecipeInit;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class BasicEnchantedSmithingRecipe extends SmithingTransformRecipe implements EnchantedSmithing{
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


    public BasicEnchantedSmithingRecipe(ResourceLocation loc, Ingredient template, Ingredient base, Ingredient addition, ItemStack result, HashMap<Enchantment,Integer> enchants){
        super(loc, template, base,addition,  result);
        this.template=template;
        this.base=base;
        this.addition=addition;
        this.result=result;
        enchantMap=enchants;
    }
    @Override
    public boolean matches(Container container, Level level) {
        return super.matches(container,level);
     }
    @Override
    public ItemStack assemble(Container p_44001_, RegistryAccess p_267165_) {
        ItemStack stack=super.assemble(p_44001_,p_267165_);
        applyEnchants(stack,enchantMap);
        return stack;
    }


    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.ENCHANT_SMITHING_SERIALIZER.get();
    }
}
