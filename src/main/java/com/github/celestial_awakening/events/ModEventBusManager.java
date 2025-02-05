package com.github.celestial_awakening.events;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.living.NightProwler;
import com.github.celestial_awakening.entity.living.phantom_knights.PhantomKnight_Crescencia;
import com.github.celestial_awakening.entity.living.transcendents.Astralite;
import com.github.celestial_awakening.entity.living.transcendents.Asteron;
import com.github.celestial_awakening.entity.living.transcendents.Nebure;
import com.github.celestial_awakening.init.EntityInit;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= CelestialAwakening.MODID,bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusManager {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){
        event.put(EntityInit.ASTERON.get(), Asteron.createAttributes().build());
        event.put(EntityInit.ASTRALITE.get(), Astralite.createAttributes().build());
        event.put(EntityInit.NEBURE.get(), Nebure.createAttributes().build());

        event.put(EntityInit.NIGHT_PROWLER.get(), NightProwler.createAttributes().build());
        event.put(EntityInit.PK_CRESCENCIA.get(), PhantomKnight_Crescencia.createAttributes().build());
    }
    /*
    @SubscribeEvent
    public static void updateAttributes(EntityAttributeModificationEvent event){
        Map<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> attributeMap = new HashMap<>();

        System.out.println("UA CONFIG H IS " + Config.mobHPScale);
        SolFollower.addScaledAttributes(event,EntityInit.asteron.get());
        SolAcolyte.addScaledAttributes(event,EntityInit.astralite.get());
        NightProwler.addScaledAttributes(event,EntityInit.NIGHT_PROWLER.get());
        PhantomKnight_Crescencia.addScaledAttributes(event,EntityInit.PK_CRESCENCIA.get());
        attributeMap.forEach((k, v) ->
        {
            AttributeSupplier supplier = DefaultAttributes.getSupplier(k);
            AttributeSupplier.Builder newBuilder = supplier != null ? new AttributeSupplier.Builder(supplier) : new AttributeSupplier.Builder();
            newBuilder.combine(v);
            FORGE_ATTRIBUTES.put(k, newBuilder.build());
        });
    }

     */

}
