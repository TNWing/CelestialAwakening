package com.github.celestial_awakening.capabilities;

import com.github.celestial_awakening.entity.projectile.CA_Projectile;
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

public class ProjCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static Capability<ProjCapability> ProjCap= CapabilityManager.get(new CapabilityToken<ProjCapability>() {});
    private CA_Projectile projectile;
    private ProjCapability projData=null;
    private int projID;
    private final LazyOptional<ProjCapability> optional=LazyOptional.of(this::createCap);
    public ProjCapabilityProvider(CA_Projectile proj){
        this.projectile=proj;
        this.projID=proj.getId();
    }

    private ProjCapability createCap(){
        if (this.projData==null){
            this.projData=new ProjCapability(projID);
        }
        return this.projData;
    }
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap==ProjCap){
            return optional.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt=new CompoundTag();
        createCap().saveNBTData(nbt,projectile.getMovementModifiers());

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        createCap().loadNBTData(nbt);
    }
}
