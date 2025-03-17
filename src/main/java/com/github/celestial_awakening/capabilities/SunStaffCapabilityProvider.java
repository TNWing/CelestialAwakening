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

public class SunStaffCapabilityProvider  implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability<SunStaffCapability> cap= CapabilityManager.get(new CapabilityToken<>() {});
    private SunStaffCapability data=null;
    private final LazyOptional<SunStaffCapability> optional=LazyOptional.of(this::createCap);

    private SunStaffCapability createCap(){
        if (this.data==null){
            this.data=new SunStaffCapability();
        }
        return this.data;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap==cap){
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt=new CompoundTag();
        createCap().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createCap().loadNBTData(nbt);
    }
}
