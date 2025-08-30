package com.github.celestial_awakening.recipes.serializers;

import com.github.celestial_awakening.recipes.LifeFragFoodRecipe;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.Nullable;

public class LifeFragFoodSerializer implements RecipeSerializer<LifeFragFoodRecipe> {
    @Override
    public LifeFragFoodRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
        ShapedRecipe shapedRecipe = RecipeSerializer.SHAPED_RECIPE.fromJson(resourceLocation,jsonObject); // Read as a normal ShapedRecipe
        return new LifeFragFoodRecipe(resourceLocation,
                shapedRecipe.getGroup(),
                shapedRecipe.category(),
                shapedRecipe.getWidth(),
                shapedRecipe.getHeight(),
                shapedRecipe.getIngredients(),
                shapedRecipe.getResultItem(null));
    }

    @Override
    public @Nullable LifeFragFoodRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
        ShapedRecipe shapedRecipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(resourceLocation, buf); // Read as normal
        return new LifeFragFoodRecipe(
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
    public void toNetwork(FriendlyByteBuf buffer, LifeFragFoodRecipe recipe) {
        RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
    }
}
