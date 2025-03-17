package com.github.celestial_awakening.capabilities;

import com.github.celestial_awakening.events.CommandMapValue;
import com.github.celestial_awakening.events.DelayedFunctionManager;
import com.github.celestial_awakening.events.GenericCommandPattern;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LivingEntityCapability {
    /**
    Data to store


     */
    UUID uuid;


    /*
    TODO
    rename cd int to timer
    move the abilityMap to player cap, use this for all livingentities in general
    alternatively, rename this back to player cap and use the other one for livingentities
     */
    /*
    Idea on how to properly implement CDs
    anytime a CD is added, it adds a string for the associated CD and the CD amt to the capability (IF NEEDED, can also just add it when a player logs or smth)

     */

    int insanityPts;
    int navigauge;//used for diviner
    public void increaseInsanityValue(int i){
        insanityPts+=i;
    }
    public void increaseNaviGauge(int i){
        navigauge+=i;
    }
    public void setUUID(UUID id){
        this.uuid =id;
    }

    public LivingEntityCapability(){
    }
    private ConcurrentHashMap<String, Pair<Integer,Object[]>> abilityDataMap=new ConcurrentHashMap<>();
    /*
    instead of cd map, the above map does
     */
    public CompoundTag initNBTData(CompoundTag nbt){
        ListTag abilities=new ListTag();
        for (Map.Entry<String, Pair<Integer,Object[]>> entry:abilityDataMap.entrySet()){
            CompoundTag compoundTag=new CompoundTag();
            compoundTag.putString("Name",entry.getKey());
            compoundTag.putInt("CD",entry.getValue().getFirst());


            ListTag dataList=new ListTag();

            Object[] data=entry.getValue().getSecond();

            for (Object obj:data) {
                System.out.println("DATA TO SAVE IS " + obj);
                if (obj instanceof UUID){
                    dataList.add(StringTag.valueOf(((UUID)obj).toString()));
                }
                else if (obj instanceof Integer){
                    dataList.add(IntTag.valueOf((Integer) obj));
                }
            }
            compoundTag.put("Data",dataList);
            abilities.add(compoundTag);
        }
        nbt.put("Abilities",abilities);
        return nbt;
    }

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
        ListTag abilities=new ListTag();
        for (Map.Entry<String, Pair<Integer,Object[]>> entry:abilityDataMap.entrySet()){
            CompoundTag compoundTag=new CompoundTag();
            compoundTag.putString("Name",entry.getKey());
            compoundTag.putInt("CD",entry.getValue().getFirst());


            ListTag dataList=new ListTag();

            Object[] data=entry.getValue().getSecond();
            for (Object obj:data) {
                if (obj instanceof UUID){
                    dataList.add(StringTag.valueOf(obj.toString()));
                }
                else if (obj instanceof Integer){
                    dataList.add(IntTag.valueOf((Integer) obj));
                }
            }
            System.out.println("WE ARE saving data for " + entry.getKey() +  ", data is as follows" +  dataList.toString());
            compoundTag.put("Data",dataList);
            abilities.add(compoundTag);
        }
        nbt.put("Abilities",abilities);
        nbt.putInt("NaviGauge",navigauge);

    }
    public void loadNBTData(CompoundTag nbt,boolean insert){
        ListTag commands=(ListTag)nbt.get("CommandPatterns");
        if (commands!=null){
            for (int i = 0; i <commands.size(); ++i) {
                CompoundTag compoundtag = commands.getCompound(i);
                String commandName=compoundtag.getString("CommandName");
                Integer cd=compoundtag.getInt("CD");
                GenericCommandPattern pattern= LivingEntityCapHelperFuncs.findPatternFromString(commandName);
            }
        }
        ListTag abilities=(ListTag) nbt.get("Abilities");
        if (abilities!=null){
            for (int i = 0; i <abilities.size(); ++i) {
                CompoundTag compoundtag = abilities.getCompound(i);
                String abilityName=compoundtag.getString("Name");
                Integer cd=compoundtag.getInt("CD");
                ListTag data= (ListTag) compoundtag.get("Data");
                List<Object> objList = new ArrayList<>();
                for (int a = 0; a < data.size(); ++a) {
                    Tag abilityTag = data.get(a);
                    if (abilityTag instanceof StringTag){
                        objList.add(UUID.fromString(((StringTag)abilityTag).getAsString()));
                        System.out.println("LOADING UUID " + UUID.fromString(((StringTag)abilityTag).getAsString()));
                    }
                    else if (abilityTag instanceof IntTag){
                        objList.add(((IntTag)abilityTag).getAsInt());
                    }
                }
                System.out.println("WE got ability " + abilityName + " WITH TIME " + cd);
                abilityDataMap.put(abilityName,new Pair<>(cd, objList.toArray()));
            }
        }
        navigauge=nbt.getInt("NaviGauge");
    }

    public void insertIntoAbilityMap(String abilityName, Integer cd){
        abilityDataMap.put(abilityName,new Pair<>(cd,new Object[0]));
    }
    public void removeFromAbilityMap(String abilityName){
        abilityDataMap.remove(abilityName);
    }
    public void insertIntoAbilityMap(String abilityName,Integer cd,Object[] data){
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
    public boolean hasAbility(String abilityName){
        return abilityDataMap.containsKey(abilityName);
    }
    public Integer getAbilityCD(String abilityName){
        if (abilityDataMap.containsKey(abilityName)){
            return abilityDataMap.get(abilityName).getFirst();
        }
        return null;
    }
    public Object[] getAbilityData(String abilityName){
        if (abilityDataMap.containsKey(abilityName)){
            return abilityDataMap.get(abilityName).getSecond();
        }
        return null;
    }
    public void changeAbilityData(String abilityName,Object[] data){
        if (abilityDataMap.containsKey(abilityName)){
            abilityDataMap.put(abilityName, new Pair<>(abilityDataMap.get(abilityName).getFirst(), data));
        }
    }
    public void tickAbilityMap(){
        Iterator<Map.Entry<String, Pair<Integer,Object[]>>> iterator = abilityDataMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Pair<Integer,Object[]>> entry = iterator.next();
            Pair<Integer,Object[]> val=entry.getValue();
            if (val.getFirst()!=-10){
                entry.setValue(new Pair<>(val.getFirst()-1,val.getSecond()));
                if (val.getFirst()-1 <= 0) {
                    if (LivingEntityCapHelperFuncs.onRemoveFromMap(entry.getKey(),uuid)){
                        iterator.remove();
                    }

                }
            }

        }
    }

    public void updateData(LivingEntityCapability data){
        this.abilityDataMap=data.abilityDataMap;
    }

    public void copy(LivingEntityCapability data){
        this.abilityDataMap=data.abilityDataMap;
        this.insanityPts=data.insanityPts;
        this.navigauge=data.navigauge;
    }

    public void copyForRespawn(LivingEntityCapability data){
        this.abilityDataMap=data.abilityDataMap;
        this.abilityDataMap.clear();
        this.insanityPts=data.insanityPts;
        this.navigauge=data.navigauge;
    }
}
