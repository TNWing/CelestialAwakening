package com.github.celestial_awakening.events;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.living.night_prowlers.ProwlerWhelp;
import com.github.celestial_awakening.entity.living.phantom_knights.PhantomKnight_Crescencia;
import com.github.celestial_awakening.entity.living.solmanders.SolmanderNewt;
import com.github.celestial_awakening.entity.living.transcendents.Asteron;
import com.github.celestial_awakening.entity.living.transcendents.Astralite;
import com.github.celestial_awakening.entity.living.transcendents.Nebure;
import com.github.celestial_awakening.init.EntityInit;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= CelestialAwakening.MODID,bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusManager {
    @SubscribeEvent
    public static void onRegisterSpawnPlacements(SpawnPlacementRegisterEvent event){
        event.register(EntityInit.NIGHT_PROWLER_WHELP.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,CA_SpawnPlacements.dark_NightSurface, SpawnPlacementRegisterEvent.Operation.REPLACE);
        //event.register(EntityInit.SOLMANDER_NEWT.get(), SpawnPlacements.Type.IN_LAVA,Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,CA_SpawnPlacements.lava_daySurface,SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event){//called before config load
        event.put(EntityInit.ASTERON.get(), Asteron.createAttributes().build());
        event.put(EntityInit.ASTRALITE.get(), Astralite.createAttributes().build());
        event.put(EntityInit.NEBURE.get(), Nebure.createAttributes().build());

        event.put(EntityInit.NIGHT_PROWLER_WHELP.get(), ProwlerWhelp.createAttributes().build());
        event.put(EntityInit.SOLMANDER_NEWT.get(), SolmanderNewt.createAttributes().build());
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
