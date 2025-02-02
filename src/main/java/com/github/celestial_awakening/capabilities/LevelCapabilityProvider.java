package com.github.celestial_awakening.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LevelCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {



    private Level level;

    public static Capability <LevelCapability> LevelCap= CapabilityManager.get(new CapabilityToken<LevelCapability>() {});
    private LevelCapability levelData=null;
    private final LazyOptional<LevelCapability> optional=LazyOptional.of(this::createLevelData);

    public LevelCapabilityProvider(Level lvl){
        this.level=lvl;
    }

    private LevelCapability createLevelData() {
        if (this.levelData==null){
            this.levelData=new LevelCapability(level);
        }
        return this.levelData;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap==LevelCap){
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt=new CompoundTag();
        createLevelData().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createLevelData().loadNBTData(nbt,true);
    }
}
