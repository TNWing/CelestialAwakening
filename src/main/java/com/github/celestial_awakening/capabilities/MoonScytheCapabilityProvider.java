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

public class MoonScytheCapabilityProvider  implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    /*
    public static Capability<ShotLauncherData> LauncherData= CapabilityManager.get(new CapabilityToken<ShotLauncherData>() {});
private ShotLauncherData launcherData=null;
private final LazyOptional<ShotLauncherData> optional=LazyOptional.of(this::createLauncherData);
 */

    public static Capability<MoonScytheCapability> ScytheCap= CapabilityManager.get(new CapabilityToken<MoonScytheCapability>() {});
    private MoonScytheCapability scytheData=null;
    private final LazyOptional<MoonScytheCapability> optional=LazyOptional.of(this::createScytheCap);

    private MoonScytheCapability createScytheCap(){
        if (this.scytheData==null){
            this.scytheData=new MoonScytheCapability();
        }
        return this.scytheData;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap==ScytheCap){
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt=new CompoundTag();
        createScytheCap().saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createScytheCap().loadNBTData(nbt);
    }
}
