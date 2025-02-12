package com.github.celestial_awakening.recipes.serializers;

import com.github.celestial_awakening.recipes.LunarScaleRepair;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class LunarScaleRepairSerializer implements RecipeSerializer<LunarScaleRepair> {
    public static final LunarScaleRepairSerializer INSTANCE=new LunarScaleRepairSerializer();
    @Override
    public LunarScaleRepair fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
        CraftingBookCategory craftingbookcategory = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(jsonObject, "category", (String)null), CraftingBookCategory.MISC);
        return new LunarScaleRepair(resourceLocation, craftingbookcategory);
    }

    public LunarScaleRepair fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf buf) {
        CraftingBookCategory craftingbookcategory = buf.readEnum(CraftingBookCategory.class);
        return new LunarScaleRepair(resourceLocation, craftingbookcategory);
    }
    @Override
    public void toNetwork(FriendlyByteBuf buf, LunarScaleRepair lunarScaleRepair) {
        buf.writeEnum(lunarScaleRepair.category());
    }
}
