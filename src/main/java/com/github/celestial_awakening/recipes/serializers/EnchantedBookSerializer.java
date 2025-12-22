package com.github.celestial_awakening.recipes.serializers;

import com.github.celestial_awakening.recipes.EnchantedBookRecipe;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

public class EnchantedBookSerializer implements RecipeSerializer<EnchantedBookRecipe> {
    @Override
    public EnchantedBookRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
        ShapedRecipe shapedRecipe = RecipeSerializer.SHAPED_RECIPE.fromJson(resourceLocation,jsonObject); // Read as a normal ShapedRecipe
        int lvl=1;
        Enchantment enchantment=null;
        if (jsonObject.has("result")){
            JsonObject resultObj= GsonHelper.getAsJsonObject(jsonObject, "result");
            if (resultObj.has("level")){
                lvl=GsonHelper  .getAsInt(resultObj,"level");
            }
            if (resultObj.has("enchantment")){
                enchantment= BuiltInRegistries.ENCHANTMENT.get(new ResourceLocation(GsonHelper.getAsString(resultObj,"enchantment")));
            }
        }
        return new EnchantedBookRecipe(resourceLocation,shapedRecipe.getGroup(),
                shapedRecipe.category(),
                shapedRecipe.getWidth(),
                shapedRecipe.getHeight(),
                shapedRecipe.getIngredients(),
                shapedRecipe.getResultItem(null),true,enchantment, lvl);
    }

    @Override
    public @Nullable EnchantedBookRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
        ShapedRecipe shapedRecipe = RecipeSerializer.SHAPED_RECIPE.fromNetwork(resourceLocation, buf); // Read as normal
        return new EnchantedBookRecipe(resourceLocation,shapedRecipe.getGroup(),
                shapedRecipe.category(),
                shapedRecipe.getWidth(),
                shapedRecipe.getHeight(),
                shapedRecipe.getIngredients(),
                shapedRecipe.getResultItem(null),true,BuiltInRegistries.ENCHANTMENT.get(buf.readResourceLocation()),buf.readVarInt());
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, EnchantedBookRecipe recipe) {
        RecipeSerializer.SHAPED_RECIPE.toNetwork(buffer, recipe);
        buffer.writeResourceLocation(BuiltInRegistries.ENCHANTMENT.getKey(recipe.getEnchantment()));
        buffer.writeVarInt(recipe.getEnchantLvl());
    }
}
