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

public class SearedStoneToolCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    /*
    public static Capability<ShotLauncherData> LauncherData= CapabilityManager.get(new CapabilityToken<ShotLauncherData>() {});
private ShotLauncherData launcherData=null;
private final LazyOptional<ShotLauncherData> optional=LazyOptional.of(this::createLauncherData);
 */

    public static Capability<SearedStoneToolCapability> capability= CapabilityManager.get(new CapabilityToken<>() {
    });
    private SearedStoneToolCapability data=null;
    private final LazyOptional<SearedStoneToolCapability> optional=LazyOptional.of(this::createCap);

    private SearedStoneToolCapability createCap(){
        if (this.data==null){
            this.data=new SearedStoneToolCapability();
        }
        return this.data;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap==capability){
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
