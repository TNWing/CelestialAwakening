package com.github.celestial_awakening.effects;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.entity.living.transcendents.AbstractTranscendent;
import com.github.celestial_awakening.entity.living.transcendents.Astralite;
import com.github.celestial_awakening.entity.living.transcendents.Asteron;
import com.github.celestial_awakening.entity.living.transcendents.Nebure;
import com.github.celestial_awakening.events.custom_events.TranscendentSpawnEvent;
import com.github.celestial_awakening.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.Random;

public class CelestialBeacon extends MobEffect {
    /*
    instead, make it work like omen, but instead of triggering when entering village
    -timer represents time to prepare
    -amp represents raid strength
     */

    //alternatively, have the waves function like so
    /*
    a=amplifier
    every X seconds, summon a wave
    subsequent waves have increase difficulty
    x=60/a
    by default, the effect lasts 60 seconds

    i suppose i can make a custom mob instance?
     */
    public CelestialBeacon(MobEffectCategory p_19451_, int color) {
        super(p_19451_, color);
    }

    @Override
    public boolean isDurationEffectTick(int ticks, int amplifier) {
        int k=1200>>amplifier;//bitshift right b
        if (k>0){
            return ticks%k==1;
        }
        else{
            return true;
        }
    }


    @Override
    public void applyEffectTick(LivingEntity target, int amplifier) {
        applyEffectTick(target,amplifier,1);

    }

    public void applyEffectTick(LivingEntity target,int amplifier, int stage){
        Random rand = new Random();
        //find valid spawn points
        Level level=target.level();
        if (Config.transcendentsDimensionTypes.contains(level.dimensionTypeId())){
            BlockPos targetCenter=target.blockPosition();
            ChunkAccess chunkAccess=level.getChunk(targetCenter);
            ChunkPos chunkPos=chunkAccess.getPos();
            ArrayList<BlockPos> applicableBlocks=new ArrayList<>();
            for (int cx=-1;cx<=1;cx++){
                for (int cz=-1;cz<=1;cz++){
                    LevelChunk chunk=level.getChunk(chunkPos.x+cx,chunkPos.z+cz);
                    for (int x=0;x<16;x++){
                        for (int z=0;z<16;z++){
                            //for future changes
                            //instead of the 16 by 16
                            //check each 4 by 4 square in a chunk
                            int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, chunk.getPos().x*16+x, chunk.getPos().z*16+z);//highest block
                            if (Math.abs(y-targetCenter.getY())<=15){
                                BlockPos blockPos=new BlockPos(chunk.getPos().x*16+x,y,chunk.getPos().z*16+z);
                                applicableBlocks.add(blockPos);
                            }
                        }
                    }
                }
            }
            if (applicableBlocks.size()>0){
                ArrayList<AbstractTranscendent[]> groups = new ArrayList<>();
                switch (stage){
                    case 1:{
                        AbstractTranscendent[] group=new AbstractTranscendent[5];

                        group[0]=new Asteron(EntityInit.ASTERON.get(), level);
                        group[1]=new Astralite(EntityInit.ASTRALITE.get(),level);
                        group[2]=new Astralite(EntityInit.ASTRALITE.get(),level);
                        group[3]=new Asteron(EntityInit.ASTERON.get(),level);
                        group[4]=new Astralite(EntityInit.ASTRALITE.get(),level);
                        groups.add(group);
                        break;
                    }
                    case 2:{
                        AbstractTranscendent[] group=new AbstractTranscendent[4];

                        group[0]=new Asteron(EntityInit.ASTERON.get(), level);
                        group[1]=new Asteron(EntityInit.ASTERON.get(),level);
                        group[2]=new Astralite(EntityInit.ASTRALITE.get(),level);
                        group[3]=new Nebure(EntityInit.NEBURE.get(),level);
                        groups.add(group);
                        break;
                    }
                    case 3:{
                        break;
                    }
                    case 4:{
                        break;
                    }
                }
                for (AbstractTranscendent[] mobGroup:groups) {
                    BlockPos chosenPos=applicableBlocks.get(rand.nextInt(applicableBlocks.size()));
                    Vec3 spt=Vec3.atLowerCornerOf(chosenPos);
                    for (int i=0;i< mobGroup.length;i++){
                        mobGroup[i].setPos(spt);
                        level.addFreshEntity(mobGroup[i]);
                        if (!level.isClientSide){
                            TranscendentSpawnEvent event=new TranscendentSpawnEvent(spt,level);
                            MinecraftForge.EVENT_BUS.post(event);
                        }

                    }
                }
            }
        }

    }


}
