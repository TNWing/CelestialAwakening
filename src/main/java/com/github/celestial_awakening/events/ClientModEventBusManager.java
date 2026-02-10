package com.github.celestial_awakening.events;

import com.github.celestial_awakening.CelestialAwakening;
import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.init.EntityInit;
import com.github.celestial_awakening.init.ModelLayerInit;
import com.github.celestial_awakening.rendering.client.models.*;
import com.github.celestial_awakening.rendering.client.renderers.*;
import com.github.celestial_awakening.rendering.client.renderers.san_renderers.InsManager;
import com.github.celestial_awakening.rendering.client.renderers.san_renderers.InsPigRenderer;
import com.github.celestial_awakening.rendering.client.renderers.san_renderers.InsVillagerRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT,modid= CelestialAwakening.MODID,bus=Mod.EventBusSubscriber.Bus.MOD)
public class ClientModEventBusManager {

    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event){//move renderers to dedicated event?
        /*
        EntityRenderers.register(EntityInit.ASTERON.get(), TranscendentAsteronRenderer::new);
        EntityRenderers.register(EntityInit.ASTRALITE.get(), TranscendentAstraliteRenderer::new);
        EntityRenderers.register(EntityInit.NEBURE.get(), TranscendentNebureRenderer::new);

        EntityRenderers.register(EntityInit.LIGHT_RAY.get(), LightRayRenderer::new);
        EntityRenderers.register(EntityInit.SHINING_ORB.get(), ShiningOrbRenderer::new);
        EntityRenderers.register(EntityInit.LUNAR_CRESCENT.get(), LunarCrescentRenderer::new);
        EntityRenderers.register(EntityInit.ORBITER.get(), OrbiterRenderer::new);
        EntityRenderers.register(EntityInit.MOONLIGHT_ORB.get(), MoonlightOrbRenderer::new);
        EntityRenderers.register(EntityInit.NIGHT_PROWLER_WHELP.get(), NightProwlerRenderer::new);
        EntityRenderers.register(EntityInit.CUSTOM_ARROW.get(), CArrowRenderer::new);

        EntityRenderers.register(EntityInit.PK_CRESCENCIA.get(), PKCrescenciaRenderer::new);
        
         */

        CA_ItemProperties.addProperties();
    }
    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(ModelLayerInit.ASTERON_LAYER, TranscendentAsteronModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayerInit.LIGHT_RAY_LAYER, LightRayModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayerInit.ASTRALITE_LAYER, TranscendentAstraliteModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayerInit.SHINING_ORB_LAYER, ShiningOrbModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayerInit.ORBITER_LAYER, OrbiterModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayerInit.LUNAR_CRESCENT_LAYER, LunarCrescentModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayerInit.NIGHT_PROWLER_LAYER,NightProwlerModel::createBodyLayer);
        event.registerLayerDefinition(ModelLayerInit.PK_CRESCENCIA_LAYER, PKCrescenciaModel::createBodyLayer);


    }
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event){
        if (Config.insVisuals!= Config.INS_VISUALS.NONE){
            event.registerEntityRenderer(EntityType.VILLAGER,ctx->{
                InsManager.init(ctx);
                return new InsVillagerRenderer(ctx, (VillagerRenderer) ctx.getEntityRenderDispatcher().renderers.get(EntityType.VILLAGER));
            });
            event.registerEntityRenderer(EntityType.PIG,ctx->{
                InsManager.init(ctx);
                return new InsPigRenderer<>(ctx,(PigRenderer) ctx.getEntityRenderDispatcher().renderers.get(EntityType.PIG));
            });
        }

        event.registerEntityRenderer(EntityInit.SOLMANDER_NEWT.get(), SolmanderNewtRenderer::new);
        event.registerEntityRenderer(EntityInit.CORE_GUARDIAN.get(), CoreGuardianRenderer::new);
        event.registerEntityRenderer(EntityInit.ASTERON.get(), TranscendentAsteronRenderer::new);
        event.registerEntityRenderer(EntityInit.ASTRALITE.get(), TranscendentAstraliteRenderer::new);
        event.registerEntityRenderer(EntityInit.NEBURE.get(), TranscendentNebureRenderer::new);

        event.registerEntityRenderer(EntityInit.LIGHT_RAY.get(), LightRayRenderer::new);
        event.registerEntityRenderer(EntityInit.SHINING_ORB.get(), ShiningOrbRenderer::new);
        event.registerEntityRenderer(EntityInit.LUNAR_CRESCENT.get(), LunarCrescentRenderer::new);
        event.registerEntityRenderer(EntityInit.ORBITER.get(), OrbiterRenderer::new);
        event.registerEntityRenderer(EntityInit.MOONLIGHT_ORB.get(), MoonlightOrbRenderer::new);
        event.registerEntityRenderer(EntityInit.NIGHT_PROWLER_WHELP.get(), NightProwlerRenderer::new);
        event.registerEntityRenderer(EntityInit.CUSTOM_ARROW.get(), CArrowRenderer::new);
        event.registerEntityRenderer(EntityInit.COSMIC_PEARL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(EntityInit.GENERIC_SHARD.get(), GenericShardRenderer::new);
        event.registerEntityRenderer(EntityInit.PK_CRESCENCIA.get(), PKCrescenciaRenderer::new);



    }





}
