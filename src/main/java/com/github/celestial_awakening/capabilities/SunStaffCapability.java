package com.github.celestial_awakening.capabilities;

import net.minecraft.nbt.CompoundTag;

public class SunStaffCapability {
    /*
Abilities
Searing Flash
Left Click
Deal a burst of fire damage to all entities in a 5 block radius
CD of 12 seconds
Shining Ray
Right Click
Cast a ray of energy that explodes on impact, dealing damage and healing allies.
CD of 6 seconds
Solar Wind
Hold Shift + Hold Right Click for at least 1 second
While holding shift and right click, create an expanding zone that deals fire damage to all entities in it.
The zone can last for up to 10 seconds, and the area can have a max radius of 7 blocks.
CD of 4-10 seconds, depending on how long the storm lasted.
 */
    int flashCD=0;
    int rayCD=0;
    int windCD=0;
    void saveNBTData(CompoundTag nbt){
        nbt.putInt("FlashCD",flashCD);
        nbt.putInt("RayCD",rayCD);
        nbt.putInt("WindCD",windCD);
    }
    void loadNBTData(CompoundTag nbt){
        flashCD=nbt.getInt("FlashCD");
        rayCD=nbt.getInt("RayCD");
        windCD=nbt.getInt("WindCD");
    }

    public void decrementCD(){
        flashCD=Math.max(0,flashCD-1);
        rayCD=Math.max(0,rayCD-1);
        windCD=Math.max(0,windCD-1);
    }

    public int getFlashCD(){
        return flashCD;
    }

    public int getRayCD() {
        return rayCD;
    }

    public int getWindCD() {
        return windCD;
    }

    public void setFlashCD(int flashCD) {
        this.flashCD = Math.max(0,flashCD);
    }

    public void setRayCD(int rayCD) {
        this.rayCD = Math.max(0,rayCD);
    }

    public void setWindCD(int windCD) {
        this.windCD = Math.max(0,windCD);
    }
}
