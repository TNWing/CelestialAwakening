package com.github.celestial_awakening.datagen.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class DaytimeBaseCondition implements LootItemCondition {
    //honestly i could probs replace this w/ a simple time check
    /*
    the downside is that if a level has a different time period frame (say 36000 ticks/day), then time check isnt as ideal
    but also, the isDay() method calculates if its day based on the skydarken variable, not by the game tickcount
    speaking of which, now i need to test to see if diviner AoD breaks the isDay method
     */
    boolean dayBoost;//true if boosted during day, false if boosted during night

    public DaytimeBaseCondition(boolean boost) {
        this.dayBoost = boost;

    }

    @Override
    public LootItemConditionType getType() {
        return null;
    }

    @Override
    public boolean test(LootContext lootContext) {
        return lootContext.getLevel().isDay() == dayBoost;
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<DaytimeBaseCondition> {

        @Override
        public void serialize(JsonObject jsonObject, DaytimeBaseCondition cond, JsonSerializationContext context) {
            jsonObject.addProperty("day_boost",cond.dayBoost);
        }

        @Override
        public DaytimeBaseCondition deserialize(JsonObject jsonObject, JsonDeserializationContext context) {
            boolean b=jsonObject.has("day_boost")? GsonHelper.getAsBoolean(jsonObject,"day_boost"):true;
            return new DaytimeBaseCondition(b);
        }
    }
}
