package com.github.celestial_awakening.capabilities;

import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public class LivingEntityCapability {

    UUID uuid;
    int navigauge;//used for diviner
    public void increaseNaviGauge(int i){
        navigauge+=i;
    }
    public void setUUID(UUID id){
        this.uuid =id;
    }


    void saveNBTData(CompoundTag nbt){
        nbt.putInt("NaviGauge",navigauge);
    }
    public void loadNBTData(CompoundTag nbt){
        navigauge=nbt.getInt("NaviGauge");
    }
}
