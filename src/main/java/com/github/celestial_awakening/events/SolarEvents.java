package com.github.celestial_awakening.events;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.github.celestial_awakening.capabilities.LivingEntityCapability;
import com.github.celestial_awakening.capabilities.LivingEntityCapabilityProvider;
import com.github.celestial_awakening.effects.CelestialBeaconMobEffectInstance;
import com.github.celestial_awakening.events.command_patterns.UpdateDivinerEyeCommandPattern;
import com.github.celestial_awakening.events.custom_events.DivinerEyeSoundEvent;
import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.init.MobEffectInit;
import com.github.celestial_awakening.init.SoundInit;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.LevelCapS2CPacket;
import com.github.celestial_awakening.util.ResourceCheckerFuncs;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

import static com.github.celestial_awakening.util.ResourceCheckerFuncs.validDim;

public class SolarEvents {

    public void canCreateDivinerEye(TickEvent.LevelTickEvent event){
        if (event.side.isServer()){
            ServerLevel level= (ServerLevel) event.level;
            long time=level.getDayTime();
            if (time%100==0 && time >Config.transcendentsInitDelay && time%24000L<12000L ){
                @NotNull LazyOptional<LevelCapability> capOptional;
                if (Config.divinerShared){
                    capOptional=level.getServer().overworld().getCapability(LevelCapabilityProvider.LevelCap);
                }
                else{
                    capOptional= level.getCapability(LevelCapabilityProvider.LevelCap);
                }
                capOptional.ifPresent(cap->{
                    if (validDim(level, Config.transcendentsDimensionTypes)){
                        if (cap.divinerEyeCD>0){
                            cap.divinerEyeCD-=100;
                        }
                        else{
                            Random rand = new Random();
                            float randF=rand.nextFloat();
                            if (randF<cap.divinerEyeChance){
                                createDivinerEye(cap,level.dimension());
                                cap.divinerEyeChance=0;
                                //MinecraftForge.EVENT_BUS.post(new MoonScytheAttackEvent(itemStack,isCrit,attacker.level(),dir,targetPos,player,dmg,hAng,cd));
                                MinecraftForge.EVENT_BUS.post(new DivinerEyeSoundEvent(true,level));
                            }
                            else{//increase chance for next attempt
                                cap.divinerEyeChance+=rand.nextInt(1,6)/100f;
                            }

                        }
                    }
                });

            }
        }

    }

    public void dropSunstone(Level level, BlockEvent.BreakEvent event){
        Block block=event.getState().getBlock();
        long time=level.dayTime();
        if (time>0 && time<12000){
            float chance=0;
            Random rand = new Random();
            if (block instanceof LeavesBlock){
                chance=8.5f+5f*Math.abs(6000f-time)/6000f;
            }
            else if(block instanceof BushBlock){
                chance=3f+2f*Math.abs(6000f-time)/6000f;
            }
            float randNum=rand.nextFloat(100f);
            if (randNum<=chance){
                BlockPos blockPos=event.getPos();
                ItemEntity itemEntity =new ItemEntity(level,blockPos.getX(),blockPos.getY(),blockPos.getZ(),new ItemStack(ItemInit.SUNSTONE.get()));
                level.addFreshEntity(itemEntity);
            }
        }

    }

    public void createDivinerEye(LevelCapability cap, ResourceKey<Level> dimID){
        Random rand=new Random();
        cap.divinerEyeCD=rand.nextInt(Config.transcendentsDivMinCD,Config.transcendentsDivMaxCD)+1;
        cap.divinerEyeChance=0;
        cap.divinerEyeFromState=-1;
        cap.divinerEyeToState=-1;
        cap.divinerEyeCurrentChangeDelay =120;
        cap.divinerEyeFrameProgress=0;
        cap.divinerEyeTimer=rand.nextInt(201)+360;//401 + 460
        ModNetwork.sendToClientsInDim(new LevelCapS2CPacket(cap),dimID);
        Object[] params=new Object[]{cap,dimID};
        DelayedFunctionManager.delayedFunctionManager.insertIntoLevelMap(ServerLifecycleHooks.getCurrentServer().getLevel(dimID), new UpdateDivinerEyeCommandPattern(params,0),120,true);//6 seconds before it opens
    }
    /*
    i can use this server side or client side. server side is probably easier, as if client side, the client will need to perform detections AND send messages to server, which server must verify.
     */
    public void detectTargets(Level level, LevelCapability levelCap){
        //also, when the player logs, does not save the diviner?
        ServerChunkCache chunkCache = (ServerChunkCache) level.getChunkSource();
        //	visibleChunkMap f_140130_

        Map<Long, ChunkHolder> visibleChunkMap = ObfuscationReflectionHelper.getPrivateValue(ChunkMap.class,chunkCache.chunkMap ,"f_140130_");
        float startingDivPower=levelCap.divinerEyePower;
        for (ChunkHolder chunkHolder : visibleChunkMap.values()) {
            LevelChunk chunk = chunkHolder.getFullChunk();
            if (chunk==null){
                continue;
            }
            ChunkPos chunkPos=chunk.getPos();
            AABB chunkBoundingBox = new AABB(chunkPos.getMinBlockX(),  level.getMinBuildHeight(), chunkPos.getMinBlockZ(), chunkPos.getMaxBlockX() + 1, level.getMaxBuildHeight(), chunkPos.getMaxBlockZ() + 1);
            Predicate<LivingEntity> pred=o ->ResourceCheckerFuncs.validEntityType(o,Config.transcendentsTargets);
            List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, chunkBoundingBox,pred);
            boolean capDirty=false;
            for (LivingEntity entity:entities){
                if (entity.hasEffect(MobEffectInit.CELESTIAL_BEACON.get())){
                    continue;
                }

                capDirty=true;
                BlockPos entityBlockPos=entity.blockPosition();
                if(level.canSeeSky(entityBlockPos)){//glass is see-thr so being under glass doesnt protect, i can just leave it like this tho
                    //m,aybe have amplifier is determined by how long the player has stood in the open consecutively?
                    LazyOptional<LivingEntityCapability> optional=entity.getCapability(LivingEntityCapabilityProvider.capability);
                    //TODO: test this
                    optional.ifPresent(cap->{
                        int amp=0;
                        if (startingDivPower>35){
                            amp=1;
                        }
                        cap.increaseNaviGauge((short) 2);
                        //System.out.println("navigauge entity " + entity + "  " + cap.getNavigauge() );
                        if (cap.getNavigauge()>60){//so takes 1.5 sec of being exposed to trigger
                            //System.out.println("beacon   entity " + entity );
                            CelestialBeaconMobEffectInstance mobEffectInstance=new CelestialBeaconMobEffectInstance(1200,amp,1);

                            entity.addEffect(mobEffectInstance);
                            levelCap.changeDivPower(Config.divinerScanPower);

                            if (Config.divinerHeatwaveEnabled && startingDivPower>=10){
                                entity.setSecondsOnFire(4);
                                if(Config.divinerHeatWaveBlockMod){//perform heatwave
                                    BlockState bushState= Blocks.DEAD_BUSH.defaultBlockState();
                                    BlockState magmaState= Blocks.MAGMA_BLOCK.defaultBlockState();
                                    BlockState dirtState= Blocks.DIRT.defaultBlockState();

                                    BlockPos centerBlockPos=entity.blockPosition();
                                    int vOffset=9;
                                    int hOffset=6;
                                    for (int y=centerBlockPos.getY()-vOffset;y<centerBlockPos.getY()+vOffset;y++){
                                        for (int x=centerBlockPos.getX()-hOffset;x<centerBlockPos.getX()+hOffset;x++){
                                            for (int z=centerBlockPos.getZ()-hOffset;z<centerBlockPos.getZ()+hOffset;z++){
                                                BlockPos blockPos=new BlockPos(x,y,z);
                                                BlockState blockState=level.getBlockState(blockPos);
                                                List<TagKey<Block>> tags=blockState.getTags().toList();
                                                if (tags.contains(BlockTags.LEAVES)){
                                                    level.destroyBlock(blockPos,true);
                                                }
                                                else if (tags.contains(BlockTags.CROPS)){
                                                    level.setBlockAndUpdate(blockPos,bushState);
                                                }
                                                else if (blockState.getBlock() instanceof FarmBlock || tags.contains(BlockTags.DIRT)){
                                                    level.setBlockAndUpdate(blockPos,dirtState);
                                                }
                                                else if (tags.contains(Tags.Blocks.COBBLESTONE) || tags.contains(Tags.Blocks.STONE)){
                                                    level.setBlockAndUpdate(blockPos,magmaState);
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            if (startingDivPower>=25){
                                if(Config.divinerAoDEnabled) {
                                    int pts = (int) (2500 * startingDivPower / (startingDivPower + 25));
                                    if (level.random.nextInt(0, 2) == 0) {
                                        levelCap.setSunControlVal((byte) (-startingDivPower / 8));
                                        levelCap.divinerSunControlTimer = (pts * 35);
                                    } else {
                                        levelCap.setSunControlVal((byte) (-startingDivPower / 8));
                                        levelCap.divinerSunControlTimer = (pts * 35);
                                    }
                                }
                                //System.out.println("OUr level has sunctrl " + levelCap.divinerSunControlVal + "  and timer " + levelCap.divinerSunControlTimer);
                            }
                            else{
                                levelCap.divinerSunControlTimer=10;
                            }
                        }
                    });

                }
            }
            if (capDirty){
                ModNetwork.sendToClientsInDim(new LevelCapS2CPacket(levelCap),level.dimension());
            }
        }
    }

}
