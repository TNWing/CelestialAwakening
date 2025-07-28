package com.github.celestial_awakening.blocks.entity;

import com.github.celestial_awakening.init.BlockInit;
import com.github.celestial_awakening.menus.AstralterMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AstralterBlockEntity extends BlockEntity implements MenuProvider, Nameable {
    private Component name;
    private static final Component DEFAULT_NAME = Component.translatable("container.astralter");
    public AstralterBlockEntity(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }
    public AstralterBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(BlockInit.ASTRALTER_ENTITY.get(), blockPos, blockState);
    }
    public void load(CompoundTag tag) {
        super.load(tag);
    }

    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("SunValue", 0);
        tag.putInt("MoonValue", 0);
    }

    private final ContainerData dataAccess = new ContainerData() {

        @Override
        public int get(int p_39284_) {
            switch(p_39284_){
                case 0:{
                    break;
                }
                default:{
                    break;
                }
            }
            return 0;
        }

        @Override
        public void set(int p_39285_, int p_39286_) {
            switch(p_39285_){
                case 0:{
                    break;
                }
                default:{
                    break;
                }
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };
    @Override
    public Component getName() {
        return this.name != null ? this.name : DEFAULT_NAME;
    }

    @Override
    public Component getDisplayName() {
        return this.getName();
    }
    public void setLevel(Level p_155091_) {
        super.setLevel(p_155091_);
    }


    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> cap, Direction side) {
        return super.getCapability(cap, side);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new AstralterMenu(p_39954_,p_39955_,this);
    }
}
