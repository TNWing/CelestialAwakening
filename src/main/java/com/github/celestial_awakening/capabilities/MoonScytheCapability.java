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
    int lunarOrbs=0;
    int lastOrbTick=0;
    void saveNBTData(CompoundTag nbt){
        nbt.putInt("StrikeCD",strikeCD);
        nbt.putInt("WaveCD",waveCD);
        nbt.putInt("LunarOrbs",lunarOrbs);
    }
    void loadNBTData(CompoundTag nbt){
        strikeCD=nbt.getInt("StrikeCD");
        waveCD=nbt.getInt("WaveCD");
        lunarOrbs=nbt.getInt("LunarOrbs");
    }
    public int getStrikeCD(){
        return strikeCD;
    }
    public int getWaveCD(){
        return waveCD;
    }
    public int getLunarOrbs(){
        return lunarOrbs;
    }
    public void changeWaveCD(int cnt){
        waveCD+=cnt;
    }
    public void changeStrikeCD(int cnt){
        strikeCD+=cnt;
    }
    public void changeLunarOrbs(int cnt,int t){
        lunarOrbs=Math.max(0,Math.min(6,cnt));
        lastOrbTick=t;
    }
    public void incrementLunarOrbs(int t){
        lunarOrbs=Math.min(6,lunarOrbs+1);
        lastOrbTick=t;
    }
    public void decrementLunarOrbs(int t){
        lunarOrbs=Math.max(0,lunarOrbs-1);
        lastOrbTick=t;
    }
    public int getLastOrbTick(){
        return lastOrbTick;
    }
}
