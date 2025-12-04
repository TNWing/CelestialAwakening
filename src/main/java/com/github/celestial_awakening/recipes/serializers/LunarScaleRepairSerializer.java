package com.github.celestial_awakening.recipes.serializers;

import com.github.celestial_awakening.recipes.LunarScaleRepairRecipe;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class LunarScaleRepairSerializer implements RecipeSerializer<LunarScaleRepairRecipe> {
    @Override
    public LunarScaleRepairRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
        CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject, "category", (String)null), CraftingBookCategory.MISC);
        return new LunarScaleRepairRecipe(resourceLocation, craftingbookcategory);
    }

    public LunarScaleRepairRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
        CraftingBookCategory craftingbookcategory = buf.readEnum(CraftingBookCategory.class);
        return new LunarScaleRepairRecipe(resourceLocation, craftingbookcategory);
    }
    @Override
    public void toNetwork(FriendlyByteBuf buf, LunarScaleRepairRecipe lunarScaleRepairRecipe) {
        buf.writeEnum(lunarScaleRepairRecipe.category());
    }
}
