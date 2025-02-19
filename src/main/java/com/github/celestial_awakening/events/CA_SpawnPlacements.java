package com.github.celestial_awakening.events;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

public class CA_SpawnPlacements {
    static SpawnPlacements.SpawnPredicate dark_NightSurface=new SpawnPlacements.SpawnPredicate() {
        ///effect give @e[type=celestial_awakening:night_prowler] minecraft:glowing 1000000 0 true
        ///execute as Dev run tp @s @e[type=celestial_awakening:night_prowler,limit=1]
        @Override
        public boolean test(EntityType entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource source) {

            BlockState blockStateBelow=serverLevelAccessor.getBlockState(blockPos.below());
            BlockState blockState=serverLevelAccessor.getBlockState(blockPos);
            System.out.println("SKY "  + serverLevelAccessor.canSeeSky(blockPos));
            if (
                    serverLevelAccessor.getRawBrightness(blockPos,0)<5 &&
                    serverLevelAccessor.dayTime() % 24000L>12000 &&
                    serverLevelAccessor.getHeightmapPos(Heightmap.Types.WORLD_SURFACE,blockPos).equals(blockPos) &&
                    //serverLevelAccessor.canSeeSky(blockPos) &&
                    (blockStateBelow.is(BlockTags.DIRT) || blockStateBelow.is(BlockTags.BASE_STONE_OVERWORLD)) &&
                    blockState.isAir()
            ){
                return true;
            }


            return false;
        }
    };
}
