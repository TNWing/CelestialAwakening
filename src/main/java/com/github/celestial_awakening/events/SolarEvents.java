package com.github.celestial_awakening.events;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.github.celestial_awakening.effects.CelestialBeaconMobEffectInstance;
import com.github.celestial_awakening.init.ItemInit;
import com.github.celestial_awakening.init.MobEffectInit;
import com.github.celestial_awakening.networking.ModNetwork;
import com.github.celestial_awakening.networking.packets.LevelCapS2CPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
                            cap.divinerEyeCD--;
                        }
                        else{
                            Random rand = new Random();
                            float randF=rand.nextFloat();
                            if (randF<cap.divinerEyeChance){
                                createDivinerEye(cap,level.dimension());
                                cap.divinerEyeChance=0;
                                //success, perform roll
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
                chance=5+5f*Math.abs(6000f-time)/6000f;
            }
            else if(block instanceof BushBlock){
                chance=1.5f+2f*Math.abs(6000f-time)/6000f;
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
        cap.divinerEyeTimer=rand.nextInt(401)+460;
        System.out.println("DIV EYE HAS INIT TIME " + cap.divinerEyeTimer);
        ModNetwork.sendToClientsInDim(new LevelCapS2CPacket(cap),dimID);
        Object[] params=new Object[]{cap,dimID};
        DelayedFunctionManager.delayedFunctionManager.insertIntoLevelMap(ServerLifecycleHooks.getCurrentServer().getLevel(dimID), new UpdateDivinerEyeCommandPattern(params,0),120,true);//6 seconds before it opens
    }
    /*
    i can use this server side or client side. server side is probably easier, as if client side, the client will need to perform detections AND send messages to server, which server must verify.
     */
    public List<Player> detectPlayers(Level level,LevelCapability cap) throws NoSuchFieldException, IllegalAccessException {
        //also, when the player logs, does not save the diviner?
        ServerChunkCache chunkCache = (ServerChunkCache) level.getChunkSource();
        Field visibleChunkMapField = ChunkMap.class.getDeclaredField("visibleChunkMap");
        visibleChunkMapField.setAccessible(true);
        Map<Long, ChunkHolder> visibleChunkMap = (Map<Long, ChunkHolder>) visibleChunkMapField.get(chunkCache.chunkMap);
        float startingDivPower=cap.divinerEyePower;
        for (ChunkHolder chunkHolder : visibleChunkMap.values()) {
            LevelChunk chunk = chunkHolder.getFullChunk();
            if (chunk==null){
                continue;
            }
            //chunk is null?
            ChunkPos chunkPos=chunk.getPos();
            //int chunkSize = 16;
            AABB chunkBoundingBox = new AABB(chunkPos.getMinBlockX(), 0, chunkPos.getMinBlockZ(), chunkPos.getMaxBlockX() + 1, level.getMaxBuildHeight(), chunkPos.getMaxBlockZ() + 1);
            List<Player> entities = level.getEntitiesOfClass(Player.class, chunkBoundingBox);
            boolean capDirty=false;
            for (Player entity:entities){
                if (entity.hasEffect(MobEffectInit.CELESTIAL_BEACON.get())){
                    continue;
                }

                BlockPos playerBlockPos=entity.blockPosition();
                if(level.canSeeSky(playerBlockPos)){//glass is see-thr so being under glass doesnt protect, i can just leave it like this tho
                    /*
                    TODO: IDEA
                    amplifier is determined by how long the player has stood in the open consecutively?
                   i can do that later
                     */
                    CelestialBeaconMobEffectInstance mobEffectInstance=new CelestialBeaconMobEffectInstance(1200,0,1);
                    entity.addEffect(mobEffectInstance);
                    cap.changeDivPower(Config.divinerScanPower);
                    capDirty=true;
                    if (Config.divinerHeatWaveBlockMod && startingDivPower>=10){//perform heatwave
                        BlockState bushState= Blocks.DEAD_BUSH.defaultBlockState();
                        BlockState magmaState= Blocks.MAGMA_BLOCK.defaultBlockState();
                        BlockState dirtState= Blocks.DIRT.defaultBlockState();
                        entity.setSecondsOnFire(4);
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
                    if (startingDivPower>=25){
                        int pts= (int) (2500*startingDivPower/(startingDivPower+25));
                        if (level.random.nextInt(0,2)==0){
                            cap.divinerSunControlVal = (int) (-startingDivPower/10);
                            cap.divinerSunControlTimer = (pts*35);//every power point adds 20 sec?. alternatively, use a log func or smth
                            System.out.println("LEVEL STATE IS " + cap.divinerSunControlVal);
                        }
                        else{
                            cap.divinerSunControlVal = (int) (-startingDivPower/10);
                            cap.divinerSunControlTimer = (pts*35);
                            System.out.println("LEVEL STATE IS " + cap.divinerSunControlVal);
                        }
                    }
                }
            }
            if (capDirty){
                System.out.println("DIRT CAP, sending details");
                ModNetwork.sendToClientsInDim(new LevelCapS2CPacket(cap),level.dimension());
            }
        }
        return null;
    }
}
