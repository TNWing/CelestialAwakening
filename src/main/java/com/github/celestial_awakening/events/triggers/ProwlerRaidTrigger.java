package com.github.celestial_awakening.events.triggers;

import com.github.celestial_awakening.CelestialAwakening;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ProwlerRaidTrigger extends SimpleCriterionTrigger<ProwlerRaidTrigger.TriggerInstance> {
    static final ResourceLocation ID = CelestialAwakening.createResourceLocation("prowler_raid");
    @Override
    protected ProwlerRaidTrigger.TriggerInstance createInstance(JsonObject p_66248_, ContextAwarePredicate contextAwarePredicate, DeserializationContext p_66250_) {
        return new ProwlerRaidTrigger.TriggerInstance(contextAwarePredicate);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player){
        System.out.println("TRIGGERING FOR PLAYer   " + player.getName());
        this.trigger(player,p->{
            return p.matches();
        });
    }

    public static class TriggerInstance extends AbstractCriterionTriggerInstance{

        public TriggerInstance(ContextAwarePredicate p_286466_) {
            super(ProwlerRaidTrigger.ID, p_286466_);
        }
        public boolean matches(){
            return true;
        }
        public JsonObject serializeToJson(SerializationContext p_65486_) {
            JsonObject jsonobject = super.serializeToJson(p_65486_);
            return jsonobject;
        }
    }
}
