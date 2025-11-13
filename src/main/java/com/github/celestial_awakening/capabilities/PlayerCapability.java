package com.github.celestial_awakening.capabilities;

import net.minecraft.nbt.CompoundTag;

import static com.github.celestial_awakening.nbt_strings.LivingEntityNBTNames.leCap_insanity;
import static com.github.celestial_awakening.nbt_strings.LivingEntityNBTNames.pCap_prowlerCnt;

public class PlayerCapability {
    short prowlerRaidCounter;//incremented periodically, when reach certain value starts raid, resets upon starting
    short insanityPts;
    public void setInsanityValue(short i){
        insanityPts=i;
    }
    public short getInsanityPts(){
        return this.insanityPts;
    }
    public void changeInsanityVal(int i){
        insanityPts= (short) Math.min(insanityPts+i, 32000);
    }
    public void incrementProwlerRaidCounter(){
        this.prowlerRaidCounter++;
    }
    public short incrementAndGetProwlerRaidCounter(){
        return ++this.prowlerRaidCounter;
    }
    public short getProwlerRaidCounter() {
        return prowlerRaidCounter;
    }

    public void setProwlerRaidCounter(short prowlerRaidCounter) {
        this.prowlerRaidCounter = prowlerRaidCounter;
    }


    void saveNBTData(CompoundTag nbt){
        nbt.putShort(leCap_insanity,insanityPts);
        nbt.putShort(pCap_prowlerCnt,prowlerRaidCounter);
    }

    public void loadNBTData(CompoundTag nbt){
        insanityPts=nbt.getShort(leCap_insanity);
        prowlerRaidCounter=nbt.getShort(pCap_prowlerCnt);
    }
}
