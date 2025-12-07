package com.github.celestial_awakening.recipes.serializers;

import com.github.celestial_awakening.recipes.BasicEnchantedSmithingRecipe;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BasicEnchantedSmithingSerializer implements RecipeSerializer<BasicEnchantedSmithingRecipe>, EnchantedSmithingSerializer {
    /*
    json is listed like so for requirements
    requirements[
        {
        "capability":name
        "<requirement_type>:x
        }
    ]

    requirement_type can be things like equals, not, greater_equals, greater, etc
     */
    @Override
    public BasicEnchantedSmithingRecipe fromJson(ResourceLocation loc, JsonObject json) {

        Ingredient ingredient = Ingredient.fromJson(GsonHelper.getNonNull(json, "template"));
        Ingredient ingredient1 = Ingredient.fromJson(GsonHelper.getNonNull(json, "base"));
        Ingredient ingredient2 = Ingredient.fromJson(GsonHelper.getNonNull(json, "addition"));
        ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        HashMap<Enchantment,Integer> enchantMap=checkEnchants(json);
        return new BasicEnchantedSmithingRecipe(loc,ingredient,ingredient1,ingredient2,itemstack,enchantMap);
    }

    @Override
    public @Nullable BasicEnchantedSmithingRecipe fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {

        Ingredient ingredient = Ingredient.fromNetwork(buf);
        Ingredient ingredient1 = Ingredient.fromNetwork(buf);
        Ingredient ingredient2 = Ingredient.fromNetwork(buf);

        HashMap<Enchantment,Integer> enchantMap=bufReader(buf);
        /*
        int size2=buf.readVarInt();
        ArrayList<BasicEnchantedSmithingRecipe.Requirement> requirements=new ArrayList<>();
        for(int i=0;i<size2;i++){
            CompoundTag tag=buf.readNbt();
            requirements.add(new BasicEnchantedSmithingRecipe.Requirement
                    (tag.getString("cap"),tag.getString("method"),
                            tag.getString("type"),tag.getString("result")));
        }

         */

        ItemStack itemstack = buf.readItem();
        return new BasicEnchantedSmithingRecipe(loc,ingredient,ingredient1,ingredient2,itemstack,enchantMap);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, BasicEnchantedSmithingRecipe recipe) {
        recipe.getTemplate().toNetwork(buf);
        recipe.getBase().toNetwork(buf);
        recipe.getAddition().toNetwork(buf);
        buf.writeVarInt(recipe.getEnchantMap().size());
        for (Map.Entry<Enchantment,Integer> entry: recipe.getEnchantMap().entrySet()){
            buf.writeResourceLocation(BuiltInRegistries.ENCHANTMENT.getKey(entry.getKey()));
            buf.writeVarInt(entry.getValue());
        }
        /*
        buf.writeVarInt(recipe.getRequirements().size());
        for (BasicEnchantedSmithingRecipe.Requirement requirement: recipe.getRequirements()){
            CompoundTag tag=new CompoundTag();

              (obj.get("capability").getAsString(),
                                obj.get("data").getAsString(),
                                obj.get("type").getAsString(),
                                obj.get("result").getAsString());

            tag.putString("cap",requirement.getCapabilityName());
            tag.putString("method",requirement.getMethodName());
            tag.putString("type",requirement.getType());
            tag.putString("result",requirement.getResult());
            buf.writeNbt(tag);
        }
        */
        buf.writeItem(recipe.getResult());
    }
}
