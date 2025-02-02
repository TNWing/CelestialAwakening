package com.github.celestial_awakening.capabilities;

import net.minecraft.nbt.CompoundTag;

public class LightRayCapability {
    float damage;
    float width;
    float height;
    float maxWidth;
    float maxHeight;
    float formationProgress;


    public LightRayCapability(){

    }

    void saveNBTData(CompoundTag nbt){
        nbt.putFloat("damage",damage);
        nbt.putFloat("width",width);
        nbt.putFloat("height",height);
        nbt.putFloat("maxWidth",maxWidth);
        nbt.putFloat("maxHeight",maxHeight);
        nbt.putFloat("formationProgress",formationProgress);

    }

    void loadNBTData(CompoundTag nbt){
        damage=nbt.getFloat("damage");
        width=nbt.getFloat("width");
        height=nbt.getFloat("height");
        maxWidth=nbt.getFloat("maxWidth");
        maxHeight=nbt.getFloat("maxHeight");
        formationProgress=nbt.getFloat("formationProgress");
    }
}
