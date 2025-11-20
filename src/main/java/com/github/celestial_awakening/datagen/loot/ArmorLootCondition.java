package com.github.celestial_awakening.datagen.loot;

import com.github.celestial_awakening.init.LootInit;
import com.github.celestial_awakening.items.CustomArmorMaterial;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.TimeCheck;

import javax.annotation.Nullable;

public class ArmorLootCondition implements LootItemCondition {
    final ArmorMaterial material;
    final float chancePerPiece;

    public ArmorLootCondition(String mat,float r){
        ArmorMaterial m=null;
        try{
            m=CustomArmorMaterial.valueOf(mat.toUpperCase());
        }
        catch (Exception e){
        }
        finally {
            material=m;
            this.chancePerPiece=r;
        }


    }
    @Override
    public LootItemConditionType getType() {
        return LootInit.ARMOR_COND.get();
    }

    @Override
    public boolean test(LootContext lootContext) {
        if (material == null || !lootContext.hasParam(LootContextParams.KILLER_ENTITY)){
            return false;
        }
        Entity entity=lootContext.getParam(LootContextParams.KILLER_ENTITY);
        if (entity instanceof LivingEntity livingEntity) {
            Iterable<ItemStack> armorSlots = livingEntity.getArmorSlots();

            for (ItemStack armorSlot : armorSlots) {
                if(!armorSlot.isEmpty() && (armorSlot.getItem() instanceof ArmorItem armorItem)) {
                    if (armorItem.getMaterial()==material){
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //may not need builder
    public static class Builder implements LootItemCondition.Builder {
        String material;
        float chancePerPiece;
        Builder(String m, float c){
            this.material=m;
            this.chancePerPiece=c;
        }
        @Override
        public LootItemCondition build() {
            return new ArmorLootCondition(this.material,chancePerPiece);
        }
    }
    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<ArmorLootCondition>{

        @Override
        public void serialize(JsonObject jsonObject, ArmorLootCondition cond, JsonSerializationContext context) {
            jsonObject.addProperty("armor",cond.material.toString());
            jsonObject.addProperty("chancePerPiece",cond.chancePerPiece);

        }

        @Override
        public ArmorLootCondition deserialize(JsonObject jsonObject, JsonDeserializationContext context) {
            String mat=jsonObject.has("armor")? GsonHelper.getAsString(jsonObject,"armor"):null;
            float chance=jsonObject.has("chancePerPiece")? GsonHelper.getAsFloat(jsonObject,"chancePerPiece"):0;
            return new ArmorLootCondition(mat,chance);
        }
    }
}
