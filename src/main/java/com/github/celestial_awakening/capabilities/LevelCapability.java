package com.github.celestial_awakening.capabilities;

import com.github.celestial_awakening.events.CommandMapValue;
import com.github.celestial_awakening.events.DelayedFunctionManager;
import com.github.celestial_awakening.events.command_patterns.GenericCommandPattern;
import com.github.celestial_awakening.events.command_patterns.UpdateDivinerEyeCommandPattern;
import com.github.celestial_awakening.events.raids.CARaids;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.celestial_awakening.nbt_strings.LevelCapNBTNames.*;

public class LevelCapability{
    public LevelCapability(Level level){
        initVals(level);
        raids=new CARaids();
    }

    private CompoundTag storedNBT;

    public ConcurrentHashMap<BlockPos,Short> currentMoonstonePos=new ConcurrentHashMap<>();

    public ResourceKey<Level> levelResourceKey;
    Codec<ResourceKey<Level>> levelCodec = ResourceKey.codec(Registries.DIMENSION);
    public float divinerEyeChance;
    public int divinerEyeCD;//in ticks
    public int divinerEyeTimer;//in ticks, how long the eye will persist in total for this iteration
    public byte divinerEyeFromState;
    public byte divinerEyeToState;
    public int divinerEyeCurrentChangeDelay;
    public float divinerEyeFrameProgress;//0-100, updated client side except when server changes frame, in which case it is set to 0
    public int divinerEyePower;//0-100, determines what abilities can be used, maybe make new cap in the thousands or 1 mil?
    public byte divinerSunControlVal;//-10 to 10

    public byte getDivinerSunControlType() {
        return divinerSunControlType;
    }

    public void setDivinerSunControlType(byte divinerSunControlType) {
        this.divinerSunControlType = divinerSunControlType;
    }

    public byte divinerSunControlType;//-3 to 3, used to determine which kind of sun control is executed
    public int divinerSunControlTimer;

    public int pkRemainingSpawnAttempts;
    public int prowlerSpawnCD;

    public void increaseDeepLayerCounter(int amt) {
        this.deepLayerCounter = Math.max(deepLayerCounterLim,deepLayerCounter+amt);
    }

    public int deepLayerCounter;//used to enable core guardian spawns
    public int deepLayerCounterLim=1000;//used to enable core guardian spawns
    /*
    -2: not active
    -1: eye closed
    0: eye fully open, pupil in center
    1-8:pupil is in position at edge of eye. Upward dir: 1, clockwise
     */
    public CARaids raids;


    public void updateData(LevelCapability data){
        this.divinerEyeToState=data.divinerEyeToState;
        this.divinerEyeFromState=data.divinerEyeFromState;
        this.divinerEyeCurrentChangeDelay =data.divinerEyeCurrentChangeDelay;
        this.divinerEyeFrameProgress=data.divinerEyeFrameProgress;
        this.divinerEyeTimer=data.divinerEyeTimer;
        this.divinerEyeChance=data.divinerEyeChance;
        this.divinerEyeCD=data.divinerEyeCD;
        this.divinerSunControlVal =data.divinerSunControlVal;
        this.divinerSunControlTimer =data.divinerSunControlTimer;
        if (this.divinerSunControlTimer==0){
            this.divinerSunControlVal=0;
        }
        this.currentMoonstonePos=data.currentMoonstonePos;
        this.levelResourceKey=data.levelResourceKey;
        this.prowlerSpawnCD=data.prowlerSpawnCD;
        this.divinerEyePower=data.divinerEyePower;
        this.divinerSunControlType=data.divinerSunControlType;
        this.deepLayerCounter=data.deepLayerCounter;
    }

    void saveNBTData(CompoundTag nbt){
        ListTag listTag=new ListTag();
        for (BlockPos blockPos:currentMoonstonePos.keySet()){
            CompoundTag compoundTag=new CompoundTag();
            compoundTag.putInt("x",blockPos.getX());
            compoundTag.putInt("y",blockPos.getY());
            compoundTag.putInt("z",blockPos.getZ());
            compoundTag.putShort(lvlCap_moonstoneTimer, currentMoonstonePos.get(blockPos));
            listTag.add(compoundTag);
        }
        nbt.put(lvlCap_moonstoneData,listTag);
        //then, look at level command map
        CompoundTag divEyeTag=new CompoundTag();
        divEyeTag.putInt(lvlCap_transcendentDivCD,this.divinerEyeCD);
        divEyeTag.putByte(lvlCap_transcendentDivFrom,this.divinerEyeFromState);
        divEyeTag.putByte(lvlCap_transcendentDivTo,this.divinerEyeToState);
        divEyeTag.putInt(lvlCap_transcendentDivChangeDelay,this.divinerEyeCurrentChangeDelay);

        divEyeTag.putFloat(lvlCap_transcendentDivFrameProgress,this.divinerEyeFrameProgress);
        divEyeTag.putFloat(lvlCap_transcendentDivChance,this.divinerEyeChance);

        divEyeTag.putInt(lvlCap_transcendentPower,this.divinerEyePower);
        divEyeTag.putByte(lvlCap_divSunControlVal,this.divinerSunControlVal);
        divEyeTag.putInt(lvlCap_divSunControlTime,this.divinerSunControlTimer);
        divEyeTag.putByte(lvlCap_divSunControlType,this.divinerSunControlType);
        if (this.levelResourceKey!=null){
            DataResult<Tag> result= levelCodec.encodeStart(NbtOps.INSTANCE,this.levelResourceKey);
            result.resultOrPartial(err->System.out.println(err)).ifPresent(encodedObj->{
                divEyeTag.put(lvlCap_transcendentLevelRK,encodedObj);
            });
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            ArrayList<CommandMapValue> arrayList=DelayedFunctionManager.delayedFunctionManager.getLevelCommandMap().get(server.getLevel(this.levelResourceKey));
            if (arrayList!=null){//is null for some reason
                for (CommandMapValue val:arrayList) {
                    GenericCommandPattern pattern=val.getPattern();
                    if (pattern instanceof UpdateDivinerEyeCommandPattern){
                        //System.out.println("WE ARE SAVING UCP with T " + this.divinerEyeTimer + "   CD  " + this.divinerEyeCurrentChangeDelay);
                        int tickCnt=val.getTicks();
                        int diff=this.divinerEyeCurrentChangeDelay-tickCnt;//how much time has actually passed
                        this.divinerEyeTimer-=diff;
                        this.divinerEyeCurrentChangeDelay =tickCnt;
                    }

                }
            }
        }
        divEyeTag.putInt(lvlCap_transcendentDivChangeDelay,this.divinerEyeCurrentChangeDelay);
        divEyeTag.putInt(lvlCap_transcendentDivTimer,this.divinerEyeTimer);

        nbt.put(lvlCap_transcendentHolder,divEyeTag);
        nbt.putInt(lvlCap_deepCnt,deepLayerCounter);

        nbt.put("Raids",raids.saveRaids());
    }

    public void loadNBTData(CompoundTag nbt,boolean insert){
        this.storedNBT=nbt;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        ListTag moonstoneList= (ListTag) nbt.get(lvlCap_moonstoneData);
        if (moonstoneList!=null){
            for (int i = 0; i < moonstoneList.size(); ++i) {
                CompoundTag compoundtag = moonstoneList.getCompound(i);
                BlockPos blockPos=new BlockPos(compoundtag.getInt("x"),compoundtag.getInt("y"),compoundtag.getInt("z"));
                currentMoonstonePos.put(blockPos,compoundtag.getShort(lvlCap_moonstoneTimer));
            }

        }
        CompoundTag divEye= nbt.getCompound(lvlCap_transcendentHolder);
        if (divEye!=null){
            this.divinerEyeTimer=divEye.getInt(lvlCap_transcendentDivTimer);
            this.divinerEyeCD=divEye.getInt(lvlCap_transcendentDivCD);
            this.divinerEyeFromState=divEye.getByte(lvlCap_transcendentDivFrom);
            this.divinerEyeToState=divEye.getByte(lvlCap_transcendentDivTo);
            this.divinerEyeCurrentChangeDelay =divEye.getInt(lvlCap_transcendentDivChangeDelay);
            this.divinerEyeFrameProgress=divEye.getFloat(lvlCap_transcendentDivFrameProgress);
            this.divinerEyeChance=divEye.getFloat(lvlCap_transcendentDivChance);
            this.divinerEyePower=divEye.getInt(lvlCap_transcendentPower);
            this.divinerSunControlVal =divEye.getByte(lvlCap_divSunControlVal);
            this.divinerSunControlTimer =divEye.getInt(lvlCap_divSunControlTime);
            this.divinerSunControlType=divEye.getByte(lvlCap_divSunControlType);
            levelCodec.parse(NbtOps.INSTANCE,divEye.get(lvlCap_transcendentLevelRK)).result().ifPresent(data->{
                if (data!=null){
                    this.levelResourceKey=data;
                }

            });
            if (divinerEyeTimer<=0){
                this.divinerEyeFromState=-2;
                this.divinerEyeToState=-2;
            }
            if (insert && this.levelResourceKey!=null && server.getLevel(this.levelResourceKey)!=null){
                if (this.divinerEyeTimer>0){
                    Object[] params=new Object[]{this,this.levelResourceKey};
                    DelayedFunctionManager.delayedFunctionManager.insertIntoLevelMap(ServerLifecycleHooks.getCurrentServer().getLevel(this.levelResourceKey),new UpdateDivinerEyeCommandPattern(params,0),this.divinerEyeCurrentChangeDelay,true);
                }
            }
        }
        deepLayerCounter=nbt.getInt(lvlCap_deepCnt);

        raids.loadRaids(nbt.getCompound("Raids"),server.getLevel(this.levelResourceKey));

    }
    public void loadNBTAfterLevelLoad(){
        if (storedNBT!=null){
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            //System.out.println("We got nbt here " + nbt);
            ListTag moonstoneList= (ListTag) storedNBT.get(lvlCap_moonstoneData);
            if (moonstoneList!=null){
                for (int i = 0; i < moonstoneList.size(); ++i) {
                    CompoundTag compoundtag = moonstoneList.getCompound(i);
                    BlockPos blockPos=new BlockPos(compoundtag.getInt("x"),compoundtag.getInt("y"),compoundtag.getInt("z"));
                    currentMoonstonePos.put(blockPos,compoundtag.getShort(lvlCap_moonstoneTimer));
                }
            }

            CompoundTag divEye= (CompoundTag) storedNBT.get(lvlCap_transcendentHolder);
            if (divEye!=null){
                this.divinerEyeTimer=divEye.getInt(lvlCap_transcendentDivTimer);
                this.divinerEyeCD=divEye.getInt(lvlCap_transcendentDivCD);
                this.divinerEyeFromState=divEye.getByte(lvlCap_transcendentDivFrom);
                this.divinerEyeToState=divEye.getByte(lvlCap_transcendentDivTo);
                this.divinerEyeCurrentChangeDelay =divEye.getInt(lvlCap_transcendentDivChangeDelay);
                this.divinerEyeFrameProgress=divEye.getFloat(lvlCap_transcendentDivFrameProgress);
                this.divinerEyeChance=divEye.getFloat(lvlCap_transcendentDivChance);
                this.divinerEyePower=divEye.getInt(lvlCap_transcendentPower);
                this.divinerSunControlVal =divEye.getByte(lvlCap_divSunControlVal);
                this.divinerSunControlTimer =divEye.getInt(lvlCap_divSunControlTime);
                this.divinerSunControlType=divEye.getByte(lvlCap_divSunControlType);
                levelCodec.parse(NbtOps.INSTANCE,divEye.get(lvlCap_transcendentLevelRK)).result().ifPresent(data->{
                    if (data!=null){
                        this.levelResourceKey=data;
                    }

                });
                if (divinerEyeTimer<=0 ){
                    this.divinerEyeFromState=-2;
                    this.divinerEyeToState=-2;
                }
                if (this.levelResourceKey!=null && server.getLevel(this.levelResourceKey)!=null){
                    if (this.divinerEyeTimer>0){
                        Object[] params=new Object[]{this,this.levelResourceKey};
                        DelayedFunctionManager.delayedFunctionManager.insertIntoLevelMap(ServerLifecycleHooks.getCurrentServer().getLevel(this.levelResourceKey),new UpdateDivinerEyeCommandPattern(params,0),this.divinerEyeCurrentChangeDelay,true);
                    }
                }
            }
            deepLayerCounter=storedNBT.getInt(lvlCap_deepCnt);

            raids.loadRaids(storedNBT.getCompound("Raids"),server.getLevel(this.levelResourceKey));
        }
    }
    public CompoundTag initNBTData(CompoundTag nbt){
        ListTag listTag=new ListTag();
        for (BlockPos blockPos:currentMoonstonePos.keySet()){
            CompoundTag compoundTag=new CompoundTag();
            compoundTag.putInt("x",blockPos.getX());
            compoundTag.putInt("y",blockPos.getY());
            compoundTag.putInt("z",blockPos.getZ());
            compoundTag.putShort(lvlCap_moonstoneTimer, currentMoonstonePos.get(blockPos));
            listTag.add(compoundTag);
        }
        nbt.put(lvlCap_moonstoneData,listTag);
        CompoundTag divEyeTag=new CompoundTag();
        divEyeTag.putInt(lvlCap_transcendentDivCD,this.divinerEyeCD);
        divEyeTag.putByte(lvlCap_transcendentDivFrom,this.divinerEyeFromState);
        divEyeTag.putByte(lvlCap_transcendentDivTo,this.divinerEyeToState);
        divEyeTag.putInt(lvlCap_transcendentDivChangeDelay,this.divinerEyeCurrentChangeDelay);
        divEyeTag.putFloat(lvlCap_transcendentDivFrameProgress,this.divinerEyeFrameProgress);
        divEyeTag.putFloat(lvlCap_transcendentDivChance,this.divinerEyeChance);
        divEyeTag.putInt(lvlCap_transcendentDivTimer,this.divinerEyeTimer);
        divEyeTag.putInt(lvlCap_transcendentPower,this.divinerEyePower);
        divEyeTag.putByte(lvlCap_divSunControlVal,this.divinerSunControlVal);
        divEyeTag.putInt(lvlCap_divSunControlTime,this.divinerSunControlTimer);
        divEyeTag.putByte(lvlCap_divSunControlType,this.divinerSunControlType);
        nbt.put(lvlCap_transcendentHolder,divEyeTag);
        nbt.putInt(lvlCap_deepCnt,deepLayerCounter);
        nbt.put("Raids",raids.saveRaids());
        return nbt;
    }


    //called when making a new capability
    public void initVals(Level level){
        this.divinerEyeTimer=0;
        this.divinerEyeCD=24000;
        this.divinerEyeFromState=-2;
        this.divinerEyeToState=-2;
        this.divinerEyeCurrentChangeDelay =0;
        this.divinerEyeFrameProgress=0;
        this.divinerEyeChance=0;
        this.divinerSunControlVal =0;
        this.divinerEyePower=0;
        this.deepLayerCounter=0;
        this.raids=new CARaids();
        if (level!=null){
            this.levelResourceKey=level.dimension();
        }
    }


    public void changeDivPower(int i){
        this.divinerEyePower=Math.max(Math.min(this.divinerEyePower+i,100),0);
    }


    public boolean decrementSunControlTimer(){
        //System.out.println("sun control timer  is " + divinerSunControlTimer + " and val is " + divinerSunControlVal);//so timer and val are fine
        if (divinerSunControlTimer>0){

            this.divinerSunControlTimer =Math.max(0,this.divinerSunControlTimer-1);
            if (this.divinerSunControlTimer ==0){
                this.divinerSunControlVal=0;
                return true;
            }
        }
        return false;
    }
    public void setSunControlVal(byte i){
        this.divinerSunControlVal= (byte) Math.max(-10,Math.min(10,i));
    }
}
