package com.github.celestial_awakening.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

    public void putInFrontOfList(MovementModifier mod){
        movementModificationList.add(0,mod);
    }
    public void putInBackOfList(MovementModifier mod){
        movementModificationList.add(mod);
    }
    public MovementModifier popFromList(){
        synchronized (movementModificationList) {
            if (!movementModificationList.isEmpty()) {
                MovementModifier mod=movementModificationList.get(0);
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
