package com.github.celestial_awakening.init;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.entity.living.night_prowlers.ProwlerWhelp;
import com.github.celestial_awakening.entity.living.phantom_knights.PhantomKnight_Crescencia;
import com.github.celestial_awakening.entity.living.planetary_guardians.CoreGuardian;
import com.github.celestial_awakening.entity.living.solmanders.SolmanderNewt;
import com.github.celestial_awakening.entity.living.transcendents.Asteron;
import com.github.celestial_awakening.entity.living.transcendents.Astralite;
import com.github.celestial_awakening.entity.living.transcendents.Nebure;
import com.github.celestial_awakening.entity.projectile.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES=DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CelestialAwakening.MODID);

    public static final RegistryObject<EntityType<Asteron>> ASTERON =ENTITY_TYPES.register("transcendent_asteron",
            ()->EntityType.Builder.of(Asteron::new, MobCategory.MONSTER).sized(1f,1f).build("transcendent_asteron"));

    public static final RegistryObject<EntityType<Astralite>> ASTRALITE =ENTITY_TYPES.register("transcendent_astralite",
            ()->EntityType.Builder.of(Astralite::new,MobCategory.MONSTER).sized(1f,1f).build("transcendent_astralite"));

    public static final RegistryObject<EntityType<Nebure>> NEBURE=ENTITY_TYPES.register("transcendent_nebure",
            ()->EntityType.Builder.of(Nebure::new,MobCategory.MONSTER).sized(1.5f,3f).build("transcendent_nebure"));

    public static final RegistryObject<EntityType<ProwlerWhelp>> NIGHT_PROWLER_WHELP =ENTITY_TYPES.register("night_prowler_whelp",
            ()->EntityType.Builder.of(ProwlerWhelp::new,MobCategory.MONSTER).build("night_prowler_whelp"));

    public static final RegistryObject<EntityType<SolmanderNewt>> SOLMANDER_NEWT=ENTITY_TYPES.register("solmander_newt",
            ()->EntityType.Builder.of(SolmanderNewt::new,MobCategory.MONSTER).fireImmune().sized(1.6f,1f).build("solmander_newt"));



    public static final RegistryObject<EntityType<PhantomKnight_Crescencia>> PK_CRESCENCIA=ENTITY_TYPES.register("pk_crescencia",
            ()->EntityType.Builder.of(PhantomKnight_Crescencia::new,MobCategory.MONSTER).sized(1f,2f).build("pk_crescencia"));





    public static final RegistryObject<EntityType<CoreGuardian>> CORE_GUARDIAN=ENTITY_TYPES.register("core_guardian",
            ()->EntityType.Builder.of(CoreGuardian::new,MobCategory.MONSTER).sized(1.4f,2.6f).build("core_guardian"));










    public static final RegistryObject<EntityType<LightRay>> LIGHT_RAY=ENTITY_TYPES.register("light_ray",
            ()->EntityType.Builder.<LightRay>of(LightRay::new,MobCategory.MISC).sized(1f,1f).build("light_ray")
    );

    public static final RegistryObject<EntityType<PhotonOrb>> PHOTON_ORB = ENTITY_TYPES.register("photon_orb",()->EntityType.Builder.<PhotonOrb>of(PhotonOrb::new,MobCategory.MISC).build("photon_orb"));

    public static final RegistryObject<EntityType<ShiningOrb>> SHINING_ORB=ENTITY_TYPES.register("shining_orb",
            ()->EntityType.Builder.<ShiningOrb>of(ShiningOrb::new,MobCategory.MISC).sized(0.8f,0.8f).build("shining_orb"));

    public static final RegistryObject<EntityType<LunarCrescent>> LUNAR_CRESCENT=ENTITY_TYPES.register("lunar_crescent",
            ()->EntityType.Builder.<LunarCrescent>of(LunarCrescent::new,MobCategory.MISC).sized(1f,0.2f).build("lunar_crescent"));

    public static final RegistryObject<EntityType<OrbiterProjectile>> ORBITER=ENTITY_TYPES.register("orbiter",
            ()->EntityType.Builder.<OrbiterProjectile>of(OrbiterProjectile::new,MobCategory.MISC).sized(0.3f,0.3f).build("orbiter"));
    public static final RegistryObject<EntityType<CA_ArrowProjectile>> CUSTOM_ARROW=ENTITY_TYPES.register("ca_arrow",
            ()->EntityType.Builder.<CA_ArrowProjectile>of(CA_ArrowProjectile::new,MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20).build("ca_arrow"));


    public static final RegistryObject<EntityType<MoonlightOrb>> MOONLIGHT_ORB=ENTITY_TYPES.register("moonlight_orb",
            ()->EntityType.Builder.<MoonlightOrb>of(MoonlightOrb::new,MobCategory.MISC).sized(0.5f,0.5f).build("moonlight_orb"));

    public static final RegistryObject<EntityType<CosmicPearlProjectile>> COSMIC_PEARL=ENTITY_TYPES.register("cosmic_pearl",()->EntityType.Builder.<CosmicPearlProjectile>of(CosmicPearlProjectile::new,MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("cosmic_pearl"));

}
