package com.github.celestial_awakening.capabilities;

import com.github.celestial_awakening.events.CommandMapValue;
import com.github.celestial_awakening.events.DelayedFunctionManager;
import com.github.celestial_awakening.events.GenericCommandPattern;
import com.github.celestial_awakening.events.UpdateDivinerEyeCommandPattern;
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

import static com.github.celestial_awakening.nbt_strings.LevelCapNBTNames.nbtName_DivSunControlTime;
import static com.github.celestial_awakening.nbt_strings.LevelCapNBTNames.nbtName_DivSunControlVal;
public class LevelCapability{
    public LevelCapability(Level level){
        initVals(level);
    }

    private CompoundTag storedNBT;

    public ConcurrentHashMap<BlockPos,Integer> currentMoonstonePos=new ConcurrentHashMap<>();

    public ResourceKey<Level> levelResourceKey;
    Codec<ResourceKey<Level>> levelCodec = ResourceKey.codec(Registries.DIMENSION);
    public float divinerEyeChance;
    public int divinerEyeCD;//in ticks
    public int divinerEyeTimer;//in ticks, how long the eye will persist in total for this iteration
    public int divinerEyeFromState;
    public int divinerEyeToState;
    public int divinerEyeCurrentChangeDelay;
    public float divinerEyeFrameProgress;//0-100, updated client side except when server changes frame, in which case it is set to 0
    public int divinerEyePower;//0-100, determines what abilities can be used, maybe make new cap in the thousands or 1 mil?
    public int divinerSunControlVal;//-10 to 10
    public int divinerSunControlTimer;

    public int pkRemainingSpawnAttempts;
    public int prowlerSpawnCD;
    /*
    -2: not active
    -1: eye closed
    0: eye fully open, pupil in center
    1-8:pupil is in position at edge of eye. Upward dir: 1, clockwise
    wondering if i can do an overlay style
    instead of a separate file for each unique frame, i instead have a few image files for each general state (closed, half open, open.
    Then, i have a separate img file overlayed on it for the open states, which results in less img files.
    furthermore, this overlayed file doesnt need to be anything special. it can be a really tiny img whose position is shifted.
     */


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
    }

    void saveNBTData(CompoundTag nbt){
        ListTag listTag=new ListTag();
        for (BlockPos blockPos:currentMoonstonePos.keySet()){
            CompoundTag compoundTag=new CompoundTag();
            compoundTag.putInt("x",blockPos.getX());
            compoundTag.putInt("y",blockPos.getY());
            compoundTag.putInt("z",blockPos.getZ());
            compoundTag.putInt("timer", currentMoonstonePos.get(blockPos));
            listTag.add(compoundTag);
        }
        nbt.put("moonstoneData",listTag);
        //then, look at level command map
        CompoundTag divEyeTag=new CompoundTag();
        divEyeTag.putInt("cd",this.divinerEyeCD);
        divEyeTag.putInt("fromState",this.divinerEyeFromState);
        divEyeTag.putInt("toState",this.divinerEyeToState);
        divEyeTag.putInt("changeDelay",this.divinerEyeCurrentChangeDelay);

        divEyeTag.putFloat("frameProgress",this.divinerEyeFrameProgress);
        divEyeTag.putFloat("chance",this.divinerEyeChance);

        divEyeTag.putInt("timer",this.divinerEyeTimer);
        divEyeTag.putInt("power",this.divinerEyePower);
        divEyeTag.putInt(nbtName_DivSunControlVal,this.divinerSunControlVal);
        divEyeTag.putInt(nbtName_DivSunControlTime,this.divinerSunControlTimer);
        DataResult<Tag> result= levelCodec.encodeStart(NbtOps.INSTANCE,this.levelResourceKey);
        result.resultOrPartial(err->System.out.println(err)).ifPresent(encodedObj->divEyeTag.put("levelRK",encodedObj));//not savingg?

        //its bc the keys of lcm are Levels, but i save it as the rk of the level heere
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
        //ccd 3 is 0
        divEyeTag.putInt("changeDelay",this.divinerEyeCurrentChangeDelay);
        divEyeTag.putInt("timer",this.divinerEyeTimer);

        nbt.put("divEye",divEyeTag);
    }

    public void loadNBTData(CompoundTag nbt,boolean insert){
        this.storedNBT=nbt;
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        ListTag moonstoneList= (ListTag) nbt.get("moonstoneData");
        if (moonstoneList!=null){
            for (int i = 0; i < moonstoneList.size(); ++i) {
                CompoundTag compoundtag = moonstoneList.getCompound(i);
                BlockPos blockPos=new BlockPos(compoundtag.getInt("x"),compoundtag.getInt("y"),compoundtag.getInt("z"));
                currentMoonstonePos.put(blockPos,compoundtag.getInt("timer"));
            }

        }
        CompoundTag divEye= nbt.getCompound("divEye");
        if (divEye!=null){
            this.divinerEyeTimer=divEye.getInt("timer");
            this.divinerEyeCD=divEye.getInt("cd");
            this.divinerEyeFromState=divEye.getInt("fromState");
            this.divinerEyeToState=divEye.getInt("toState");
            this.divinerEyeCurrentChangeDelay =divEye.getInt("changeDelay");
            this.divinerEyeFrameProgress=divEye.getFloat("frameProgress");
            this.divinerEyeChance=divEye.getFloat("chance");
            this.divinerEyePower=divEye.getInt("power");
            this.divinerSunControlVal =divEye.getInt(nbtName_DivSunControlVal);
            this.divinerSunControlTimer =divEye.getInt(nbtName_DivSunControlTime);
            this.levelResourceKey=levelCodec.parse(NbtOps.INSTANCE,divEye.get("levelRK")).result().orElse(null);
            System.out.println("TIMER is " +divinerEyeTimer+ " WITH STATES " + this.divinerEyeFromState +"   " + this.divinerEyeToState);
            if (insert && this.levelResourceKey!=null && server.getLevel(this.levelResourceKey)!=null){
                if (this.divinerEyeTimer>0){
                    Object[] params=new Object[]{this,this.levelResourceKey};
                    DelayedFunctionManager.delayedFunctionManager.insertIntoLevelMap(ServerLifecycleHooks.getCurrentServer().getLevel(this.levelResourceKey),new UpdateDivinerEyeCommandPattern(params,0),this.divinerEyeCurrentChangeDelay,true);
                }
            }
        }

    }
    public void loadNBTAfterLevelLoad(){
        if (storedNBT!=null){
            MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
            //System.out.println("We got nbt here " + nbt);
            ListTag moonstoneList= (ListTag) storedNBT.get("moonstoneData");
            for (int i = 0; i < moonstoneList.size(); ++i) {
                CompoundTag compoundtag = moonstoneList.getCompound(i);
                //currentMoonstonePos.put(new BlockPos(compoundtag.getInt("x"),compoundtag.getInt("y"),compoundtag.getInt("z")),compoundtag.getInt("timer"));
                BlockPos blockPos=new BlockPos(compoundtag.getInt("x"),compoundtag.getInt("y"),compoundtag.getInt("z"));
                currentMoonstonePos.put(blockPos,compoundtag.getInt("timer"));
            }
            CompoundTag divEye= (CompoundTag) storedNBT.get("divEye");
            this.divinerEyeTimer=divEye.getInt("timer");
            this.divinerEyeCD=divEye.getInt("cd");
            this.divinerEyeFromState=divEye.getInt("fromState");
            this.divinerEyeToState=divEye.getInt("toState");
            this.divinerEyeCurrentChangeDelay =divEye.getInt("changeDelay");
            this.divinerEyeFrameProgress=divEye.getFloat("frameProgress");
            this.divinerEyeChance=divEye.getFloat("chance");
            this.divinerEyePower=divEye.getInt("power");
            this.divinerSunControlVal =divEye.getInt(nbtName_DivSunControlVal);
            this.divinerSunControlTimer =divEye.getInt(nbtName_DivSunControlTime);
            this.levelResourceKey=levelCodec.parse(NbtOps.INSTANCE,divEye.get("levelRK")).result().orElse(null);
            System.out.println("LOADING DIV Data AFTER Lvl load on levelkey " + this.levelResourceKey);
            System.out.println("TIMER after is " +divinerEyeTimer +" WITH STATES " + this.divinerEyeFromState +"   " + this.divinerEyeToState);
            if (this.levelResourceKey!=null && server.getLevel(this.levelResourceKey)!=null){
                if (this.divinerEyeTimer>0){
                    Object[] params=new Object[]{this,this.levelResourceKey};
                    DelayedFunctionManager.delayedFunctionManager.insertIntoLevelMap(ServerLifecycleHooks.getCurrentServer().getLevel(this.levelResourceKey),new UpdateDivinerEyeCommandPattern(params,0),this.divinerEyeCurrentChangeDelay,true);
                }
            }
        }




    }
    public CompoundTag initNBTData(CompoundTag nbt){
        ListTag listTag=new ListTag();
        for (BlockPos blockPos:currentMoonstonePos.keySet()){
            CompoundTag compoundTag=new CompoundTag();
            compoundTag.putInt("x",blockPos.getX());
            compoundTag.putInt("y",blockPos.getY());
            compoundTag.putInt("z",blockPos.getZ());
            compoundTag.putInt("timer", currentMoonstonePos.get(blockPos));
            listTag.add(compoundTag);
        }
        nbt.put("moonstoneData",listTag);
        CompoundTag divEyeTag=new CompoundTag();
        divEyeTag.putInt("cd",this.divinerEyeCD);
        divEyeTag.putInt("fromState",this.divinerEyeFromState);
        divEyeTag.putInt("toState",this.divinerEyeToState);
        divEyeTag.putInt("changeDelay",this.divinerEyeCurrentChangeDelay);
        divEyeTag.putFloat("frameProgress",this.divinerEyeFrameProgress);
        divEyeTag.putFloat("chance",this.divinerEyeChance);
        divEyeTag.putInt("timer",this.divinerEyeTimer);
        divEyeTag.putInt("power",this.divinerEyePower);
        divEyeTag.putInt(nbtName_DivSunControlVal,this.divinerSunControlVal);
        divEyeTag.putInt(nbtName_DivSunControlTime,this.divinerSunControlTimer);
        nbt.put("divEye",divEyeTag);
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
        if (level!=null){
            this.levelResourceKey=level.dimension();
        }
    }

    public void changeDivPower(int i){
        this.divinerEyePower=Math.max(Math.min(this.divinerEyePower+i,100),0);
    }


    public boolean decrementSunControlTimer(){
        if (divinerSunControlTimer>0){
            this.divinerSunControlTimer =Math.max(0,this.divinerSunControlTimer-1);
            if (this.divinerSunControlTimer ==0){
                this.divinerSunControlVal=0;
                return true;
            }
        }
        return false;
    }
    public void setSunControlVal(int i){
        this.divinerSunControlVal=Math.max(-10,Math.min(10,i));
    }
}
