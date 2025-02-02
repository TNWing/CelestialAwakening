package com.github.celestial_awakening.events;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class CA_SpawnPlacements {
    static SpawnPlacements.SpawnPredicate dark_NightSurface=new SpawnPlacements.SpawnPredicate() {
        @Override
        public boolean test(EntityType entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource source) {
            BlockState blockStateBelow=serverLevelAccessor.getBlockState(blockPos.below());
            if (serverLevelAccessor.getRawBrightness(blockPos,0)<5 &&
                    serverLevelAccessor.dayTime() % 24000L>12000 &&
                    serverLevelAccessor.getHeightmapPos(Heightmap.Types.WORLD_SURFACE,blockPos).equals(blockPos)){
                return true;
            }
            return false;
        }
    };
}
