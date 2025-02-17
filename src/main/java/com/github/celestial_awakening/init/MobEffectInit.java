package com.github.celestial_awakening.init;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.effects.*;
import com.github.celestial_awakening.util.CA_UUIDs;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MobEffectInit {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS=DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CelestialAwakening.MODID);

    public static final RegistryObject<MobEffect> PURGING_LIGHT =MOB_EFFECTS.register("purging_light",()->new PurgingLight(MobEffectCategory.HARMFUL, 16750848));
    public static final RegistryObject<MobEffect> EXPOSING_LIGHT=MOB_EFFECTS.register("exposing_light",()->new ExposingLight(MobEffectCategory.HARMFUL, 16750848));

    public static final RegistryObject<MobEffect> CELESTIAL_BEACON=MOB_EFFECTS.register("celestial_beacon",()
    ->new CelestialBeacon(MobEffectCategory.NEUTRAL,16750848));

    public static final RegistryObject<MobEffect> REMNANT_FL=MOB_EFFECTS.register("remnant_fl",()
            ->new CustomEffect(MobEffectCategory.NEUTRAL,1675084,false)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, String.valueOf(CA_UUIDs.remnantFinalLightID),0.2f,AttributeModifier.Operation.MULTIPLY_TOTAL)
            .addAttributeModifier(Attributes.ATTACK_SPEED, String.valueOf(CA_UUIDs.remnantFinalLightID),0.2f,AttributeModifier.Operation.MULTIPLY_TOTAL)
    );

    public static final RegistryObject<MobEffect> PHOTOSYNTHESIS=MOB_EFFECTS.register("photosynthesis",()->
            new Photosynthesis(MobEffectCategory.BENEFICIAL,16750848));
    public static final RegistryObject<MobEffect> MARK_OF_HAUNTING=MOB_EFFECTS.register("mark_of_haunting",()->
            new MarkOfHaunting(MobEffectCategory.NEUTRAL,16750848));
}

