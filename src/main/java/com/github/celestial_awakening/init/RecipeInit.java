package com.github.celestial_awakening.init;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.recipes.serializers.LunarScaleRepairSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RecipeInit {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_DEFERRED_REGISTER = DeferredRegister.create( ForgeRegistries.RECIPE_SERIALIZERS, CelestialAwakening.MODID);
    //why does the deferred register cause issues?
    public static final RegistryObject<RecipeSerializer> LUNAR_SCALE_REPAIR_SERIALIZER = RECIPE_SERIALIZER_DEFERRED_REGISTER.register("lunar_scale_repair", ()->new LunarScaleRepairSerializer());

}
