package com.github.celestial_awakening.datagen.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class DaytimeBonusCondition implements LootItemCondition {
    boolean dayBoost;//true if boosted during day, false if boosted during night
    float chance;
    float lootingMult;

    public DaytimeBonusCondition(boolean boost, float baseChance, float looting) {
        this.dayBoost = boost;
        this.chance = baseChance;
        this.lootingMult = looting;

    }

    @Override
    public LootItemConditionType getType() {
        return null;
    }

    @Override
    public boolean test(LootContext lootContext) {
        boolean timeFlag = lootContext.getLevel().isDay() == dayBoost;
        int i = lootContext.getLootingModifier();
        return timeFlag && lootContext.getRandom().nextFloat() < this.chance + (float) i * this.lootingMult;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<DaytimeBonusCondition> {

        @Override
        public void serialize(JsonObject jsonObject, DaytimeBonusCondition cond, JsonSerializationContext context) {
            jsonObject.addProperty("day_boost",cond.dayBoost);
            jsonObject.addProperty("chance",cond.chance);
            jsonObject.addProperty("looting_multiplier",cond.lootingMult);
        }

        @Override
        public DaytimeBonusCondition deserialize(JsonObject jsonObject, JsonDeserializationContext context) {
            boolean b=jsonObject.has("day_boost")? GsonHelper.getAsBoolean(jsonObject,"day_boost"):true;
            float chance=jsonObject.has("chance")? GsonHelper.getAsFloat(jsonObject,"chance"):0;
            float mult=jsonObject.has("looting_multiplier")? GsonHelper.getAsFloat(jsonObject,"looting_multiplier"):0f;
            return new DaytimeBonusCondition(b,chance,mult);
        }
    }
}
