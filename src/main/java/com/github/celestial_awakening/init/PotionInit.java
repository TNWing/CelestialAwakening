package com.github.celestial_awakening.init;

import com.github.celestial_awakening.CelestialAwakening;
import com.mojang.blaze3d.shaders.Effect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class PotionInit {
    public static final DeferredRegister<Potion> POTIONS=DeferredRegister.create(ForgeRegistries.POTIONS, CelestialAwakening.MODID);

    public static final RegistryObject<Potion> LUNAR_GLEAM=POTIONS.register("lunar_gleam",()->new Potion(
            new MobEffectInstance(MobEffects.CONFUSION, 300),
            new MobEffectInstance(MobEffects.SATURATION, 60),
            new MobEffectInstance(MobEffects.LUCK, 500),
            new MobEffectInstance(MobEffects.JUMP, 500)
    ));

    public static final RegistryObject<Potion> PHOTOGENIC=POTIONS.register("photogenic",()->new Potion(
            new MobEffectInstance(MobEffects.BLINDNESS, 60),
            new MobEffectInstance(MobEffects.NIGHT_VISION, 600),
            new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300)
    ));

    public static final RegistryObject<Potion> PHOTOSYNTHESIS=POTIONS.register("photosynthesis",()->new Potion(
            new MobEffectInstance(MobEffectInit.PHOTOSYNTHESIS.get(), 2400)
    ));

    public static final RegistryObject<Potion> LUNAR_RESTORATION=POTIONS.register("lunar_restoration",()->new Potion(
            new MobEffectInstance(MobEffectInit.LUNAR_RESTORATION.get(), 2400)
    ));
}
