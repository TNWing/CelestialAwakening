package com.github.celestial_awakening.capabilities;

import net.minecraft.nbt.CompoundTag;

public class MoonScytheCapability {
    /*
Crit hits create a crescent strike
Non-crit hits create a crescent wave
Each ability has a cool down of 5 seconds.
     */
    int strikeCD=0;
    int waveCD=0;
    void saveNBTData(CompoundTag nbt){
        nbt.putInt("StrikeCD",strikeCD);
        nbt.putInt("WaveCD",waveCD);
    }
    void loadNBTData(CompoundTag nbt){
        strikeCD=nbt.getInt("StrikeCD");
        waveCD=nbt.getInt("WaveCD");
    }
    public int getStrikeCD(){
        return strikeCD;
    }
    public int getWaveCD(){
        return waveCD;
    }
    public void changeWaveCD(int cnt){
        waveCD+=cnt;
    }
    public void changeStrikeCD(int cnt){
        strikeCD+=cnt;
    }
}
