package com.github.celestial_awakening.blocks;

import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.function.Supplier;

public class RefractionChest extends ChestBlock {
    /*
    like a normal chest, but should have the following effects/properties
    -increased hardness
    -If holding food or iron ingots while exposed to the sky, will perform their transmutation
        -iron ingots transmutation will occur every 100 ticks between 17000 and 19000
        -food will occur every 600 ticks
    -provides light
    -can hold higher stack count?
     */
    public RefractionChest(Properties p_51490_, Supplier<BlockEntityType<? extends ChestBlockEntity>> p_51491_) {
        super(p_51490_, p_51491_);
    }
}
