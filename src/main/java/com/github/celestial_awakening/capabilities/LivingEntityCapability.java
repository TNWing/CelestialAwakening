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
import static com.github.celestial_awakening.nbt_strings.LivingEntityNBTNames.*;
import static com.github.celestial_awakening.nbt_strings.LivingEntityNBTNames.leCap_abilityData;

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

    short insanityPts;
    short navigauge;//used for diviner
    public void setInsanityValue(short i){
        insanityPts=i;
    }
    public void changeInsanityVal(short i){
        insanityPts+=i;
    }
    public void changeNaviGauge(short i){
        navigauge= (short) Math.max(0,Math.min(navigauge+i,(short)100));
    }
    public void increaseNaviGauge(short i){
        navigauge+=i;
    }
    public short getNavigauge(){
        return navigauge;
    }
    public void setUUID(UUID id){
        this.uuid =id;
    }

    public LivingEntityCapability(){
    }
    private ConcurrentHashMap<String, Pair<Integer,Object[]>> abilityDataMap=new ConcurrentHashMap<>();

    public CompoundTag initNBTData(CompoundTag nbt){
        ListTag abilities=new ListTag();
        for (Map.Entry<String, Pair<Integer,Object[]>> entry:abilityDataMap.entrySet()){
            CompoundTag compoundTag=new CompoundTag();
            compoundTag.putString(leCap_abilityName,entry.getKey());
            compoundTag.putInt(leCap_abilityCD,entry.getValue().getFirst());


            ListTag dataList=new ListTag();

            Object[] data=entry.getValue().getSecond();

            for (Object obj:data) {
                if (obj instanceof UUID){
                    dataList.add(StringTag.valueOf(((UUID)obj).toString()));
                }
                else if (obj instanceof Integer){
                    dataList.add(IntTag.valueOf((Integer) obj));
                }
            }
            compoundTag.put(leCap_abilityData,dataList);
            abilities.add(compoundTag);
        }
        nbt.put(leCap_abilities,abilities);
        nbt.putShort(leCap_insanity,insanityPts);
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
        commandPair.putString("Name","Fade");
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
            compoundTag.putString(leCap_abilityName,entry.getKey());
            compoundTag.putInt(leCap_abilityCD,entry.getValue().getFirst());


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
            compoundTag.put(leCap_abilityData,dataList);
            abilities.add(compoundTag);
        }
        nbt.put(leCap_abilities,abilities);
        nbt.putShort(leCap_navigauge,navigauge);
        nbt.putShort(leCap_insanity,insanityPts);

    }
    public void loadNBTData(CompoundTag nbt,boolean insert){
        ListTag commands=(ListTag)nbt.get("CommandPatterns");
        if (commands!=null){
            for (int i = 0; i <commands.size(); ++i) {
                CompoundTag compoundtag = commands.getCompound(i);
                String commandName=compoundtag.getString("Name");
                Integer cd=compoundtag.getInt("CD");
                GenericCommandPattern pattern= LivingEntityCapHelperFuncs.findPatternFromString(commandName);
            }
        }
        ListTag abilities=(ListTag) nbt.get(leCap_abilities);
        if (abilities!=null){
            for (int i = 0; i <abilities.size(); ++i) {
                CompoundTag compoundtag = abilities.getCompound(i);
                String abilityName=compoundtag.getString(leCap_abilityName);
                Integer cd=compoundtag.getInt(leCap_abilityCD);
                ListTag data= (ListTag) compoundtag.get(leCap_abilityData);
                List<Object> objList = new ArrayList<>();
                for (int a = 0; a < data.size(); ++a) {
                    Tag abilityTag = data.get(a);
                    if (abilityTag instanceof StringTag){
                        objList.add(UUID.fromString(abilityTag.getAsString()));
                    }
                    else if (abilityTag instanceof IntTag){
                        objList.add(((IntTag)abilityTag).getAsInt());
                    }
                }
                abilityDataMap.put(abilityName,new Pair<>(cd, objList.toArray()));
            }
        }
        navigauge=nbt.getShort(leCap_navigauge);
        insanityPts=nbt.getShort(leCap_insanity);
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
        this.navigauge=data.navigauge;
        this.insanityPts=data.insanityPts;
    }

    public void copyForRespawn(LivingEntityCapability data){
        //this.abilityDataMap=data.abilityDataMap;
        this.abilityDataMap=new ConcurrentHashMap<>();
        this.navigauge=data.navigauge;
        this.insanityPts=data.insanityPts;
    }
}
