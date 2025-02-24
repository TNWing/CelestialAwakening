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
import net.minecraft.server.level.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.server.ServerLifecycleHooks;

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
                LevelCapability cap;
                if (Config.divinerShared){
                    cap=level.getServer().overworld().getCapability(LevelCapabilityProvider.LevelCap).orElse(null);
                }
                else{
                    cap= level.getCapability(LevelCapabilityProvider.LevelCap).orElse(null);
                }
                if (cap!=null  && validDim(level, Config.transcendentsDimensionTypes)){
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
            for (Player player:entities){
                if (player.hasEffect(MobEffectInit.CELESTIAL_BEACON.get())){
                    continue;
                }
                BlockPos playerBlockPos=player.blockPosition();
                if(level.canSeeSky(playerBlockPos)){//glass is see-thr so being under glass doesnt protect, i can just leave it like this tho

                    /*
                    TODO: IDEA
                    amplifier is determined by how long the player has stood in the open consecutively?
                   i can do that later
                     */
                    CelestialBeaconMobEffectInstance mobEffectInstance=new CelestialBeaconMobEffectInstance(1200,0,1);
                    player.addEffect(mobEffectInstance);
                }
            }
        }
        return null;
    }
}
