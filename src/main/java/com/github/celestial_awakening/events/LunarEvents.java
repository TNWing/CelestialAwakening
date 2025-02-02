package com.github.celestial_awakening.events;
import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.util.MathFuncs;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.github.celestial_awakening.util.MathFuncs.angBtwnVec;
import static com.github.celestial_awakening.util.ResourceCheckerFuncs.validDim;


public class LunarEvents {

    public boolean attemptPKSpawn(Level level){
        int time=(int)(level.getDayTime() % 24000L);//ranges from 0-24k
        if (MathFuncs.isInRange(time,0,0)){
            switch (level.getMoonPhase()) {
                case 3://half
                case 7:
                {
                    break;
                }
                case 2://gibb
                case 8:
                {
                    break;
                }
                case 4://crescent
                case 6:{
                    if (level.getDayTime()%24000L>Config.pkCrescenciaMinDay){
                        //perform rng roll

                    }
                    break;
                }
                case 5:{//new moon
                    break;
                }
            }
        }

        return false;
    }
    public void detectIfLookingAtCelestialBody(Level level,int isSun){
        int time=(int)level.dayTime();//ranges from 0-24k
        float sunAngle = level.getSunAngle(time) + isSun * (float) Math.PI / 2; //-pi/2 for the moon, pi/2 for sun

        Vec3 sun = new Vec3(Math.cos(sunAngle), Math.sin(sunAngle), 0f);
        for (Player player:level.players()){
            Vec3 view = player.getViewVector(1.0f);
            if (angBtwnVec(view,sun)<7D){
                System.out.println("LOOKING AT CELESTIAL BODY  " + isSun);
            }
        }
    }



    public void moonstoneMark(Level level){
        Random rand = new Random();
        LevelCapability cap=level.getCapability(LevelCapabilityProvider.LevelCap).orElse(null);

        if (cap!=null && !cap.currentMoonstonePos.isEmpty()){
            for (BlockPos blockPos:cap.currentMoonstonePos.keySet()){
                AABB bound=new AABB(blockPos);
                TargetingConditions conds=TargetingConditions.forNonCombat();
                Monster monster =level.getNearestEntity(Monster.class,conds,null,blockPos.getX(),blockPos.getY(),blockPos.getZ(), bound);

            }
        }
    }

    public void revealMoonstone(Level level){
        Random rand = new Random();
        LevelCapability cap=level.getCapability(LevelCapabilityProvider.LevelCap).orElse(null);

        if (cap!=null && validDim(level, Config.lunarMatDimensionTypes) && level.isNight()){

            int time=(int)(level.getDayTime() % 24000L);//ranges from 0-24k
            //time% % 24000L will give the actual daytime
            /*
            night is 12000-24000
            spawn at 15000,18000,21000
             */
            if ((time%12000)%3000==0 && time!=24000){//valid time
                //for each player, attempt to create a moonstone in a nearby chunk
                List<? extends Player> pList=level.players();
                for (Player p:pList) {
                    if (cap.currentMoonstonePos.keySet().size()>15){
                        break;
                    }
                    ChunkPos chunkPos=p.chunkPosition();
                    ArrayList<BlockPos> applicableBlocks=new ArrayList<BlockPos>();
                    for (int cx=-1;cx<=1;cx++){
                        for (int cz=-1;cz<=1;cz++){
                            if (cx!=0 && cz!=0){
                                LevelChunk chunk=level.getChunk(chunkPos.x+cx,chunkPos.z+cz);
                                for (int x=0;x<16;x++){
                                    for (int z=0;z<16;z++){
                                        int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, chunk.getPos().x*16+x, chunk.getPos().z*16+z);//highest block
                                        BlockPos blockPos=new BlockPos(chunk.getPos().x*16+x,y-1,chunk.getPos().z*16+z);

                                        //check to see if block is exposed to surface and is one of X types
                                        BlockState blockState=level.getBlockState(blockPos);
                                        if (!cap.currentMoonstonePos.containsKey(blockPos)){
                                            if ((blockState.is(BlockTags.DIRT) || blockState.is(BlockTags.BASE_STONE_OVERWORLD) || blockState.is(BlockTags.SAND))){
                                                applicableBlocks.add(blockPos);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    //pick a random applicable block
                    if (applicableBlocks.size()>0){
                        System.out.println("PLACING MOONSTONE");
                        BlockPos chosenSpot=applicableBlocks.get(rand.nextInt(applicableBlocks.size()));//err, bound must be pos
                        cap.currentMoonstonePos.put(chosenSpot,4800);
                    }

                }
            }

        }
        //level.addParticle();
        ServerLevel serverLevel= (ServerLevel) level;
        //serverLevel.sendParticles()
    }

    public void midnightIronTransformation(Level level){
        if (!level.isClientSide()&& validDim(level,Config.lunarMatDimensionTypes)){
            int time=(int)(level.getDayTime() % 24000L);//ranges from 0-24k
            if (time%18000==0){
                WorldBorder border=level.getWorldBorder();
                AABB worldBox = new AABB(
                        border.getMinX(), level.getMinBuildHeight(),border.getMinZ(),
                        border.getMaxX(), level.getMaxBuildHeight(), border.getMaxZ());
                for (ItemEntity itemEntity : level.getEntitiesOfClass(ItemEntity.class,worldBox)) {
                    if (itemEntity.getItem().getItem() == Items.IRON_INGOT) {
                        itemEntity.setItem(new ItemStack(ItemInit.MIDNIGHT_IRON_INGOT.get(), itemEntity.getItem().getCount()));
                    }
                }
            }

        }
    }

}
