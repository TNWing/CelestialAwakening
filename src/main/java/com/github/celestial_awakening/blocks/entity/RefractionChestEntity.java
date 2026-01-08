package com.github.celestial_awakening.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

//import static net.minecraft.world.level.block.BaseEntityBlock.createTickerHelper;

public class RefractionChestEntity extends ChestBlockEntity {
    protected RefractionChestEntity(BlockEntityType<?> p_155327_, BlockPos p_155328_, BlockState p_155329_) {
        super(p_155327_, p_155328_, p_155329_);
    }

    public void serverTick(){

    }
    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
        return p_152134_ == p_152133_ ? (BlockEntityTicker<A>)p_152135_ : null;
    }
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return null;
        //return level.isClientSide ? null : createTickerHelper(blockEntityType, null,RefractionChestEntity::serverTick);
    }
}
