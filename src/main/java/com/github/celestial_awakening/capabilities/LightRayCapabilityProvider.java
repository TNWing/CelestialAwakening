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

public class LightRayCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    public static Capability <LightRayCapability> cap= CapabilityManager.get(new CapabilityToken<LightRayCapability>() {});
    private LightRayCapability data=null;
    private final LazyOptional<LightRayCapability> optional=LazyOptional.of(this::createCapability);

    public LightRayCapability createCapability(){
        if (this.data==null){
            this.data=new LightRayCapability();
        }
        return this.data;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap==this.cap){
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt=new CompoundTag();
        createCapability().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createCapability().loadNBTData(nbt);
    }
}
