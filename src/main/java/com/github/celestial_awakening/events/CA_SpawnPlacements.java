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
    static SpawnPlacements.SpawnPredicate dark_NightSurface=new SpawnPlacements.SpawnPredicate() {
        ///effect give @e[type=celestial_awakening:night_prowler] minecraft:glowing 1000000 0 true
        ///execute as Dev run tp @s @e[type=celestial_awakening:night_prowler,limit=1]
        @Override
        public boolean test(EntityType entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
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
            boolean b=((blockStateBelow.is(BlockTags.DIRT) || blockStateBelow.is(BlockTags.BASE_STONE_OVERWORLD)));
            for (int y = blockPos.getY(); y <= serverLevelAccessor.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, blockPos).getY(); y++) {
                BlockState aboveBlockState = serverLevelAccessor.getBlockState(new BlockPos(blockPos.getX(), y, blockPos.getZ()));
                if (!aboveBlockState.is(BlockTags.LEAVES) && !aboveBlockState.isAir()) {
                    return false;//if the above block is not a leaf and is not an air block, then it is a solid block that the entity cant spawn on
                }
            }
            long timeMod=serverLevelAccessor.dayTime()%24000L;
            if (
                    darkEnough &&
                    timeMod >14000 && timeMod<23000 &&
                    (blockStateBelow.is(BlockTags.DIRT) || blockStateBelow.is(BlockTags.BASE_STONE_OVERWORLD)) &&
                    blockState.isAir()
            )
            {
                return true;
            }


            return false;
        }
    };

    static  <T extends Mob> SpawnPlacements.SpawnPredicate wip_enabled(SpawnPlacements.SpawnPredicate<T> pred){
        return new SpawnPlacements.SpawnPredicate() {
            @Override
            public boolean test(EntityType p_217081_, ServerLevelAccessor p_217082_, MobSpawnType p_217083_, BlockPos p_217084_, RandomSource p_217085_) {
                if (Config.wipEnabled){
                    return pred.test(p_217081_, p_217082_, p_217083_, p_217084_, p_217085_);
                }
                return Config.wipEnabled;
            }
        };
    }

//SpawnPlacements.Type.IN_LAVA
    static SpawnPlacements.SpawnPredicate lava_daySurface =new SpawnPlacements.SpawnPredicate() {
        //copied from strider
        @Override
        public boolean test(EntityType entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
            if (serverLevelAccessor.getLevel().isDay() && serverLevelAccessor.getLevel().getDayTime()>=Config.solmanderDelay){
                BlockPos.MutableBlockPos blockpos$mutableblockpos = blockPos.mutable();

                do {
                    blockpos$mutableblockpos.move(Direction.UP);
                } while(serverLevelAccessor.getFluidState(blockpos$mutableblockpos).is(FluidTags.LAVA));

                return serverLevelAccessor.getBlockState(blockpos$mutableblockpos).isAir() && serverLevelAccessor.canSeeSky(blockpos$mutableblockpos);
            }
            return false;
        }
    };
    static SpawnPlacements.SpawnPredicate deepLayerSpawn =new SpawnPlacements.SpawnPredicate() {
        //copied from strider
        @Override
        public boolean test(EntityType entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
            LazyOptional<LevelCapability> optional= serverLevelAccessor.getLevel().getCapability(LevelCapabilityProvider.LevelCap);
            AtomicBoolean atomicBoolean=new AtomicBoolean(false);

            if (blockPos.getY()<=serverLevelAccessor.getLevel().getMinBuildHeight()+20){
                optional.ifPresent(cap->{
                    if (cap.deepLayerCounter>=50){
                        atomicBoolean.set(true);
                    }
                });
            }
            return atomicBoolean.get();
        }
    };
}
