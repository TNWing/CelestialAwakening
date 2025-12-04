package com.github.celestial_awakening.init;

import com.github.celestial_awakening.CelestialAwakening;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundInit {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS=DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CelestialAwakening.MODID);

    public static final RegistryObject<SoundEvent> TRANSCENDENT_DIV_OPEN=registerSoundEvent("trans_div_open");

    public static final RegistryObject<SoundEvent> TRANSCENDENT_SPAWN_1=registerSoundEvent("trans_spawn_1");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name){
        return SOUND_EVENTS.register(name,()->SoundEvent.createVariableRangeEvent(CelestialAwakening.createResourceLocation(name)));
    }
}
