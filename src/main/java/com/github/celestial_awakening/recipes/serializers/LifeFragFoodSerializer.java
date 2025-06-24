package com.github.celestial_awakening.recipes.serializers;

import com.github.celestial_awakening.recipes.LifeFragFood;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

public class LifeFragFoodSerializer implements RecipeSerializer<LifeFragFood> {
    @Override
    public LifeFragFood fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
        ShapedRecipe shapedRecipe = RecipeSerializer.SHAPED_RECIPE.fromJson(resourceLocation,jsonObject); // Read as a normal ShapedRecipe
        return new LifeFragFood(resourceLocation,
                shapedRecipe.getGroup(),
                shapedRecipe.category(),
                shapedRecipe.getWidth(),
                shapedRecipe.getHeight(),
                shapedRecipe.getIngredients(),
                shapedRecipe.getResultItem(null));
    }

    @Override
    public @Nullable LifeFragFood fromNetwork(ResourceLocation resourceLocation,  FriendlyByteBuf buf) {
        ShapedRecipe shapedRecipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(resourceLocation, buf); // Read as normal
        return new LifeFragFood(
                resourceLocation,
                shapedRecipe.getGroup(),
                shapedRecipe.category(),
                shapedRecipe.getWidth(),
                shapedRecipe.getHeight(),
                shapedRecipe.getIngredients(),
                shapedRecipe.getResultItem(null)
        );
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, LifeFragFood recipe) {
        RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
    }
}
