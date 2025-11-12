package com.github.celestial_awakening.events.raids;

import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.github.celestial_awakening.capabilities.PlayerCapability;
import com.github.celestial_awakening.capabilities.PlayerCapabilityProvider;
import com.github.celestial_awakening.entity.living.night_prowlers.AbstractNightProwler;
import com.github.celestial_awakening.entity.living.night_prowlers.ProwlerWhelp;
import com.github.celestial_awakening.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProwlerRaid extends AbstractCARaid{
    /*
    Mechanics
    -periodically, increment a player-specific counter at night
    -if the counter is above a given value, store the player as a target and reset the counter
    -for each player stored in the target list
        -create a raid
        -check for any nearby players. remove players in target list that are nearby the current player target

     */
    public BlockPos getCenterPos() {
        return centerPos;
    }

    public void setCenterPos(BlockPos centerPos) {
        this.centerPos = centerPos;
    }

    private BlockPos centerPos;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private boolean active;

    private int warningTriggerTime;

    private List<AbstractNightProwler> prowlers=new ArrayList<>();

    private int delayTicks=60;

    private int currentWave=1;

    private int maxWave;

    private int nextWaveInterval;


    /*
strength determines possible units to spawn, each unit also has a var called Reps
wave # determines the # of units & max unit value

ex:
base prowler has unit val of 5
w/ infusement, it gains +4 val

to determine what units to spawn, rolls for unit that has most base val first
then, perform rolls to determine infusement
afterwords, repeats with next unit with highest base val

so if we got Hunter,Whelp. Hunter has 1 rep, whelp has 2 rep
adds Hunter to spawn group, rolls to set infusement, then adds whelp if it fits, rolls to set infusement.
Adds another whelp if theres space, roll infusement. loop back to hunter.
when it loops, adds an additional roll to determine if the unit is added to group (in order to reduce likelyhood of excess elites)

     */
/*
make this a separate class and init the values based on config vals later down the line
 */
    public enum ProwlerType{
        WHELP(EntityInit.NIGHT_PROWLER_WHELP.get());
        /*
        should include
        -entity type
        -hashmap that maps wave val to the following (Prowler Vals)
            -reps
            -base val
            -infusement bonus val
            -roll chance (for loops)
            -max amt (-1 for none)
         */
        ;
        EntityType type;
        HashMap<Integer,ProwlerVals> waveMap;
        ProwlerType(EntityType t){
            this.type=t;
            waveMap=new HashMap<>();
        }
        public void setWaveVals(Integer num,ProwlerVals vals){
            waveMap.put(num,vals);
        }

    }
    //ProwlerType Whelp=new ProwlerType(EntityInit.NIGHT_PROWLER_WHELP.get());



    public static class ProwlerVals{
        int reps;
        int baseVal;
        int infusementBonus;
        int rollChance;
        int maxAmt;
        public ProwlerVals(){
            reps=0;
        }
        public ProwlerVals(int r,int bV, int i, int rC, int max){
            reps=r;
            baseVal=bV;
            infusementBonus=i;
            rollChance=rC;
            maxAmt=max;
        }
        public ProwlerVals(Integer[] vals){
            reps=vals[0];
            baseVal=vals[1];
            infusementBonus=vals[2];
            rollChance=vals[3];
            maxAmt=vals[4];
        }
    }

    public boolean isDone(){
        return currentWave>=maxWave;
    }
    /*
    maps wave to prowlerType
     */
    private HashMap<Integer,List<ProwlerType>> prowlerMap=new HashMap<>();

    public static void initProwlerRaidData(){
        ProwlerRaid.ProwlerType.WHELP.setWaveVals(0,new ProwlerVals());
    }


    public ProwlerRaid(int p_37692_, ServerLevel p_37693_, BlockPos p_37694_, int maxWaves,int str) {
        this.setRaidID(p_37692_);
        this.setServerLevel(p_37693_);
        this.active = true;
        this.setRaidStrength(str);
        //this.raidCooldownTicks = 300;
        //this.raidEvent.setProgress(0.0F);
        this.setCenterPos(p_37694_);
        this.maxWave=maxWaves;
        //this.numGroups = this.getNumGroups(p_37693_.getDifficulty());
        //this.status = Raid.RaidStatus.ONGOING;
    }
    /*
    raid strength scaaling works like this
    wave 1 uses 50% of strength compared to max wave (100% strength)
    strength: linear scaling between the waves
     */

    /*
Data to store
-raid id
-warning trigger time
-delay before start
-raid strength
-raid center pos

Important notes
-vanilla illagers store the raid ids, in order to associate themselves with raids. when said illager dies, it informs the raid of its death
-warning trigger time is the time when the warning of the raid played. as such, trigger time+delay will produce the time the raid should occur.


Config should have these settings
-raid warnings prevent sleeping
-raid prowlers despawn during day
 */

    public CompoundTag save(CompoundTag tag) {
        super.save(tag);
        tag.putInt("CX",this.getCenterPos().getX());
        tag.putInt("CY",this.getCenterPos().getY());
        tag.putInt("CZ",this.getCenterPos().getZ());
        tag.putInt("MW",this.maxWave);
        /*
        Vanilla raid data



        p_37748_.putInt("Id", this.id);
        p_37748_.putBoolean("Started", this.started);
        p_37748_.putBoolean("Active", this.active);
        p_37748_.putLong("TicksActive", this.ticksActive);
        p_37748_.putInt("BadOmenLevel", this.badOmenLevel);
        p_37748_.putInt("GroupsSpawned", this.groupsSpawned);
        p_37748_.putInt("PreRaidTicks", this.raidCooldownTicks);
        p_37748_.putInt("PostRaidTicks", this.postRaidTicks);
        p_37748_.putFloat("TotalHealth", this.totalHealth);
        p_37748_.putInt("NumGroups", this.numGroups);
        p_37748_.putString("Status", this.status.getName());
        p_37748_.putInt("CX", this.center.getX());
        p_37748_.putInt("CY", this.center.getY());
        p_37748_.putInt("CZ", this.center.getZ());
        ListTag listtag = new ListTag();

        for(UUID uuid : this.heroesOfTheVillage) {
            listtag.add(NbtUtils.createUUID(uuid));
        }

        p_37748_.put("HeroesOfTheVillage", listtag);

         */
        return tag;
    }

    public boolean attemptToStart(Player target){
        Level level=target.level();
        DifficultyInstance difficultyInstance= level.getCurrentDifficultyAt(target.blockPosition());
        float effectiveDifficulty=difficultyInstance.getEffectiveDifficulty();
        System.out.println("Effective diff is " + difficultyInstance);
        LazyOptional<PlayerCapability> pCapOptional=target.getCapability(PlayerCapabilityProvider.capability);
        AtomicBoolean result= new AtomicBoolean(false);
        pCapOptional.ifPresent(cap->{
            if (effectiveDifficulty>1.5f && cap.getProwlerRaidCounter()>5){
                int lY = level.getHeight(Heightmap.Types.WORLD_SURFACE, target.getBlockX(), target.getBlockZ());
                if (this.getCenterPos()!=null){//will not attempt spawn if too far underground
                    // && this.getCenterPos().getY()>=lY-20
                    result.set(true);
                }
            }
        });

        return result.get();
    }

    public List<AbstractNightProwler> prowlersToSpawn(){
        int strength=this.getRaidStrength();
        List<AbstractNightProwler> prowlers=new ArrayList<>();

        return prowlers;
    }
    /*
    code used to determine valid spawning blocks for illager raids
     */
    @Nullable
    private BlockPos findRandomSpawnPos(int p_37708_, int p_37709_) {
        int i = p_37708_ == 0 ? 2 : 2 - p_37708_;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int i1 = 0; i1 < p_37709_; ++i1) {
            float f = this.getServerLevel().random.nextFloat() * ((float)Math.PI * 2F);
            int j = this.getCenterPos().getX() + Mth.floor(Mth.cos(f) * 32.0F * (float)i) + this.getServerLevel().random.nextInt(5);
            int l = this.getCenterPos().getZ() + Mth.floor(Mth.sin(f) * 32.0F * (float)i) + this.getServerLevel().random.nextInt(5);
            int k = this.getServerLevel().getHeight(Heightmap.Types.WORLD_SURFACE, j, l);
            blockpos$mutableblockpos.set(j, k, l);
            if (!this.getServerLevel().isVillage(blockpos$mutableblockpos) || p_37708_ >= 2) {
                int j1 = 10;
                if (this.getServerLevel()
                        .hasChunksAt(blockpos$mutableblockpos.getX() - 10, blockpos$mutableblockpos.getZ() - 10, blockpos$mutableblockpos.getX() + 10, blockpos$mutableblockpos.getZ() + 10)
                        && this.getServerLevel().isPositionEntityTicking(blockpos$mutableblockpos)
                        && (
                                NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, this.getServerLevel(), blockpos$mutableblockpos, EntityType.RAVAGER)
                                        || this.getServerLevel().getBlockState(blockpos$mutableblockpos.below()).is(Blocks.SNOW) && this.getServerLevel().getBlockState(blockpos$mutableblockpos).isAir())) {
                    return blockpos$mutableblockpos;
                }
            }
        }

        return null;
    }

    public void tick() {
        if (isActive())
        if (this.getServerLevel().getGameTime()>=warningTriggerTime+delayTicks){

        }
        nextWaveInterval--;
        if (prowlers.isEmpty() && nextWaveInterval>80){
            nextWaveInterval=80;
            /*
            expedite the spawning if all prowlers are dead
             */
        }
        if (nextWaveInterval<=0){
            nextWaveInterval=700;
            currentWave++;
/*
            if (currentWave>=maxWave){
                this.getServerLevel().getCapability(LevelCapabilityProvider.LevelCap).ifPresent(cap->{
                    cap.raids.removeProwlerRaid(this.getRaidID());
                });
            }

 */
        }

    }
}
