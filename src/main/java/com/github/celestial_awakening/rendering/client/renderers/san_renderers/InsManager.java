package com.github.celestial_awakening.rendering.client.renderers.san_renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;

import static net.minecraft.client.renderer.entity.LivingEntityRenderer.isEntityUpsideDown;

@OnlyIn(Dist.CLIENT)
public class InsManager {
    HashMap<String, RendererHolder> rendererHolderHashMap;
    HashMap<Entity,AppearanceData> entityAppearanceMap;

    public static InsManager INSTANCE;

    static class AppearanceData{
        AppearanceData(String name,int d){
            modelName=name;
            this.delay=d;
        }
        int delay;//delay until next possible model change
        String modelName;
    }
    class RendererHolder{
        EntityModel model;
        ResourceLocation texture;
        float shadow;
        RendererHolder(EntityModel m,ResourceLocation loc,float s){
            this.model=m;
            this.texture=loc;
            this.shadow=s;
        }
    }

    void insertIntoModelMap(String name,EntityModel model,ResourceLocation loc, float shadow){
        INSTANCE.rendererHolderHashMap.put(name,new RendererHolder(model,loc,shadow));
    }

    public void removeFromEntityMap(Entity ent){
        if (INSTANCE.entityAppearanceMap.containsKey(ent)){
            INSTANCE.entityAppearanceMap.remove(ent);
        }
    }
    void insertIntoEntityMap(Entity ent,AppearanceData data){
        INSTANCE.entityAppearanceMap.put(ent,data);
    }

    public void init(EntityRendererProvider.Context context){
        if (INSTANCE==null){
            INSTANCE=new InsManager();
        }
        INSTANCE.insertIntoModelMap("pillager",new IllagerModel<>(context.bakeLayer(ModelLayers.PILLAGER)), new ResourceLocation("textures/entity/illager/pillager.png"),0.5f);
        INSTANCE.insertIntoModelMap("vindicator",new IllagerModel<>(context.bakeLayer(ModelLayers.VINDICATOR)), new ResourceLocation("textures/entity/illager/vindicator.png"),0.5f);
        //INSTANCE.insertIntoMap();
    }


}
