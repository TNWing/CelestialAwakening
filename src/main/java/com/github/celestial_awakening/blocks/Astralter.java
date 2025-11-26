package com.github.celestial_awakening.blocks;

import com.github.celestial_awakening.blocks.entity.AstralterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class Astralter extends BaseEntityBlock {
    public Astralter(Properties p_49224_) {
        super(p_49224_);
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos,BlockState state) {
        return new AstralterBlockEntity(pos,state);
    }
}
