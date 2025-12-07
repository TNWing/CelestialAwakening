package com.github.celestial_awakening.capabilities;

import net.minecraft.nbt.CompoundTag;

public class SearedStoneToolCapability {

    public Byte getUpgradeTier() {
        return upgradeTier;
    }

    public void setUpgradeTier(byte upgradeTier) {
        this.upgradeTier = upgradeTier;
    }

    public Boolean isSmeltActive() {
        return smeltActive;
    }

    public void setSmeltActive(boolean smeltActive) {
        this.smeltActive = smeltActive;
    }

    Byte upgradeTier=0;
    Boolean smeltActive=false;

    void saveNBTData(CompoundTag nbt){
        nbt.putByte("SSTier",upgradeTier);
        nbt.putBoolean("Smelt",smeltActive);
    }
    void loadNBTData(CompoundTag nbt){
        upgradeTier=nbt.getByte("SSTier");
        smeltActive=nbt.getBoolean("Smelt");
    }


}
