package com.github.celestial_awakening.recipes.serializers;

import com.github.celestial_awakening.recipes.EnchantedSmithingRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public class EnchantedSmithingSerializer implements RecipeSerializer<EnchantedSmithingRecipe> {
    public static final EnchantedSmithingSerializer INSTANCE=new EnchantedSmithingSerializer();
    HashMap<Enchantment,Integer> enchantMap=new HashMap<>();
    @Override
    public EnchantedSmithingRecipe fromJson(ResourceLocation loc, JsonObject json) {
        Ingredient ingredient = Ingredient.fromJson(GsonHelper.getNonNull(json, "template"));
        Ingredient ingredient1 = Ingredient.fromJson(GsonHelper.getNonNull(json, "base"));
        Ingredient ingredient2 = Ingredient.fromJson(GsonHelper.getNonNull(json, "addition"));
        ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        if (json.has("enchantments")){
            JsonArray arr=json.getAsJsonArray("enchantments");
            for (JsonElement ele:arr){
                JsonObject obj=ele.getAsJsonObject();
                Enchantment enchantment= BuiltInRegistries.ENCHANTMENT.get(new ResourceLocation(obj.get("enchantment").getAsString()));
                Integer lvl=obj.get("level").getAsInt();
                enchantMap.put(enchantment,lvl);
            }
        }
        return new EnchantedSmithingRecipe(loc,ingredient,ingredient1,ingredient2,itemstack,enchantMap);
    }

    @Override
    public @Nullable EnchantedSmithingRecipe fromNetwork(ResourceLocation loc, FriendlyByteBuf p_44106_) {
        SmithingTransformRecipe recipe=RecipeSerializer.SMITHING_TRANSFORM.fromNetwork(loc,p_44106_);
        int size=p_44106_.readVarInt();
        HashMap<Enchantment,Integer> map=new HashMap();
        for (int i=0;i<size;i++){
            //map.put();
        }
        return null;
        //return new EnchantedSmithingRecipe(loc,recipe.get);
    }

    @Override
    public void toNetwork(FriendlyByteBuf p_44101_, EnchantedSmithingRecipe p_44102_) {

    }
}
