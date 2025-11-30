package com.github.celestial_awakening.capabilities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import static com.github.celestial_awakening.nbt_strings.LevelCapNBTNames.*;
import static com.github.celestial_awakening.nbt_strings.LivingEntityNBTNames.*;

public class PlayerCapability {
    short prowlerRaidCounter=0;//incremented periodically, when reach certain value starts raid, resets upon starting
    short insanityPts=32000;

    int insBiomeSoundCD;//stuff like nether ghast or deep dark shrieker
    int insMobSoundCD;//generic mob sounds like enderman & creeper
    int insBlockSoundCD;//stuff like tnt, footsteps, etc
    int insSharedCD;//short cd to prevent multiple sounds from playing
    /*
    Priority
    Biome->Block->Mob
     */
    public int getInsBiomeSoundCD() {
        return insBiomeSoundCD;
    }

    public void setInsBiomeSoundCD(int insBiomeSoundCD) {
        this.insBiomeSoundCD = Math.max(0,insBiomeSoundCD);
    }

    public int getInsMobSoundCD() {
        return insMobSoundCD;
    }

    public void setInsMobSoundCD(int insMobSoundCD) {
        this.insMobSoundCD =Math.max(0, insMobSoundCD);
    }

    public int getInsBlockSoundCD() {
        return insBlockSoundCD;
    }

    public void setInsBlockSoundCD(int insBlockSoundCD) {
        this.insBlockSoundCD = Math.max(0,insBlockSoundCD);
    }


    public void setInsanityValue(short i){
        insanityPts=(short) Math.max(0,Math.min(i, 32000));;
    }
    public short getInsanityPts(){
        return this.insanityPts;
    }
    public void changeInsanityVal(int i){
        insanityPts= (short) Math.max(0,Math.min(insanityPts+i, 32000));
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

    public CompoundTag initNBTData(){
        CompoundTag nbt=new CompoundTag();
        nbt.putShort(leCap_insanity,insanityPts);
        nbt.putShort(pCap_prowlerCnt,prowlerRaidCounter);
        nbt.putInt(pCap_soundBiome,insBiomeSoundCD);
        nbt.putInt(pCap_soundMob,insMobSoundCD);
        nbt.putInt(pCap_soundBlock,insBlockSoundCD);
        return nbt;
    }


    void saveNBTData(CompoundTag nbt){
        nbt.putShort(leCap_insanity,insanityPts);
        nbt.putShort(pCap_prowlerCnt,prowlerRaidCounter);
        nbt.putInt(pCap_soundBiome,insBiomeSoundCD);
        nbt.putInt(pCap_soundMob,insMobSoundCD);
        nbt.putInt(pCap_soundBlock,insBlockSoundCD);
    }

    public void loadNBTData(CompoundTag nbt){
        setInsanityValue(nbt.getShort(leCap_insanity));
        prowlerRaidCounter=nbt.getShort(pCap_prowlerCnt);
        insBiomeSoundCD=nbt.getInt(pCap_soundBiome);
        insMobSoundCD=nbt.getInt(pCap_soundMob);
        insBlockSoundCD=nbt.getInt(pCap_soundBlock);

    }

    public void updateData(PlayerCapability data){
        this.insanityPts=data.getInsanityPts();
        this.prowlerRaidCounter=data.getProwlerRaidCounter();
    }

}
