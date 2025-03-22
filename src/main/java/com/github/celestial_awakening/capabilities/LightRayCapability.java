package com.github.celestial_awakening.capabilities;

import net.minecraft.nbt.CompoundTag;

public class LightRayCapability {//NOT CURRENTLY USED, MAY DELETE LATER
    float maxWidth;
    float maxHeight;
    float formationProgress;


    public LightRayCapability(){

    }

    void saveNBTData(CompoundTag nbt){
        nbt.putFloat("maxWidth",maxWidth);
        nbt.putFloat("maxHeight",maxHeight);
        nbt.putFloat("formationProgress",formationProgress);

    }

    void loadNBTData(CompoundTag nbt){
        maxWidth=nbt.getFloat("maxWidth");
        maxHeight=nbt.getFloat("maxHeight");
        formationProgress=nbt.getFloat("formationProgress");
    }
}
