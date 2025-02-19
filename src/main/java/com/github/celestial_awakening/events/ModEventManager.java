package com.github.celestial_awakening.events;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.init.EntityInit;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid= CelestialAwakening.MODID,bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModEventManager {
    @SubscribeEvent
    public static void onRegisterSpawnPlacements(SpawnPlacementRegisterEvent event){

        System.out.println("REGISTERING SPAWN PLACEMENTS");
        event.register(EntityInit.NIGHT_PROWLER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,CA_SpawnPlacements.dark_NightSurface, SpawnPlacementRegisterEvent.Operation.REPLACE);
        System.out.println("POST REGISTER SP");
    }
}
