package com.github.celestial_awakening.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<PlayerCapability> playerCapability= CapabilityManager.get(new CapabilityToken<>() {
    });
    private PlayerCapability playerData=null;
    private final LazyOptional<PlayerCapability> optional=LazyOptional.of(this::createPlayerCap);
    public PlayerCapabilityProvider(){
    }
    private PlayerCapability createPlayerCap(){
        if (this.playerData==null){
            this.playerData=new PlayerCapability();
        }
        return this.playerData;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap==playerCapability){
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt=new CompoundTag();
        createPlayerCap().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createPlayerCap().loadNBTData(nbt,true);
    }
}
