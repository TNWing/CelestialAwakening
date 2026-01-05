package com.github.celestial_awakening.events;

import com.github.celestial_awakening.Config;
import com.github.celestial_awakening.capabilities.LevelCapability;
import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.util.LazyOptional;

import java.util.concurrent.atomic.AtomicBoolean;

public class CA_SpawnPlacements {
    //maybe move the prowler/solmander spawning to custom spawners instead of vanilla handlers
    ///effect give @e[type=celestial_awakening:night_prowler] minecraft:glowing 1000000 0 true
///execute as Dev run tp @s @e[type=celestial_awakening:night_prowler,limit=1]
    static SpawnPlacements.SpawnPredicate dark_NightSurface= (entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) -> {
        BlockState blockStateBelow=serverLevelAccessor.getBlockState(blockPos.below());
        BlockState blockState=serverLevelAccessor.getBlockState(blockPos);
        DimensionType dimensiontype =serverLevelAccessor.dimensionType();
        int i = dimensiontype.monsterSpawnBlockLightLimit();
        boolean darkEnough;
        if (i < 15 && serverLevelAccessor.getBrightness(LightLayer.BLOCK, blockPos) > i) {
            return false;
        } else {
            int j = serverLevelAccessor.getLevel().isThundering() ? serverLevelAccessor.getMaxLocalRawBrightness(blockPos, 10) : serverLevelAccessor.getMaxLocalRawBrightness(blockPos);
            darkEnough= j <= dimensiontype.monsterSpawnLightTest().sample(randomSource);
        }
        for (int y = blockPos.getY(); y <= serverLevelAccessor.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, blockPos).getY(); y++) {
            BlockState aboveBlockState = serverLevelAccessor.getBlockState(new BlockPos(blockPos.getX(), y, blockPos.getZ()));
            if (!aboveBlockState.is(BlockTags.LEAVES) && !aboveBlockState.isAir()) {
                return false;//if the above block is not a leaf and is not an air block, then it is a solid block that the entity cant spawn on
            }
        }
        long timeMod=serverLevelAccessor.dayTime()%24000L;
        return (darkEnough &&
                timeMod >14000 && timeMod<23000 &&
                (blockStateBelow.is(BlockTags.DIRT) || blockStateBelow.is(BlockTags.BASE_STONE_OVERWORLD)) &&
                blockState.isAir()
        );
    };

    static  <T extends Mob> SpawnPlacements.SpawnPredicate wip_enabled(SpawnPlacements.SpawnPredicate<T> pred){
        return (p_217081_, p_217082_, p_217083_, p_217084_, p_217085_) -> {
            try{
                System.out.println("WIP ENAB  " + Config.wipEnabled);
                if (Config.wipEnabled){
                    System.out.println();
                    return pred.test(p_217081_, p_217082_, p_217083_, p_217084_, p_217085_);
                }
                return Config.wipEnabled;
            }
            catch(Exception e){
                System.out.println("ERR IN WIP ");
                e.printStackTrace();
                return false;
            }

        };
    }

//copied from strider
    //SpawnPlacements.Type.IN_LAVA
    static SpawnPlacements.SpawnPredicate lava_daySurface = (entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) -> {
        if (serverLevelAccessor.getLevel().isDay() && serverLevelAccessor.getLevel().getDayTime()>=Config.solmanderDelay){
            BlockPos.MutableBlockPos blockpos$mutableblockpos = blockPos.mutable();
            System.out.println("sol check pos " + blockpos$mutableblockpos);
            int max=serverLevelAccessor.getMaxBuildHeight()-blockPos.getY();
            for (int i=1;i<max;i++){
                if (!serverLevelAccessor.getFluidState(blockpos$mutableblockpos).is(FluidTags.LAVA)){
                    break;
                }
                blockpos$mutableblockpos.move(Direction.UP);
            }
            return serverLevelAccessor.canSeeSky(blockpos$mutableblockpos);
            }
        return false;
    //do {
    //                blockpos$mutableblockpos.move(Direction.UP);
    //            } while(serverLevelAccessor.getFluidState(blockpos$mutableblockpos).is(FluidTags.LAVA));
    //            System.out.println("VALID? " + (serverLevelAccessor.getBlockState(blockpos$mutableblockpos).isAir() && serverLevelAccessor.canSeeSky(blockpos$mutableblockpos)));
    //            return serverLevelAccessor.getBlockState(blockpos$mutableblockpos).isAir() && serverLevelAccessor.canSeeSky(blockpos$mutableblockpos);
    //

    };
    //copied from strider
    static SpawnPlacements.SpawnPredicate deepLayerSpawn = (entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource) -> {
        LazyOptional<LevelCapability> optional= serverLevelAccessor.getLevel().getCapability(LevelCapabilityProvider.LevelCap);
        System.out.println("Checking  deeplayerspawn at " + blockPos);
        boolean result=false;
        if (optional.isPresent()){
            if (blockPos.getY()<=serverLevelAccessor.getLevel().getMinBuildHeight()+20){
                if (optional.orElseGet(null).deepLayerCounter>=100){
                    result=true;
                }
            }
        }
        /*
        AtomicBoolean atomicBoolean=new AtomicBoolean(false);
        if (blockPos.getY()<=serverLevelAccessor.getLevel().getMinBuildHeight()+20){
            optional.ifPresent(cap->{
                if (cap.deepLayerCounter>=100){
                    atomicBoolean.set(true);
                }
            });
        }

         */
        return result;
    };
}
