package com.github.celestial_awakening.recipes.serializers;

import com.github.celestial_awakening.recipes.BasicEnchantedSmithingRecipe;
import com.github.celestial_awakening.recipes.CapabilityModificationRecipe;
import com.github.celestial_awakening.recipes.SearedStoneToolsSmithingUpgradeRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.enchantment.Enchantment;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearedStoneToolsSmithingSerializer implements RecipeSerializer<SearedStoneToolsSmithingUpgradeRecipe>, EnchantedSmithingSerializer {
    @Override
    public SearedStoneToolsSmithingUpgradeRecipe fromJson(ResourceLocation loc, JsonObject json) {

        Ingredient ingredient = Ingredient.fromJson(GsonHelper.getNonNull(json, "template"));
        Ingredient ingredient1 = Ingredient.fromJson(GsonHelper.getNonNull(json, "base"));
        Ingredient ingredient2 = Ingredient.fromJson(GsonHelper.getNonNull(json, "addition"));
        HashMap<Enchantment,Integer> enchantMap=checkEnchants(json);
        ItemStack itemstack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
        ArrayList<CapabilityModificationRecipe.Requirement> requirements=new ArrayList<>();
        if (json.has("requirements")){
            JsonArray arr=json.getAsJsonArray("requirements");
            for (JsonElement ele:arr) {
                JsonObject obj = ele.getAsJsonObject();
                CapabilityModificationRecipe.Requirement requirement=new CapabilityModificationRecipe.Requirement(
                        obj.get("capability").getAsString(),
                                obj.get("method").getAsString(),
                                obj.get("type").getAsString(),
                                obj.get("val").getAsString());
                requirements.add(requirement);
            }
        }
        ArrayList<CapabilityModificationRecipe.CapabilityResult> results=new ArrayList<>();
        if (json.has("capability_mod")){
            JsonArray arr=json.getAsJsonArray("capability_mod");
            for (JsonElement ele : arr) {
                JsonObject obj = ele.getAsJsonObject();
                if (obj.has("SearedStone")) {
                    JsonObject searedStoneObj = obj.getAsJsonObject("SearedStone");
                    int tier = searedStoneObj.get("setUpgradeTier").getAsInt();
                    CapabilityModificationRecipe.CapabilityResult capabilityResult=new CapabilityModificationRecipe.CapabilityResult("SearedStone","setUpgradeTier",new String[]{String.valueOf(tier)});
                    results.add(capabilityResult);
                }
            }
        }
        return new SearedStoneToolsSmithingUpgradeRecipe(loc,ingredient,ingredient1,ingredient2,itemstack,enchantMap,requirements,results);
    }

    @Override
    public @Nullable SearedStoneToolsSmithingUpgradeRecipe fromNetwork(ResourceLocation loc, FriendlyByteBuf buf) {
        Ingredient ingredient = Ingredient.fromNetwork(buf);
        Ingredient ingredient1 = Ingredient.fromNetwork(buf);
        Ingredient ingredient2 = Ingredient.fromNetwork(buf);

        HashMap<Enchantment,Integer> enchantMap=bufReader(buf);

        int reqSize=buf.readVarInt();
        ArrayList<CapabilityModificationRecipe.Requirement> requirements=new ArrayList<>();
        for(int i=0;i<reqSize;i++){
            CompoundTag tag=buf.readNbt();
            requirements.add(new CapabilityModificationRecipe.Requirement
                    (tag.getString("cap"),tag.getString("method"),
                            tag.getString("type"),tag.getString("val")));
        }
        int resSize=buf.readVarInt();
        ArrayList<CapabilityModificationRecipe.CapabilityResult> results=new ArrayList<>();
        for (int i=0;i<resSize;i++){
            CompoundTag tag=buf.readNbt();
            ListTag listTag= (ListTag)tag.get("params");
            String[] strArr=new String[listTag.size()];
            for (int k=0;k<strArr.length;k++){
                strArr[k]=listTag.getString(k);
            }
            results.add(new CapabilityModificationRecipe.CapabilityResult(
               tag.getString("cap"),tag.getString("method"),strArr
            ));
        }

        ItemStack itemstack = buf.readItem();
        return new SearedStoneToolsSmithingUpgradeRecipe(loc,ingredient,ingredient1,ingredient2,itemstack,enchantMap,requirements,results);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf, SearedStoneToolsSmithingUpgradeRecipe recipe) {
        recipe.getTemplate().toNetwork(buf);
        recipe.getBase().toNetwork(buf);
        recipe.getAddition().toNetwork(buf);
        buf.writeVarInt(recipe.getEnchantMap().size());
        for (Map.Entry<Enchantment,Integer> entry: recipe.getEnchantMap().entrySet()){
            buf.writeResourceLocation(BuiltInRegistries.ENCHANTMENT.getKey(entry.getKey()));
            buf.writeVarInt(entry.getValue());
        }

        buf.writeVarInt(recipe.getRequirements().size());
        for (CapabilityModificationRecipe.Requirement requirement: recipe.getRequirements()){
            CompoundTag tag=new CompoundTag();
            /*
            tag.putString("Capability");
              (obj.get("capability").getAsString(),
                                obj.get("data").getAsString(),
                                obj.get("type").getAsString(),
                                obj.get("result").getAsString());

             */

            tag.putString("cap",requirement.getCapabilityName());
            tag.putString("method",requirement.getMethodName());
            tag.putString("type",requirement.getType());
            tag.putString("val",requirement.getResult());
            buf.writeNbt(tag);
        }
        buf.writeVarInt(recipe.getResults().size());
        for (CapabilityModificationRecipe.CapabilityResult result:recipe.getResults()){
            CompoundTag tag=new CompoundTag();
            tag.putString("cap",result.getCapabilityName());
            tag.putString("method",result.getMethodName());

            ListTag paramTag=new ListTag();

            for (String param:result.getParam()){
                paramTag.add(StringTag.valueOf(param));
            }
            tag.put("params",paramTag);
            buf.writeNbt(tag);
        }
        buf.writeItem(recipe.getResult());
    }
}
