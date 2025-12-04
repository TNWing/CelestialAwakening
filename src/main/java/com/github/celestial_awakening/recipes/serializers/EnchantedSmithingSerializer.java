package com.github.celestial_awakening.recipes.serializers;

import com.github.celestial_awakening.recipes.EnchantedSmithingRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
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

import javax.json.JsonException;
import java.util.HashMap;
import java.util.Map;

public class EnchantedSmithingSerializer implements RecipeSerializer<EnchantedSmithingRecipe> {

    @Override
    public EnchantedSmithingRecipe fromJson(ResourceLocation loc, JsonObject json) {
        HashMap<Enchantment,Integer> enchantMap=new HashMap<>();
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
                if (enchantment==null){
                    throw new JsonException("Invalid enchantment");
                }
                enchantMap.put(enchantment,lvl);
            }
        }
        return new EnchantedSmithingRecipe(loc,ingredient,ingredient1,ingredient2,itemstack,enchantMap);
    }

    @Override
    public @Nullable EnchantedSmithingRecipe fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
        HashMap<Enchantment,Integer> enchantMap=new HashMap<>();
        Ingredient ingredient = Ingredient.fromNetwork(buf);
        Ingredient ingredient1 = Ingredient.fromNetwork(buf);
        Ingredient ingredient2 = Ingredient.fromNetwork(buf);

        int size=buf.readVarInt();
        HashMap<Enchantment,Integer> map=new HashMap();
        for (int i=0;i<size;i++){
            ResourceLocation eLoc= buf.readResourceLocation();
            enchantMap.put(BuiltInRegistries.ENCHANTMENT.get(eLoc), buf.readVarInt());
            //map.put();
        }
        ItemStack itemstack = buf.readItem();
        return new EnchantedSmithingRecipe(loc,ingredient,ingredient1,ingredient2,itemstack,map);
        //return new EnchantedSmithingRecipe(loc,recipe.get);

    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, EnchantedSmithingRecipe recipe) {
        recipe.getTemplate().toNetwork(buf);
        recipe.getBase().toNetwork(buf);
        recipe.getAddition().toNetwork(buf);
        buf.writeVarInt(recipe.getEnchantMap().size());
        for (Map.Entry<Enchantment,Integer> entry: recipe.getEnchantMap().entrySet()){
            buf.writeResourceLocation(BuiltInRegistries.ENCHANTMENT.getKey(entry.getKey()));
            buf.writeVarInt(entry.getValue());
        }
        buf.writeItem(recipe.getResult());
    }
}
