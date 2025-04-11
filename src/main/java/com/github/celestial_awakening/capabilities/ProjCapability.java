package com.github.celestial_awakening.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.github.celestial_awakening.nbt_strings.MovementModifierNBTNames.*;


public class ProjCapability {
    int entityID;
    List<MovementModifier> movementModificationList = Collections.synchronizedList(new LinkedList<MovementModifier>());
    public ProjCapability(int id){
        this.entityID=id;
    }
    void saveNBTData(CompoundTag nbt,MovementModifier mod){
        ListTag listTag=new ListTag();
        if (mod!=null){
            listTag.add(mmToNBT(mod));
        }

        for (MovementModifier modifier:movementModificationList) {
            CompoundTag modTag=mmToNBT(modifier);
            listTag.add(modTag);
        }
        nbt.put(movementMods,listTag);
    }
    void loadNBTData(CompoundTag nbt){
        //MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        ListTag mmList= (ListTag) nbt.get(movementMods);
        if (mmList!=null){
            for (int i = 0; i < mmList.size(); ++i) {
                CompoundTag compoundtag = mmList.getCompound(i);
                MovementModifier mod=nbtToMod(compoundtag);
                movementModificationList.add(mod);
            }
        }
    }
    CompoundTag mmToNBT(MovementModifier mod){
        CompoundTag nbt=new CompoundTag();
        nbt.putByte(mm_angFunc, (byte) mod.getAngFunction().ordinal());
        nbt.putByte(mm_angOp, (byte) mod.getAngOperation().ordinal());
        nbt.putFloat(mm_hAng,mod.getHAng());
        nbt.putFloat(mm_vAng,mod.getVAng());

        nbt.putByte(mm_spdFunc, (byte) mod.getSpdFunction().ordinal());
        nbt.putByte(mm_spdOp, (byte) mod.getSpdOperation().ordinal());
        nbt.putFloat(mm_spd,mod.getSpd());

        nbt.putByte(mm_rotFunc, (byte) mod.getRotFunction().ordinal());
        nbt.putByte(mm_rotOp, (byte) mod.getRotOperation().ordinal());
        nbt.putFloat(mm_rot,mod.getZRot());

        nbt.putLong(mm_serverTime,mod.getServerTime());

        nbt.putShort(mm_delay, (short) mod.getDelay());
        nbt.putShort(mm_initialTicks, (short) mod.getStartingTicks());
        nbt.putShort(mm_remainingTicks, (short) mod.getRemainingTicks());
        return nbt;
    }

    MovementModifier nbtToMod(CompoundTag tag){

        float spdMod=tag.getFloat(mm_spd);
        MovementModifier.modFunction sFunc=MovementModifier.modFunction.values()[tag.getByte(mm_spdFunc)];
        MovementModifier.modOperation sOp=MovementModifier.modOperation.values()[tag.getByte(mm_spdOp)];

        float hA=tag.getFloat(mm_hAng);
        float vA=tag.getFloat(mm_vAng);
        MovementModifier.modFunction aFunc=MovementModifier.modFunction.values()[tag.getByte(mm_angFunc)];
        MovementModifier.modOperation aOp=MovementModifier.modOperation.values()[tag.getByte(mm_angOp)];

        float zR=tag.getFloat(mm_rot);
        MovementModifier.modFunction rFunc=MovementModifier.modFunction.values()[tag.getByte(mm_rotFunc)];
        MovementModifier.modOperation rOp=MovementModifier.modOperation.values()[tag.getByte(mm_rotOp)];

        int d=tag.getShort(mm_delay);
        int timer=tag.getShort(mm_remainingTicks);
        int i=tag.getShort(mm_initialTicks);
        long sTime=tag.getLong(mm_serverTime);

        MovementModifier mod=new MovementModifier(sFunc,sOp,aFunc,aOp,rFunc,rOp,spdMod,hA,vA,zR,d,i,sTime,timer);

        return mod;
    }

    public void putInFrontOfList(MovementModifier mod){
        movementModificationList.add(0,mod);
    }
    public void putInBackOfList(MovementModifier mod){
        movementModificationList.add(mod);
    }
    public MovementModifier popFromList(){
        synchronized (movementModificationList) {
            if (!movementModificationList.isEmpty()) {
                return movementModificationList.remove(0);
            }
            return null;
        }
    }

    public List<MovementModifier> getMMList(){
        return this.movementModificationList;
    }
    public void updateData(ProjCapability data){
        this.movementModificationList=data.getMMList();
    }
}
