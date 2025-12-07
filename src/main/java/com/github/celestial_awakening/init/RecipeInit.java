package com.github.celestial_awakening.init;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.recipes.CABrewingRecipe;
import com.github.celestial_awakening.recipes.serializers.BasicEnchantedSmithingSerializer;
import com.github.celestial_awakening.recipes.serializers.LifeFragFoodSerializer;
import com.github.celestial_awakening.recipes.serializers.LunarScaleRepairSerializer;
import com.github.celestial_awakening.recipes.serializers.SearedStoneToolsSmithingSerializer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeInit {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_DEFERRED_REGISTER = DeferredRegister.create( ForgeRegistries.RECIPE_SERIALIZERS, CelestialAwakening.MODID);
    //why does the deferred register cause issues?
    public static final RegistryObject<RecipeSerializer> LUNAR_SCALE_REPAIR_SERIALIZER = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("lunar_scale_repair", ()->new LunarScaleRepairSerializer());

    public static final RegistryObject<RecipeSerializer> LIFE_FRAG_FOOD_SERIALIZER=RECIPE_SERIALIZER_DEFERRED_REGISTER.register("life_frag_food", ()->new LifeFragFoodSerializer());

    public static final RegistryObject<RecipeSerializer> ENCHANT_SMITHING_SERIALIZER=RECIPE_SERIALIZER_DEFERRED_REGISTER.register("enchant_smithing",()->new BasicEnchantedSmithingSerializer());

    public static final RegistryObject<RecipeSerializer> SEARED_STONE_SERIALIZER=RECIPE_SERIALIZER_DEFERRED_REGISTER.register("seared_stone_smithing",()->new SearedStoneToolsSmithingSerializer());

    public static void registerBrewingRecipes(){
        createBrewingRecipeVariants(Potions.THICK,ItemInit.MOONSTONE.get(),PotionInit.LUNAR_GLEAM.get());
        createBrewingRecipeVariants(Potions.MUNDANE,ItemInit.SUNSTONE.get(),PotionInit.PHOTOGENIC.get());
        createBrewingRecipeVariants(PotionInit.PHOTOGENIC.get(),ItemInit.LIFE_FRAG.get(),PotionInit.PHOTOSYNTHESIS.get());
        createBrewingRecipeVariants(PotionInit.LUNAR_GLEAM.get(),ItemInit.LUNAR_SCALE.get(),PotionInit.LUNAR_RESTORATION.get());
    }


    public static void createBrewingRecipeVariants(Potion input, Item ingredient, Potion output){
        BrewingRecipeRegistry.addRecipe(new CABrewingRecipe(
                PotionUtils.setPotion(new ItemStack(Items.POTION),input),
                Ingredient.of(ingredient),
                PotionUtils.setPotion(new ItemStack(Items.POTION),output)
        ));


        BrewingRecipeRegistry.addRecipe(new CABrewingRecipe(
                PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION),input),
                Ingredient.of(ingredient),
                PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION),output)
        ));

        BrewingRecipeRegistry.addRecipe(new CABrewingRecipe(
                PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION),input),
                Ingredient.of(ingredient),
                PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION),output)
        ));
    }


}
