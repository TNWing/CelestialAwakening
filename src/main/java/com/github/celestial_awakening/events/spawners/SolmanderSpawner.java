package com.github.celestial_awakening.events.spawners;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.entity.living.solmanders.AbstractSolmander;
import com.github.celestial_awakening.entity.living.solmanders.SolmanderNewt;
import com.github.celestial_awakening.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LocalMobCapCalculator;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public record SolmanderSpawner() {

    public static void attemptSpawn(ServerLevel serverLevel){
        RandomSource randomSource=serverLevel.getRandom();
        for (ServerPlayer serverPlayer:serverLevel.players()) {
            if (randomSource.nextInt(100)<Config.solmanderChance){
                continue;
            }
            //should generate a list of what to spawn
            ArrayList<AbstractSolmander> solmanders=new ArrayList<>();
            solmanders.add(new SolmanderNewt(EntityInit.SOLMANDER_NEWT.get(), serverLevel));
            solmanders.add(new SolmanderNewt(EntityInit.SOLMANDER_NEWT.get(), serverLevel));
            solmanders.add(new SolmanderNewt(EntityInit.SOLMANDER_NEWT.get(), serverLevel));
            int indexOfLargest=0;
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
                        int y = serverLevel.getHeight(Heightmap.Types.WORLD_SURFACE, chunk.getPos().x*16+x, chunk.getPos().z*16+z);//highest block
                        BlockPos blockPos=new BlockPos(chunk.getPos().x*16+x,y-1,chunk.getPos().z*16+z);
                        FluidState fluidState=serverLevel.getFluidState(blockPos);
                        if (fluidState.is(FluidTags.LAVA)){
                            blockPosList.add(blockPos);
                        }
                    }
                }
            }
            if (!blockPosList.isEmpty()){
                boolean hasSpawned=false;
                AbstractSolmander testSol=solmanders.get(indexOfLargest);
                for (int attempts=0;attempts<8;attempts++){
                    if (hasSpawned){
                        break;
                    }
                    BlockPos pos=blockPosList.get(randomSource.nextInt(blockPosList.size()));
                    testSol.setPos(Vec3.atLowerCornerOf(pos));
                    testSol.refreshDimensions();
                    if (!serverLevel.noCollision(testSol.getType().getAABB(pos.getX(),pos.getY(),pos.getZ()))){
                        //System.out.println("COLLISION DETECTED FOR SOL");
                    }
                    else{
                        boolean bool=net.minecraftforge.event.ForgeEventFactory.checkSpawnPosition(testSol, serverLevel, MobSpawnType.NATURAL);
                        if (bool){
                            hasSpawned=true;

                            for (AbstractSolmander solmander:solmanders) {
                                SpawnGroupData spawngroupdata = null;
                                spawngroupdata= solmander.finalizeSpawn(serverLevel,serverLevel.getCurrentDifficultyAt(pos),MobSpawnType.NATURAL,spawngroupdata,null);
                                solmander.setPos(Vec3.atLowerCornerOf(pos));
                                serverLevel.addFreshEntity(solmander);
                                solmander.setDeltaMovement(
                                        serverLevel.getRandom().nextGaussian()*.1f,
                                        0.05,
                                        serverLevel.getRandom().nextGaussian()*.1f
                                );
                            }
                            serverLevel.getEntitiesOfClass(
                                    AbstractSolmander.class,
                                    new AABB(pos).inflate(2)
                            ).forEach(e->System.out.println(e.getUUID()));
                        }
                    }

                }
            }
            else{
                System.out.println("FAILED TO FIND VALID BLOCKS");
            }

        }

    }
}
