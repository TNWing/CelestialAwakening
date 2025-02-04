package com.github.celestial_awakening.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import static com.github.celestial_awakening.capabilities.MovementModifierNBTNames.*;


public class ProjCapability {
    int entityID;
    List<MovementModifier> movementModificationList = Collections.synchronizedList(new LinkedList<MovementModifier>());
    public ProjCapability(int id){
        this.entityID=id;
    }
    void saveNBTData(CompoundTag nbt,List<MovementModifier> movementModificationList){

    }
    void loadNBTData(CompoundTag nbt){
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
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
