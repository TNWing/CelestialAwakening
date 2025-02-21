package com.github.celestial_awakening.init;

import com.github.celestial_awakening.CelestialAwakening;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModelLayerInit {
    public static final ModelLayerLocation ASTERON_LAYER =new ModelLayerLocation(
      new ResourceLocation(CelestialAwakening.MODID,"transcendent_asteron_layer"),"main");
    public static final ModelLayerLocation LIGHT_RAY_LAYER=new ModelLayerLocation(CelestialAwakening.createResourceLocation("light_ray_layer"),"main");
    public static final ModelLayerLocation ASTRALITE_LAYER =new ModelLayerLocation(
            new ResourceLocation(CelestialAwakening.MODID,"transcendent_astralite_layer"),"main");
    public static final ModelLayerLocation SHINING_ORB_LAYER=new ModelLayerLocation(CelestialAwakening.createResourceLocation("shining_orb_layer"),"main");
    public static final ModelLayerLocation ORBITER_LAYER=new ModelLayerLocation(CelestialAwakening.createResourceLocation("orbiter_layer"),"main");
    public static final ModelLayerLocation LUNAR_CRESCENT_LAYER=new ModelLayerLocation(CelestialAwakening.createResourceLocation("lunar_crescent_layer"),"main");
    public static final ModelLayerLocation NIGHT_PROWLER_LAYER=new ModelLayerLocation(CelestialAwakening.createResourceLocation("night_prowler_layer"),"main");


    public static final ModelLayerLocation PK_CRESCENCIA_LAYER=new ModelLayerLocation(CelestialAwakening.createResourceLocation("pk_crescencia_layer"),"main");
}
