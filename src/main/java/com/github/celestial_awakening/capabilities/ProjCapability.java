package com.github.celestial_awakening.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.github.celestial_awakening.nbt_strings.MovementModifierNBTNames.*;


public class ProjCapability {
    int entityID;
    List<MovementModifier> movementModificationList = Collections.synchronizedList(new LinkedList<MovementModifier>());
    /*

        List<MovementModifier> spdModList = Collections.synchronizedList(new LinkedList<MovementModifier>());
        List<MovementModifier> angModList = Collections.synchronizedList(new LinkedList<MovementModifier>());
        List<MovementModifier> rotModList = Collections.synchronizedList(new LinkedList<MovementModifier>());

        having 3 separate lists (1 for each kind of mm) has several benefits but also downsides
        Pros
        -allows for a bit more flexibility when multiple modifiers are present
        -less data sent via packets
        -looks cleaner, and easier to debug most likely

        Cons
        -have to refactor and redo a bunch of stuff
        -more data can be stored (a minor issue)
        -managing mm in the projectile class can be more time consuming

        If i were to do this, general structure would be
        MovementModifier as a parent class
        SpdMM, AngMM, RotMM as children classes
     */
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
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
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
        nbt.putInt(angFunc,mod.getAngFunction().ordinal());
        nbt.putInt(angOp,mod.getAngOperation().ordinal());
        nbt.putFloat(hAng,mod.getHAng());
        nbt.putFloat(vAng,mod.getVAng());

        nbt.putInt(spdFunc,mod.getSpdFunction().ordinal());
        nbt.putInt(spdOp,mod.getSpdOperation().ordinal());
        nbt.putFloat(spd,mod.getSpd());

        nbt.putInt(rotFunc,mod.getRotFunction().ordinal());
        nbt.putInt(rotOp,mod.getRotOperation().ordinal());
        nbt.putFloat(rot,mod.getZRot());

        nbt.putLong(serverTime,mod.getServerTime());

        nbt.putInt(delay,mod.getDelay());
        nbt.putInt(initialTicks,mod.getStartingTicks());
        nbt.putInt(remainingTicks,mod.getRemainingTicks());
        return nbt;
    }
    MovementModifier nbtToMod(CompoundTag tag){

        float spdMod=tag.getFloat(spd);
        MovementModifier.modFunction sFunc=MovementModifier.modFunction.values()[tag.getInt(spdFunc)];
        MovementModifier.modOperation sOp=MovementModifier.modOperation.values()[tag.getInt(spdOp)];

        float hA=tag.getFloat(hAng);
        float vA=tag.getFloat(vAng);
        MovementModifier.modFunction aFunc=MovementModifier.modFunction.values()[tag.getInt(angFunc)];
        MovementModifier.modOperation aOp=MovementModifier.modOperation.values()[tag.getInt(angOp)];

        float zR=tag.getFloat(rot);
        MovementModifier.modFunction rFunc=MovementModifier.modFunction.values()[tag.getInt(rotFunc)];
        MovementModifier.modOperation rOp=MovementModifier.modOperation.values()[tag.getInt(rotOp)];

        int d=tag.getInt(delay);
        int timer=tag.getInt(remainingTicks);
        int i=tag.getInt(initialTicks);
        long sTime=tag.getLong(serverTime);

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
