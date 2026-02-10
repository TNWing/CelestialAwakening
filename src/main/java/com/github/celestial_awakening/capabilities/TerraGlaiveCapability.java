package com.github.celestial_awakening.capabilities;

import net.minecraft.nbt.CompoundTag;

public class TerraGlaiveCapability {
    public short getSpearCD() {
        return spearCD;
    }

    public void setSpearCD(short spearCD) {
        this.spearCD = spearCD;
    }

    short spearCD;
    void saveNBTData(CompoundTag nbt){
        nbt.putShort("SpearCD",spearCD);
    }
    void loadNBTData(CompoundTag nbt){
        spearCD=nbt.getShort("SpearCD");
    }

    public void decrementCD(){
        spearCD= (short) Math.max(0,spearCD-1);
    }

}
