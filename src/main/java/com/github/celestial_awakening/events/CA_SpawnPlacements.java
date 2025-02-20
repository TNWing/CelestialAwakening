package com.github.celestial_awakening.events;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;

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
            boolean darkEnough=true;
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
}
