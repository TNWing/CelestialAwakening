package com.github.celestial_awakening.events.spawners;

import com.github.celestial_awakening.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LocalMobCapCalculator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;

import java.util.ArrayList;

public class UndergroundGuardianSpawner {
    /*
    Members
    -Molten
    -Core
     */
    public static void attemptSpawn(ServerLevel serverLevel){
        RandomSource randomSource=serverLevel.getRandom();
        for (ServerPlayer serverPlayer:serverLevel.players()) {
            if (randomSource.nextInt(100) < Config.undergroundGuardianChance) {
                continue;
            }
            int playerY=serverPlayer.getBlockY();
            ArrayList<LevelChunk> chunks=new ArrayList<>();
            ChunkPos chunkPos=serverPlayer.chunkPosition();
            for (int i=0;i<randomSource.nextInt(2)+3;i++){
                int xOffset=randomSource.nextInt(-1,1);
                int zOffset=randomSource.nextInt(-1,1);
                if (xOffset==0 && zOffset==0){
                    continue;
                }
                LevelChunk chunk=serverLevel.getChunk(chunkPos.x+xOffset,chunkPos.z+zOffset);

                LocalMobCapCalculator calculator=new LocalMobCapCalculator(serverLevel.getChunkSource().chunkMap);
                if (!chunks.contains(chunk) && calculator.canSpawn(MobCategory.MONSTER,chunkPos)){
                    chunks.add(chunk);
                }
            }
            ArrayList<BlockPos> blockPosList=new ArrayList<>();
            for (LevelChunk chunk:chunks) {
                for (int x=0;x<16;x++) {
                    for (int z = 0; z < 16; z++) {
                        int y=playerY+randomSource.nextInt(-8,8);
                        if (y<0){
                            BlockPos blockPos=new BlockPos(chunk.getPos().x*16+x,y,chunk.getPos().z*16+z);
                            FluidState fluidState=serverLevel.getFluidState(blockPos);
                            if (fluidState.is(FluidTags.LAVA)){
                                blockPosList.add(blockPos);
                            }
                        }

                    }
                }
            }

        }
    }
}
