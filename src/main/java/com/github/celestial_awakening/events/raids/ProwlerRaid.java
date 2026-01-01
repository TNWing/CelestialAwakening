package com.github.celestial_awakening.events.raids;

import com.github.celestial_awakening.capabilities.LevelCapabilityProvider;
import com.github.celestial_awakening.entity.living.night_prowlers.AbstractNightProwler;
import com.github.celestial_awakening.entity.living.night_prowlers.ProwlerWhelp;
import com.github.celestial_awakening.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
        WHELP(EntityInit.NIGHT_PROWLER_WHELP.get()){
            @Override
            public AbstractNightProwler createProwler(ServerLevel serverLevel) {
                return new ProwlerWhelp(EntityInit.NIGHT_PROWLER_WHELP.get(),serverLevel);
            }
        };
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
        HashMap<Byte,ProwlerVals> waveMap;
        ProwlerType(EntityType t){
            this.type=t;
            waveMap=new HashMap<>();
        }

        public abstract AbstractNightProwler createProwler(ServerLevel serverLevel);
        public void setWaveVals(Byte waveNum,ProwlerVals vals){
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
        int infusementRollChance;
        int infuseLim;
        int bonusRollChance;
        public ProwlerVals(){
            reps=0;
        }
        public ProwlerVals(int reps,int minVal,int minCnt,int bonusVal,int bonusCnt, int bonusRC, int iCost, int iLim, int iRC){
            this.reps=reps;
            this.minUnitVal=minVal;
            this.minUnitCnt=minCnt;
            this.bonusUnitVal=bonusVal;
            this.bonusUnitCnt=bonusCnt;
            this.bonusRollChance=bonusRC;
            this.infusementCost=iCost;
            this.infusementRollChance=iRC;
            this.infuseLim=iLim;
        }
    }

    public boolean isDone(){
        System.out.println("curr WAVE IS " + currentWave + "  WITH MAX " + maxWave);//not reaching at all
        return currentWave>=maxWave;
    }
    /*
    maps wave to prowlerType
     */
private static final Map<Byte, List<ProwlerType>> prowlerMap =new HashMap<>();

    public static void initProwlerRaidData(){
        prowlerMap.clear();
        ProwlerVals p=new ProwlerVals(1,3,3,2,2,20,2,2,15);
        ProwlerRaid.ProwlerType.WHELP.setWaveVals((byte) 0,p);
        prowlerMap.put((byte) 0,List.of(ProwlerType.WHELP));
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
    /*
    code used to determine valid spawning blocks for illager raids
     */

    private BlockPos findRandomSpawnCenter(RandomSource randomSource, BlockPos center, int range){
        /*
                    ArrayList<BlockPos> applicableBlocks=new ArrayList<>();
            for (int cx=-1;cx<=1;cx++){
                for (int cz=-1;cz<=1;cz++){
                    LevelChunk chunk=level.getChunk(chunkPos.x+cx,chunkPos.z+cz);
                    for (int x=0;x<16;x++){
                        for (int z=0;z<16;z++){
                            //for future changes
                            //instead of the 16 by 16
                            //check each 4 by 4 square in a chunk
                            int y = level.getHeight(Heightmap.Types.WORLD_SURFACE, chunk.getPos().x*16+x, chunk.getPos().z*16+z);//highest block
                            if (Math.abs(y-targetCenter.getY())<=15){
                                BlockPos blockPos=new BlockPos(chunk.getPos().x*16+x,y,chunk.getPos().z*16+z);
                                applicableBlocks.add(blockPos);
                            }
                        }
                    }
                }
            }
         */
        //attempts to find a 3 by 3 area to spawn the entities
        ArrayList<BlockPos> applicableBlocks=new ArrayList<>();
        for (int x=-range;x<=range;x+=2){
            for (int z=-range;z<=range;z+=2){
                int cX=center.getX()+x;
                int cZ=center.getZ()+z;
                int y=getServerLevel().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,cX,cZ);
                boolean valid=true;
                if (Math.abs(y-center.getY())<=12){
                  //if it finds a valid blockpos, offset both the x and z by like an addition 1 or 2
                    for (int xo=-1;xo<=1;xo++){
                        for (int zo=-1;zo<=1;zo++){
                            if (!valid){
                                break;
                            }
                            for (int yo=0;yo<=2;yo++){
                                BlockPos pos=new BlockPos(cX,y+yo,cZ);
                                if (!getServerLevel().getBlockState(pos).isAir()){
                                    valid=false;
                                    break;
                                }
                            }

                        }
                    }
                }
                if (valid){
                    applicableBlocks.add(new BlockPos(cX,y,cZ));
                }
            }
        }
        if (!applicableBlocks.isEmpty()){
            return applicableBlocks.get(randomSource.nextInt(applicableBlocks.size()));
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
                    List<AbstractNightProwler> prowlersToSpawn=new ArrayList<>();
                    List<ProwlerType> prowlerTypes=prowlerMap.get(currentWave);
                    if (prowlerTypes!=null){
                        int strength=getRaidStrength();
                        while (strength>0){
                            for(ProwlerType pType:prowlerTypes){
                                ProwlerVals vals=pType.waveMap.get(currentWave);
                                for (int r=0;r<vals.reps;r++){
                                    int minCnt=Math.min(vals.minUnitCnt,strength/vals.minUnitVal);
                                    strength-=minCnt*vals.minUnitVal;
                                    int infuseCnt=0;
                                    for (int c=0;c<minCnt;c++){
                                        AbstractNightProwler prowler=pType.createProwler(getServerLevel());
                                        if (infuseCnt< vals.infuseLim && strength>=vals.infusementCost){
                                            if (getServerLevel().getRandom().nextInt(100)<vals.infusementRollChance){
                                                infuseCnt++;
                                                strength-=vals.infusementCost;
                                                if (getServerLevel().getRandom().nextBoolean()){
                                                    prowler.setInfuse(1);
                                                }
                                                else{
                                                    prowler.setInfuse(-1);
                                                }
                                            }
                                        }
                                        prowlersToSpawn.add(prowler);
                                    }
                                    //System.out.println("INFUSE IS " + infuseCnt);
                                    for (int b=0;b<vals.bonusUnitCnt;b++){//TODO
                                        if (strength<vals.bonusUnitVal){
                                            break;
                                        }
                                    }
                                }
                                if (strength<=0){
                                    break;
                                }
                            }
                            strength--;//just to prevent it from endlessly looping for now
                        }
                    }
                    if (!prowlersToSpawn.isEmpty()){
                        BlockPos spt=findRandomSpawnCenter(getServerLevel().getRandom(),centerPos,24);
                        if (spt!=null){
                            for (AbstractNightProwler prowler:prowlersToSpawn) {

                                prowler.setPos(spt.getCenter());
                                getServerLevel().addFreshEntity(prowler);
                                prowler.setRaid(this);
                                prowlers.add(prowler);
                            }

                        }

                    }
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
