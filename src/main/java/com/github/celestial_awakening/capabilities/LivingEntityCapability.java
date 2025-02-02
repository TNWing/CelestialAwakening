package com.github.celestial_awakening.capabilities;

import com.github.celestial_awakening.events.CommandMapValue;
import com.github.celestial_awakening.events.DelayedFunctionManager;
import com.github.celestial_awakening.events.GenericCommandPattern;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LivingEntityCapability {
    /**
    Data to store


     */
    UUID uuid;

    int navigauge;

    /*
    TODO
    rename cd int to timer
     */
    /*
    Idea on how to properly implement CDs
    anytime a CD is added, it adds a string for the associated CD and the CD amt to the capability (IF NEEDED, can also just add it when a player logs or smth)

     */

    public void setUUID(UUID id){
        this.uuid =id;
    }
    public LivingEntityCapability(){
    }
    private ConcurrentHashMap<String, Pair<Integer,Integer[]>> abilityDataMap=new ConcurrentHashMap<>();
    /*
    instead of cd map, the above map does
     */

    //TODO: preserve cap data on death
    void saveNBTData(CompoundTag nbt){
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (uuid !=null && DelayedFunctionManager.delayedFunctionManager.getPlayerCommandMap().containsKey(uuid)){
            ArrayList<CommandMapValue> arrayList= DelayedFunctionManager.delayedFunctionManager.getPlayerCommandMap().get(server.getPlayerList().getPlayer(uuid));

        }

        //TODO: code below is just to visualize process
        /*
        CompoundTag testNBT = new CompoundTag();
        CompoundTag cdData=new CompoundTag
        CompoundTag commandPair = new CompoundTag();
        commandPair.putString("CommandName","Fade");
        commandPair.putInt("CD",5);
        cdData.put(commandPair)
        testNBT.put(cdData);

         */

        /*
        main issue is when the data is loaded, i need to map each command name to command pattern
         */
        ListTag cdData= new ListTag();
        for (Map.Entry<String,Pair<Integer,Integer[]>> entry:abilityDataMap.entrySet()) {
            CompoundTag compoundTag=new CompoundTag();
            compoundTag.putString("Name",entry.getKey());
            compoundTag.putInt("CD",entry.getValue().getFirst());
            compoundTag.putIntArray("Data", List.of(entry.getValue().getSecond()));
            System.out.println("SAVING ability " + entry.getKey() + " WITH TIME " + entry.getValue().getFirst());
            cdData.add(compoundTag);
        }
        nbt.put("AbilityData",cdData);

    }
    public void loadNBTData(CompoundTag nbt,boolean insert){
        ListTag commands=(ListTag)nbt.get("CommandPatterns");
        if (commands!=null){
            for (int i = 0; i <commands.size(); ++i) {
                CompoundTag compoundtag = commands.getCompound(i);
                String commandName=compoundtag.getString("CommandName");
                Integer cd=compoundtag.getInt("CD");
                GenericCommandPattern pattern= PlayerCapHelperFuncs.findPatternFromString(commandName);
            }
        }
        ListTag cdData=(ListTag) nbt.get("AbilityData");
        if (cdData!=null){
            for (int i = 0; i <cdData.size(); ++i) {
                CompoundTag compoundtag = cdData.getCompound(i);
                String abilityName=compoundtag.getString("Name");
                Integer cd=compoundtag.getInt("CD");
                Integer[] data= Arrays.stream(compoundtag.getIntArray("Data")).boxed().toArray(Integer[]::new);
                System.out.println("WE got ability " + abilityName + " WITH TIME " + cd);
                abilityDataMap.put(abilityName,new Pair<>(cd,data));
            }
        }
    }

    public void insertIntoAbilityMap(String abilityName, Integer cd){
        abilityDataMap.put(abilityName,new Pair<>(cd,new Integer[0]));
    }

    public void insertIntoAbilityMap(String abilityName,Integer cd,Integer[] data){
        abilityDataMap.put(abilityName,new Pair<>(cd,data));
    }
    public void changeAbilityCD(String abilityName, int amt){
        int newCD=abilityDataMap.get(abilityName).getFirst()+amt;
        if (newCD<=0){
            abilityDataMap.remove(abilityName);
        }
        else{
            abilityDataMap.put(abilityName,new Pair<>(newCD,abilityDataMap.get(abilityName).getSecond()));
        }

    }
    public Integer getAbilityCD(String abilityName){
        if (abilityDataMap.containsKey(abilityName)){
            return abilityDataMap.get(abilityName).getFirst();
        }
        return null;
    }
    public Integer[] getAbilityData(String abilityName){
        if (abilityDataMap.containsKey(abilityName)){
            return abilityDataMap.get(abilityName).getSecond();
        }
        return null;
    }
    public void tickCDMap(){
        Iterator<Map.Entry<String, Pair<Integer,Integer[]>>> iterator = abilityDataMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Pair<Integer,Integer[]>> entry = iterator.next();
            Pair<Integer,Integer[]> val=entry.getValue();
            entry.setValue(new Pair<>(val.getFirst()-1,val.getSecond()));
            if (val.getFirst()-1 <= 0) {
                iterator.remove();
                LivingEntityCapHelperFuncs.onRemoveFromMap(entry.getKey(),uuid);
            }
        }
    }

    public void updateData(LivingEntityCapability data){
        this.abilityDataMap=data.abilityDataMap;
    }
    public CompoundTag initNBTData(CompoundTag nbt){
        ListTag cdData=new ListTag();
        for (Map.Entry<String, Pair<Integer,Integer[]>> entry:abilityDataMap.entrySet()){
            CompoundTag compoundTag=new CompoundTag();
            compoundTag.putString("Name",entry.getKey());
            compoundTag.putInt("CD",entry.getValue().getFirst());
            compoundTag.putIntArray("Data", List.of(entry.getValue().getSecond()));
            cdData.add(compoundTag);
        }
        nbt.put("AbilityData",cdData);
        return nbt;
    }
}
