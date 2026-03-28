package com.github.celestial_awakening.events.spawners;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.entity.living.solmanders.AbstractSolmander;
import com.github.celestial_awakening.entity.living.solmanders.SolmanderNewt;
import com.github.celestial_awakening.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
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
                int xOffset=randomSource.nextInt(-2,2);
                int zOffset=randomSource.nextInt(-2,2);
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
            /*
            Alternative method:
            have the array of valid chunks
            for each chunk, check for conditional
            then check for player distance

            the way lycanites does it is that it takes each player's position, and adds it to a list if that position is not close to any other positions currently in the list.
            then, it performs an iteration through a large area, centered on each position in the list
             */

            /*
            How vanilla handles it

           ServerChunkCache.tickChunks() runs
           for each chunk in the list, calls NaturalSpawner.spawnForChunk
           in spawnForChunk, calls  NaturalSpawner.spawnCategoryForChunk
            in that method, first generates a random blockPos
            calls spawnCategoryForPosition, which does this:
                performs 3 loops of the following{
                    loops a random amt of times (max of 4):[
                        generates a blockpos that is randomly offset from the original blockpos
                        gets nearest player
                        checks to see if nearestplayer is not too far and not too close
                        if so, performs conditional checks (such as valid position, spawnerdata exists, etc)
                    ]
                }

            or a more comprehensive method

            Given a starting position:
            Repeat up to 3 times (spawn groups):

                Reset position to origin

                Pick random group size (1–4)

                For each attempt in group:

                    Random walk position (not independent offsets)
                        offsets based on previously calculated position
                        so if pos1 is the initial position, we get positions pos1+offset1, pos1+offset1+offset2, pos1+offset1+offset2+offset3, etc

                    Find nearest player

                    If player distance is valid:

                        If mob type not chosen yet:
                            pick mob type for this group
                            if none → stop this group

                        If position + rules valid:

                            spawn mob

                            if global limit reached → STOP ENTIRE METHOD

                            if group limit reached → STOP THIS GROUP

             */

            /*
            so how should i go about doing this (for performance considerations)
            spawn in any chunk provided it has lava? worst case scenario its still not that efficient but it does work to a degree
            base player checks on center of chunk pos?
            so first gather chunks and validate there are no enarby players in the chunk, then add it to list
            then check chhunk conditional, finally spawn entity in it?

            or alternatively, do a random walk on spawning as well
            or random walk on validating chunk possibility
            basically, start at center of chunk, random walk around to find valid positions, do this a couple of times
            alternatively, divide chunk into smaller chunks, sample a random block per small chunk
            basically, div into 8 by 8 (total of 4 small chunks)
            then sample 1 or 2 random blocks
            or even divide into 4 by 4s?

            for 4 small chunks
            -sample a random pos from each
                -then,determine if any nearby players from that pos
                    if not, run the conditional check, then add it to list
                    then take random pos and spawn there
                    this would also simulate lava pools (if there is a sufficiently large lava pool, then the random check should hit it?)
             */
            for (LevelChunk chunk:chunks) {
                for (int baseX=0;baseX<16;baseX+=8){
                    for (int baseZ=0;baseZ<16;baseZ+=8){
                        int x=chunk.getPos().x*16+baseX+serverLevel.getRandom().nextInt(8);
                        int z=chunk.getPos().z*16+baseZ+serverLevel.getRandom().nextInt(8);
                        int y=serverLevel.getHeight(Heightmap.Types.WORLD_SURFACE,x,z);
                        BlockPos blockPos=new BlockPos(x,y,z);
                        if (serverLevel.hasNearbyAlivePlayer(blockPos.getX(), blockPos.getY(),blockPos.getZ(), 24D)){
                            FluidState fluidState=serverLevel.getFluidState(blockPos);
                            if (fluidState.is(FluidTags.LAVA)){
                                blockPosList.add(blockPos);
                            }

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
