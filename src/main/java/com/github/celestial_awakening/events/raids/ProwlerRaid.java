package com.github.celestial_awakening.events.raids;

import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.github.celestial_awakening.entity.living.night_prowlers.AbstractNightProwler;
import com.github.celestial_awakening.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public long getWarningTriggerTime() {
        return warningTriggerTime;
    }

    public void setWarningTriggerTime(long warningTriggerTime) {
        this.warningTriggerTime = warningTriggerTime;
    }

    private long warningTriggerTime;

    private List<AbstractNightProwler> prowlers=new ArrayList<>();

    public int getDelayTicks() {
        return delayTicks;
    }

    public void setDelayTicks(int delayTicks) {
        this.delayTicks = delayTicks;
    }

    public byte getCurrentWave() {
        return currentWave;
    }

    public void setCurrentWave(byte currentWave) {
        this.currentWave = currentWave;
    }

    public byte getMaxWave() {
        return maxWave;
    }

    public void setMaxWave(byte maxWave) {
        this.maxWave = maxWave;
    }

    public int getNextWaveInterval() {
        return nextWaveInterval;
    }

    public void setNextWaveInterval(int nextWaveInterval) {
        this.nextWaveInterval = nextWaveInterval;
    }

    private int delayTicks=360;

    private byte currentWave=0;

    private byte maxWave;

    private int nextWaveInterval=10;


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
        public void setWaveVals(Integer waveNum,ProwlerVals vals){
            waveMap.put(waveNum,vals);
        }

    }
    //ProwlerType Whelp=new ProwlerType(EntityInit.NIGHT_PROWLER_WHELP.get());



    public static class ProwlerVals{
        int reps;
        int minUnitVal;
        int minUnitCnt;
        int bonusUnitCnt;
        int bonusUnitVal;
        int infusementCost;
        int bonusRollChance;
        public ProwlerVals(){
            reps=0;
        }
        public ProwlerVals(int r,int bV, int i, int rC, int max){
            reps=r;
            minUnitVal =bV;
            infusementCost =i;
            bonusRollChance=rC;
        }
        public ProwlerVals(int reps,int minVal,int minCnt,int bonusVal,int bonusCnt, int bonusRC, int iCost){
            this.reps=reps;
            this.minUnitVal=minVal;
            this.minUnitCnt=minCnt;
            this.bonusUnitVal=bonusVal;
            this.bonusUnitCnt=bonusCnt;
            this.bonusRollChance=bonusRC;
            this.infusementCost=iCost;
        }
        public ProwlerVals(Integer[] vals){
            reps=vals[0];
            minUnitVal =vals[1];
            infusementCost =vals[2];
            bonusRollChance=vals[3];
        }
    }

    public boolean isDone(){
        return currentWave>=maxWave;
    }
    /*
    maps wave to prowlerType
     */
    private static HashMap<Integer,List<ProwlerType>> prowlerMap=new HashMap<>();

    public static void initProwlerRaidData(){
        ProwlerRaid.ProwlerType.WHELP.setWaveVals(0,new ProwlerVals(1,5,4,100,5));

        prowlerMap.put(0,List.of(ProwlerType.WHELP));
    }


    public ProwlerRaid(int id, ServerLevel serverLevel, BlockPos blockPos, byte maxWaves, int str) {
        this.setRaidID(id);
        this.setServerLevel(serverLevel);
        this.active = true;
        this.setRaidStrength(str);
        //this.raidCooldownTicks = 300;
        //this.raidEvent.setProgress(0.0F);
        this.setCenterPos(blockPos);
        this.maxWave=maxWaves;
        this.warningTriggerTime=serverLevel.getGameTime();
        this.setStatus(RaidStatus.WAITING);
        //this.numGroups = this.getNumGroups(serverLevel.getDifficulty());
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
        if (isActive()){
            if (this.getServerLevel().getGameTime()<warningTriggerTime+delayTicks){//not ready
                if (((warningTriggerTime+delayTicks-this.getServerLevel().getGameTime())%120)==0){
                    this.getServerLevel().playSound(null,getCenterPos(), SoundEvents.WOLF_GROWL, SoundSource.HOSTILE,0.4f,1f);
                }

            }
            else{
                nextWaveInterval--;
                if (prowlers.isEmpty() && nextWaveInterval>80){
                    nextWaveInterval=80;
            /*
            expedite the spawning if all prowlers are dead
             */
                }
                if (nextWaveInterval<=0){
                    /*
                    So given a wave # and strength, do the following
Loop over the prowlertypes until no more strength
For each prowler type
Generate a min amount of prowlers (when possible. Otherwise
If any strength left over, rolls infusement on each
After that, roll additional prowlers for the rep, and roll infusement individually
After a rep of prowlers is generated, if there is any leftover strength, perform rolls on infusement
There should be a cap on the max # of infusements per rep
So example:
Start off with 24 strength, min of 3 prowlers for this rep (3 per prowler), infusement requires 2 strength per unit, max of 2 infusements, up to 2 additional prowlers (rng), 2 strength per additional unit
Check to see if can create full rep. We can, so create 3 prowlers
Create 3 prowlers, now have 15 strength
Roll infusements on prowlers, 1 successful infusement, now have 13 strength
Create prowler, successful infusement, now has 9 strength
Create prowler, cannot infuse again, now has 7 strength
Check to see if we can create full rep. We cant, but can create 2 prowlers. So we create them, with 1 strength leftover (can’t be used for anything so we done)

                     */
                    nextWaveInterval=700;
                    List<ProwlerType> prowlerTypes=prowlerMap.get(currentWave);
                    if (prowlerTypes!=null){
                        int strength=getRaidStrength();
                        while (strength>0){
                            for(ProwlerType type:prowlerTypes){
                                ProwlerVals vals=type.waveMap.get(currentWave);
                                for (int r=0;r<vals.reps;r++){

                                }
                            }
                            strength--;//just to prevent it from endlessly looping for now
                        }
                    }


                    /*
                    spawn stuff
                     */
                    currentWave++;

                    if (currentWave>=maxWave){
                        this.getServerLevel().getCapability(LevelCapabilityProvider.LevelCap).ifPresent(cap->{
                            cap.raids.removeProwlerRaid(this.getRaidID());
                        });
                    }


                }
            }
        }
    }
}
